package com.atguigu.springcloud.demo.slot;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;

import java.util.Collections;

/**
 * Description: 指定 main 方法，查看 SwitchSlot 是否执行
 */
public class SlotDemo {

    private static final String KEY = "abc";

    public static void main(String[] args) {
        initSwitchRule();

        Entry entry = null;
        try {
            entry = SphU.entry("abc");
        } catch (BlockException ex) {
            ex.printStackTrace();
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
    }

    public static void initSwitchRule() {
        SwitchRule rule = new SwitchRule();
        rule.setStatus(SwitchRule.SWITCH_KEY_CLOSE);
        SwitchRule.Resources resources = new SwitchRule.Resources();
        resources.setInclude(Collections.singleton(KEY));
        rule.setResources(resources);
        SwitchRuleManager.loadRules(Collections.singletonList(rule));
    }
}
