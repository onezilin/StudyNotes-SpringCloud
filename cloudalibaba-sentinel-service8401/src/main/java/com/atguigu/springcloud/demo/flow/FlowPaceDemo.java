package com.atguigu.springcloud.demo.flow;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: controlBehavior 流控效果为 RATE_LIMITER（匀速排队）的例子
 * <p>
 * 详见：
 * <a href="https://github.com/alibaba/Sentinel/blob/master/sentinel-demo/sentinel-demo-basic/src/main/java/com/alibaba/csp/sentinel/demo/flow/PaceFlowDemo.java">PaceFlowDemo</a>
 */
@Slf4j
public class FlowPaceDemo {

    private static final int count = 10;

    private static volatile CountDownLatch countDownLatch;

    private static final Integer requestQps = 100;

    private static final AtomicInteger done = new AtomicInteger();
    private static final AtomicInteger pass = new AtomicInteger();
    private static final AtomicInteger block = new AtomicInteger();

    /**
     * Description:
     * 测试步骤：
     * 1、模拟脉冲流量：每次 100 个线程，每个线程随机对资源（KEY）发送请求发送 1 个请求；间隔 2s 后重复前面步骤
     * 2、执行 initDefaultFlowRule() 流控规则：设置 QPS 阈值为 10，流控效果为 CONTROL_BEHAVIOR_DEFAULT（直接拒绝）
     * 3、执行 initPaceFlowRule() 流控规则：设置 QPS 阈值为 10，流控效果为 CONTROL_BEHAVIOR_RATE_LIMITER（匀速排队）
     * <p>
     * 预期：
     * 1、总共 500 次请求
     * 2、直接拒绝：pass 为 140 左右，block 为 360 左右。（首次达到阈值时不会触发 BlockException，因此第一次 100 个请求过来时全部 pass）
     * 3、匀速排队：pass 为 500，block 为 0
     */
    @SneakyThrows
    public static void main(String[] args) {
        System.out.println("===== begin to do flow control");

        countDownLatch = new CountDownLatch(1);
        initDefaultFlowRule();
        // initPaceFlowRule();
        simulatePulseFlow();
        countDownLatch.await();

        System.out.println("done");
        System.out.println("total pass:" + pass.get() + ", total block:" + block.get());
    }

    private static void initPaceFlowRule() {
        List<FlowRule> list = new ArrayList<>();
        FlowRule rule1 = new FlowRule();
        rule1.setResource(RuleUtil.KEY);
        rule1.setGrade(RuleConstant.FLOW_GRADE_QPS); // RATE_LIMITER 流控效果只对 QPS 生效
        rule1.setCount(count);
        /*
         * 限流效果为匀速排队，即匀速通过，对应的是漏桶算法。
         * 当请求大于阈值后，请求会被排队，按照匀速通过的速率（1000 / count 毫秒间隔，例如这里是 10，则每 100ms 通过一个请求）通过，而不是直接拒绝。
         * 当排队时间大于 maxQueueingTimeMs 后，请求会被拒绝，抛出 BlockException 异常
         */
        rule1.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER);
        rule1.setMaxQueueingTimeMs(20000); // 最长排队时间为 20 秒

        list.add(rule1);
        FlowRuleManager.loadRules(list);
    }

    private static void initDefaultFlowRule() {
        List<FlowRule> rules = new ArrayList<FlowRule>();
        FlowRule rule1 = new FlowRule();
        rule1.setResource(RuleUtil.KEY);
        rule1.setCount(count);
        rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule1.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);

        rules.add(rule1);
        FlowRuleManager.loadRules(rules);
    }

    /**
     * Description: 模拟脉冲流量，如下图所示：
     * <p>
     * request数量
     * ↑
     * |      _              _              _              _              _              _
     * |    /  \           /  \           /  \           /  \           /  \           /  \
     * |   /    \         /    \         /    \         /    \         /    \         /    \
     * |  /      \       /      \       /      \       /      \       /      \       /      \
     * | /        \     /        \     /        \     /        \     /        \     /        \
     * |/          \ _ /          \ _ /          \ _ /          \ _ /          \ _ /          \ _
     * ————————————————————————————————————————————————————————————> time
     * <p>
     * RateLimter 可以达到削峰填谷的效果
     * <p>
     * request数量
     * ↑
     * |
     * |
     * |    _____          _____          _____          _____          _____          _____
     * |  /      \       /      \       /      \       /      \       /      \       /      \
     * | /        \_____/        \_____/        \_____/        \_____/        \_____/        \_____
     * |/
     * ————————————————————————————————————————————————————————————> time
     */
    private static void simulatePulseFlow() {
        for (int i = 0; i < count; i++) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e1) {
                // ignore
            }

            if (i % 2 == 0) continue;
            for (int j = 0; j < requestQps; j++) {
                Thread thread = new Thread(() -> {
                    Instant start = Instant.now();
                    try (Entry entry = SphU.entry(RuleUtil.KEY)) {
                        pass.incrementAndGet();
                        log.info("请求通过，耗时：{}ms", (Instant.now().toEpochMilli() - start.toEpochMilli()));
                    } catch (BlockException e) {
                        block.incrementAndGet();
                    }
                });
                thread.start();
                if (done.incrementAndGet() >= count / 2 * requestQps) {
                    countDownLatch.countDown();
                }
            }
        }
    }
}
