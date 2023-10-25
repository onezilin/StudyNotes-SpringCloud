package com.atguigu.springcloud.demo.slot;

import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.slotchain.*;
import com.alibaba.csp.sentinel.spi.SpiOrder;
import com.alibaba.csp.sentinel.util.SpiLoader;

import java.util.List;

/**
 * Description: 由于引入了 sentinel-parameter-flow-control 依赖后，Sentinel 默认采用 HotParamSlotChainBuilder，
 * 因此自己项目需要手动通过 SPI 引入 DefaultSlotChainBuilder
 * <p>
 * 注意：添加 @SpiOrder 注解，否则无效
 */
@SpiOrder
public class DefaultSlotChainBuilder implements SlotChainBuilder {

    @Override
    public ProcessorSlotChain build() {
        ProcessorSlotChain chain = new DefaultProcessorSlotChain();

        // Note: the instances of ProcessorSlot should be different, since they are not stateless.
        List<ProcessorSlot> sortedSlotList = SpiLoader.loadPrototypeInstanceListSorted(ProcessorSlot.class);
        for (ProcessorSlot slot : sortedSlotList) {
            if (!(slot instanceof AbstractLinkedProcessorSlot)) {
                RecordLog.warn("The ProcessorSlot(" + slot.getClass().getCanonicalName() + ") is not an instance of " +
                        "AbstractLinkedProcessorSlot, can't be added into ProcessorSlotChain");
                continue;
            }

            chain.addLast((AbstractLinkedProcessorSlot<?>) slot);
        }

        return chain;
    }
}
