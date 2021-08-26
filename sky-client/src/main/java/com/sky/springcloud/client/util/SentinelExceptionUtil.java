/**
 * @(#) SentinelExceptionUtil.java 1.0 2021-08-19
 * Copyright (c) 2021, YUNXI. All rights reserved.
 * YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sky.springcloud.client.util;

import com.alibaba.csp.sentinel.slots.block.BlockException;

/**
 * @author 太阿
 * @since 0.1.0
 * @date 2021-08-19
 */
public class SentinelExceptionUtil {
    public static void handleException(BlockException ex) {
        // Handler method that handles BlockException when blocked.
        // The method parameter list should match original method, with the last additional
        // parameter with type BlockException. The return type should be same as the original method.
        // The block handler method should be located in the same class with original method by default.
        // If you want to use method in other classes, you can set the blockHandlerClass
        // with corresponding Class (Note the method in other classes must be static).
        System.out.println("Oops: " + ex.getClass().getCanonicalName());
    }
}
