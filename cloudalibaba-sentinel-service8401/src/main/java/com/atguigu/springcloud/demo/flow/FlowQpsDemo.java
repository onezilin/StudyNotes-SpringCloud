package com.atguigu.springcloud.demo.flow;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: controlBehavior 流控效果为 CONTROL_BEHAVIOR_DEFAULT（快速失败）的例子
 * <p>
 * 详见：
 * <a href="https://github.com/alibaba/Sentinel/blob/master/sentinel-demo/sentinel-demo-basic/src/main/java/com/alibaba/csp/sentinel/demo/flow/FlowQpsDemo.java">FlowQpsDemo</a>
 */
public class FlowQpsDemo {

    /**
     * Description:
     * 测试步骤：
     * 1、设置 QPS 阈值为 20，流控效果为 CONTROL_BEHAVIOR_DEFAULT（快速失败）
     * 2、模拟并发请求，32 个线程，每个线程随机对资源（KEY）发送请求
     * 3、统计 100 秒内 QPS 的通过、阻塞、总数
     * <p>
     * 预期：
     * QPS 的 pass 平均为 20，其他请求都会执行失败，抛出 BlockException 异常
     */
    public static void main(String[] args) throws Exception {
        initDefaultFlowRule();
        RuleUtil.tick();
        RuleUtil.simulateTraffic(32);

        System.out.println("===== begin to do flow control");
    }

    /**
     * Description: 初始化 FlowRule 流控规则
     */
    private static void initDefaultFlowRule() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule1 = new FlowRule();
        rule1.setResource(RuleUtil.KEY);
        rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule1.setCount(20); // QPS 阈值为 20

        rules.add(rule1);
        FlowRuleManager.loadRules(rules);
    }
}
