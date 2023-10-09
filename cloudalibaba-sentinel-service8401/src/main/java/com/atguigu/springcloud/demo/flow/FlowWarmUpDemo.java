package com.atguigu.springcloud.demo.flow;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: controlBehavior 流控效果为 CONTROL_BEHAVIOR_WARM_UP（warm up）时的例子
 * <p>
 * 详见：
 * <a href="https://github.com/alibaba/Sentinel/blob/master/sentinel-demo/sentinel-demo-basic/src/main/java/com/alibaba/csp/sentinel/demo/flow/WarmUpFlowDemo.java>WarmUpFlowDemo</a>
 */
public class FlowWarmUpDemo {

    /**
     * Description:
     * 测试步骤：
     * 1、设置 QPS 阈值为 20，流控效果为 CONTROL_BEHAVIOR_WARM_UP（快速失败）， 预热时长为 10 秒
     * 2、模拟并发请求，3 个线程，每个线程随机对资源（KEY）发送请求（此时还没有触发流量控制）
     * 3、模拟并发请求，32 个线程，每个线程随机对资源（KEY）发送请求（触发流量控制）
     * 4、统计 100 秒内 QPS 的通过、阻塞、总数
     * <p>
     * 预期：
     * 1、没有触发流量控制时：请求全部通过
     * 2、5 秒后，大量请求到来，触发 warm-up 流控效果，从 `count \ codeFactor` （这里计算可得值为 7）开始，经过预热时长 10 秒，
     * 才到达设置的 QPS 阈值，期间超出的请求都会执行失败，抛出 BlockException 异常
     */
    @SneakyThrows
    public static void main(String[] args) {
        initWarmUpFlowRule();
        RuleUtil.tick();
        RuleUtil.simulateTraffic(3);
        Thread.sleep(5000);
        RuleUtil.simulateTraffic(32);

        System.out.println("===== begin to do flow control");
    }

    private static void initWarmUpFlowRule() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule1 = new FlowRule();
        rule1.setResource(RuleUtil.KEY);
        rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule1.setCount(20); // QPS 阈值为 20
        rule1.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_WARM_UP);
        rule1.setWarmUpPeriodSec(10); // 预热时长为 10 秒（也就是 10 秒后才到达 QPS 阈值）

        rules.add(rule1);
        FlowRuleManager.loadRules(rules);
    }
}
