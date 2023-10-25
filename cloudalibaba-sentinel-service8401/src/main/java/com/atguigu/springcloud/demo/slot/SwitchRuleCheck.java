package com.atguigu.springcloud.demo.slot;

import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Description: 开关规则检查类，用于检查资源是否在规则中
 */
public class SwitchRuleCheck {

    /**
     * Description: 只要有一个规则【include 当前资源】并且【开关是 SWITCH_KEY_CLOSE 关闭的】，就抛出异常
     *
     * @param resource 资源
     * @param context  上下文
     */
    public void checkSwitch(ResourceWrapper resource, Context context) throws SwitchException {
        List<SwitchRule> switchRulesSet = SwitchRuleManager.getRules();
        for (SwitchRule rule : switchRulesSet) {
            if (!rule.getStatus().equals(SwitchRule.SWITCH_KEY_OPNE)) {
                continue;
            }
            if (rule.getResources() == null) {
                continue;
            }
            if (!CollectionUtils.isEmpty(rule.getResources().getInclude())) {
                if (rule.getResources().getInclude().contains(resource.getName())) {
                    throw new SwitchException(resource.getName(), "switch");
                }
            }
            if (!CollectionUtils.isEmpty(rule.getResources().getExclude())) {
                if (!rule.getResources().getExclude().contains(resource.getName())) {
                    throw new SwitchException(resource.getName(), "switch");
                }
            }
        }
    }
}
