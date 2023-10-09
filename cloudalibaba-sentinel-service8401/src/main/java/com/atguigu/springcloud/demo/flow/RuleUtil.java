package com.atguigu.springcloud.demo.flow;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.util.TimeUtil;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: 统计资源的 QPS（pass、block、total）
 */
public class RuleUtil {

    // 资源名
    static final String KEY = "abc";

    private static volatile boolean stop = false;

    private static final AtomicInteger pass = new AtomicInteger();
    private static final AtomicInteger block = new AtomicInteger();
    private static final AtomicInteger total = new AtomicInteger();

    private static int seconds = 60 + 40;

    /**
     * Description: 开启统计线程，统计资源的 QPS（pass、block、total）
     */
    public static void tick() {
        Thread timer = new Thread(new TimerTask());
        timer.setName("sentinel-timer-task");
        timer.start();
    }

    /**
     * Description: 模拟并发请求
     */
    public static void simulateTraffic(int threadCount) {
        for (int i = 0; i < threadCount; i++) {
            Thread t = new Thread(new RunTask());
            t.setName("sentinel-simulate-traffic-task");
            t.start();
        }
    }

    static class TimerTask implements Runnable {
        @Override
        public void run() {
            Instant start = Instant.now();
            System.out.println("开始统计：");

            long oldTotal = 0;
            long oldPass = 0;
            long oldBlock = 0;
            while (!stop) {
                try {
                    // 每秒统计一次
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long globalTotal = total.get();
                long oneSecondTotal = globalTotal - oldTotal;
                oldTotal = globalTotal;

                long globalPass = pass.get();
                long oneSecondPass = globalPass - oldPass;
                oldPass = globalPass;

                long globalBlock = block.get();
                long oneSecondBlock = globalBlock - oldBlock;
                oldBlock = globalBlock;

                System.out.println(seconds + " 秒发送 QPS 数：" + oneSecondTotal);
                System.out.println(TimeUtil.currentTimeMillis() + "，total:" + oneSecondTotal + ", pass:" + oneSecondPass + ", block:" + oneSecondBlock);

                if (seconds-- <= 0) {
                    stop = true;
                }
            }

            System.out.println("统计结束。");
            System.out.println("总共执行了 " + (Instant.now().getEpochSecond() - start.getEpochSecond()) + " 秒钟");
            System.out.println("total:" + total.get() + ", pass:" + pass.get() + ", block:" + block.get());
            System.exit(0);
        }
    }

    static class RunTask implements Runnable {

        @Override
        public void run() {
            while (!stop) {
                // 1、设置资源名
                try (Entry entry = SphU.entry(KEY)) {
                    // 2、被保护的业务逻辑
                    pass.addAndGet(1);
                } catch (BlockException e) {
                    // 3、如果被保护的资源被限流了，就会抛出 BlockException
                    block.incrementAndGet();
                } finally {
                    total.incrementAndGet();
                }

                Random random = new Random();
                try {
                    TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
