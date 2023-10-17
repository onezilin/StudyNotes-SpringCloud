package com.atguigu.springcloud.demo.degrade;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreaker;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.EventObserverRegistry;
import com.alibaba.csp.sentinel.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: grade 熔断策略为 SLOW_REQUEST_RATIO（慢调用比例）的例子
 * <p>
 * 详见：
 * <a href="https://github.com/alibaba/Sentinel/blob/master/sentinel-demo/sentinel-demo-basic/src/main/java/com/alibaba/csp/sentinel/demo/degrade/SlowRatioCircuitBreakerDemo.java">SlowRatioCircuitBreakerDemo</a>
 */
public class DegradeSlowRatioDemo {

    static final String KEY = "abc";

    private static AtomicInteger total = new AtomicInteger();
    private static AtomicInteger pass = new AtomicInteger();
    private static AtomicInteger block = new AtomicInteger();
    private static volatile boolean stop = false;
    private static int seconds = 120;

    /**
     * Description:
     * 测试步骤：
     * 1、设置熔断策略为 SLOW_REQUEST_RATIO（慢调用比例），最大响应时间为 50ms，超出 50ms 的请求为慢调用，
     * 统计时长为 20s，最小请求数为 100，慢调用比例阈值为 0.6，熔断时长为 10s
     * 2、上面的配置意思就是：
     * 当 20s 内请求数大于等于 100，且慢调用比例大于等于 0.6 时，开启熔断，熔断时长为 10s；经过 10s 后，熔断器变成 half-open 状态，
     * 若接下来的一个请求响应时间小于 50ms，则熔断器关闭，否则继续开启熔断，熔断时长重新计时。
     * 3、模拟并发请求 100 个线程同时请求，每个线程每次请求的响应时间随机在 40ms 到 60ms 之间，统计 120s 后的熔断状态
     * <p>
     * 预期：
     * 1、刚开始请求全部进来，此时请求数大于 100 且慢调用比例超出阈值，会触发熔断，熔断时长为 10s，10s 后熔断器变成 half-open 状态，
     * 2、此时若接下来的一个请求响应时间小于 50ms，则熔断器关闭，否则继续开启熔断，熔断时长重新计时。
     */
    public static void main(String[] args) {
        initDegradeFule();
        registerStateChangeObserver();
        startTick();

        simulateRequest();
    }

    /**
     * Description: 初始化熔断降级规则
     */
    public static void initDegradeFule() {
        List<DegradeRule> rules = new ArrayList<>();

        DegradeRule rule1 = new DegradeRule();
        rule1.setResource(KEY);
        rule1.setGrade(CircuitBreakerStrategy.SLOW_REQUEST_RATIO.getType());
        rule1.setCount(50); // 最大响应时间
        rule1.setStatIntervalMs(20000); // 统计时长
        rule1.setMinRequestAmount(100); // 最小请求数
        rule1.setSlowRatioThreshold(0.6); // 慢调用比例阈值
        rule1.setTimeWindow(10); // 熔断时长

        rules.add(rule1);
        DegradeRuleManager.loadRules(rules);
    }

    /**
     * Description: 注册熔断监听器
     */
    private static void registerStateChangeObserver() {
        EventObserverRegistry.getInstance().addStateChangeObserver("logging",
                (prevState, newState, rule, snapshotValue) -> {
                    if (newState == CircuitBreaker.State.OPEN) {
                        System.err.printf("%s -> OPEN at %d, snapshotValue=%.2f%n", prevState.name(),
                                TimeUtil.currentTimeMillis(), snapshotValue);
                    } else {
                        System.err.printf("%s -> %s at %d%n", prevState.name(), newState.name(),
                                TimeUtil.currentTimeMillis());
                    }
                });
    }

    private static void startTick() {
        Thread timer = new Thread(new TimerTask());
        timer.setName("sentinel-timer-tick-task");
        timer.start();
    }

    private static void simulateRequest() {
        int concurrency = 100;

        for (int i = 0; i < concurrency; i++) {
            Thread thread = new Thread(() -> {
                while (true) {
                    try (Entry entry = SphU.entry(KEY)) {
                        pass.incrementAndGet();
                        sleep(ThreadLocalRandom.current().nextInt(40, 60));
                    } catch (BlockException e) {
                        block.incrementAndGet();
                        sleep(ThreadLocalRandom.current().nextInt(5, 10));
                    } finally {
                        total.incrementAndGet();
                    }
                }
            });
            thread.setName("sentinel-simulate-traffic-task-" + i);
            thread.start();
        }
    }

    static class TimerTask implements Runnable {
        @Override
        public void run() {
            long start = System.currentTimeMillis();
            System.out.println("Begin to run! Go go go!");
            System.out.println("See corresponding metrics.log for accurate statistic data");

            long oldTotal = 0;
            long oldPass = 0;
            long oldBlock = 0;

            while (!stop) {
                sleep(1000);

                long globalTotal = total.get();
                long oneSecondTotal = globalTotal - oldTotal;
                oldTotal = globalTotal;

                long globalPass = pass.get();
                long oneSecondPass = globalPass - oldPass;
                oldPass = globalPass;

                long globalBlock = block.get();
                long oneSecondBlock = globalBlock - oldBlock;
                oldBlock = globalBlock;

                System.out.println(TimeUtil.currentTimeMillis() + ", total:" + oneSecondTotal
                        + ", pass:" + oneSecondPass + ", block:" + oneSecondBlock);

                if (seconds-- <= 0) {
                    stop = true;
                }
            }

            long cost = System.currentTimeMillis() - start;
            System.out.println("time cost: " + cost + " ms");
            System.out.println("total: " + total.get() + ", pass:" + pass.get()
                    + ", block:" + block.get());
            System.exit(0);
        }
    }

    private static void sleep(int timeMs) {
        try {
            TimeUnit.MILLISECONDS.sleep(timeMs);
        } catch (InterruptedException e) {
            // ignore
        }
    }
}
