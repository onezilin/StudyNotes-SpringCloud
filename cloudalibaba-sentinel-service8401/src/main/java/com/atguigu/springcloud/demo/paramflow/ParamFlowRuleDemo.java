package com.atguigu.springcloud.demo.paramflow;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Description: Sentinel 热点参数限流
 */
public class ParamFlowRuleDemo {

    private static final String KEY = "abc";

    public static String[] ParamArray = new String[]{"A", "B", "C"};

    public static void main(String[] args) {
        initParamFlowRules();

        System.out.println("sleep " + 1 + "s");
        for (int i = 1; i <= 1200; i++) {
            Entry entry = null;
            int arrIndex = (i - 1) % 3;

            try {
                // batchCount 表示本资源占用的 Token 次数，即算作几次调用
                entry = SphU.entry(KEY, EntryType.IN, 1, ParamArray[arrIndex]);
                System.out.println("第：" + i + "次，param " + ParamArray[arrIndex] + " ---------doSomething");
            } catch (BlockException blockException) {
                System.out.println("第：" + i + "次，param " + ParamArray[arrIndex] + " ---------Blocked");
            } finally {
                if (Objects.nonNull(entry)) {
                    // 释放资源时也要带上对应的参数，否则可能统计出错
                    entry.exit(2, ParamArray[arrIndex]);
                }
            }
            if (i % 12 == 0) {
                try {
                    Thread.sleep(1000);
                    System.out.println("sleep " + (i / 12 + 1) + "s");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void initParamFlowRules() {
        List<ParamFlowRule> rules = new ArrayList<>();
        ParamFlowRule rule1 = new ParamFlowRule();
        rule1.setResource(KEY);
        rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule1.setCount(3); // 单个参数限流阈值
        rule1.setBurstCount(10); // 突发流量阈值，使用令牌桶算法，不会自动补充
        rule1.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
        rule1.setDurationInSec(3); // 统计窗口时间长度，单位为秒
        rule1.setParamIdx(0); // 参数索引，从 0 开始

        rules.add(rule1);
        ParamFlowRuleManager.loadRules(rules);
    }
}
