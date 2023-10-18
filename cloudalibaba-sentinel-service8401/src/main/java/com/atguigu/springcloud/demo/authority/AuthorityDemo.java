package com.atguigu.springcloud.demo.authority;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;

import java.util.Collections;

/**
 * Description: 授权规则（黑白名单 AuthorityRule）实例
 * <p>
 * 详见：
 * <a href="https://github.com/alibaba/Sentinel/blob/master/sentinel-demo/sentinel-demo-basic/src/main/java/com/alibaba/csp/sentinel/demo/authority/AuthorityDemo.java">AuthorityDemo</a>
 */
public class AuthorityDemo {

    private static final String RESOURCE_NAME = "testABC";

    public static void main(String[] args) {
        System.out.println("========Testing for black list========");
        initBlackRules();
        testFor(RESOURCE_NAME, "appA");
        testFor(RESOURCE_NAME, "appB");
        testFor(RESOURCE_NAME, "appC");
        testFor(RESOURCE_NAME, "appE");

        System.out.println("========Testing for white list========");
        initWhiteRules();
        testFor(RESOURCE_NAME, "appA");
        testFor(RESOURCE_NAME, "appB");
        testFor(RESOURCE_NAME, "appC");
        testFor(RESOURCE_NAME, "appE");
    }

    private static void testFor(/*@NonNull*/ String resource, /*@NonNull*/ String origin) {
        // 必须先调用 ContextUtil.enter 设置 origin，AuthorityRule 才生效
        ContextUtil.enter(resource, origin);
        Entry entry = null;
        try {
            entry = SphU.entry(resource);
            System.out.println(String.format("Passed for resource %s, origin is %s", resource, origin));
        } catch (BlockException ex) {
            System.err.println(String.format("Blocked for resource %s, origin is %s", resource, origin));
        } finally {
            if (entry != null) {
                entry.exit();
            }
            ContextUtil.exit();
        }
    }

    private static void initWhiteRules() {
        AuthorityRule rule = new AuthorityRule();
        rule.setResource(RESOURCE_NAME);
        rule.setStrategy(RuleConstant.AUTHORITY_WHITE);
        rule.setLimitApp("appA,appE");
        AuthorityRuleManager.loadRules(Collections.singletonList(rule));
    }

    private static void initBlackRules() {
        AuthorityRule rule = new AuthorityRule();
        rule.setResource(RESOURCE_NAME);
        rule.setStrategy(RuleConstant.AUTHORITY_BLACK);
        rule.setLimitApp("appA,appB");
        AuthorityRuleManager.loadRules(Collections.singletonList(rule));
    }
}
