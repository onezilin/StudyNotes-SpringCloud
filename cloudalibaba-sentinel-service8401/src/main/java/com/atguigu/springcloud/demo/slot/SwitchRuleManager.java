package com.atguigu.springcloud.demo.slot;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 开关规则管理类，用于将 SwitchRule 注册进来
 */
public class SwitchRuleManager {

    private static final List<SwitchRule> rules = new ArrayList<>();

    public static List<SwitchRule> getRules() {
        return rules;
    }

    public static void loadRules(List<SwitchRule> ruleList) {
        rules.addAll(ruleList);
    }
}
