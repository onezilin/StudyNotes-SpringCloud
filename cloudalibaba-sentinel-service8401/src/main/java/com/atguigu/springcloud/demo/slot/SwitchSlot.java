package com.atguigu.springcloud.demo.slot;

import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.node.DefaultNode;
import com.alibaba.csp.sentinel.slotchain.AbstractLinkedProcessorSlot;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import com.alibaba.csp.sentinel.spi.SpiOrder;

/**
 * Description: 自定义 Slot
 * <p>
 * 注意：添加 @SpiOrder 注解，否则无效
 */
@SpiOrder(-1500)
public class SwitchSlot extends AbstractLinkedProcessorSlot<DefaultNode> {

    private final SwitchRuleCheck checker;

    public SwitchSlot() {
        this(new SwitchRuleCheck());
    }

    public SwitchSlot(SwitchRuleCheck checker) {
        this.checker = checker;
    }

    @Override
    public void entry(Context context, ResourceWrapper resourceWrapper, DefaultNode node, int count,
                      boolean prioritized,
                      Object... args) throws Throwable {
        System.out.println("------Entering for entry on DemoSlot------");
        System.out.println("Current context: " + context.getName());
        System.out.println("Current entry resource: " + context.getCurEntry().getResourceWrapper().getName());
        checker.checkSwitch(resourceWrapper, context);

        fireEntry(context, resourceWrapper, node, count, prioritized, args);
    }

    @Override
    public void exit(Context context, ResourceWrapper resourceWrapper, int count, Object... args) {
        System.out.println("------Exiting for entry on DemoSlot------");
        System.out.println("Current context: " + context.getName());
        System.out.println("Current entry resource: " + context.getCurEntry().getResourceWrapper().getName());

        fireExit(context, resourceWrapper, count, args);
    }
}
