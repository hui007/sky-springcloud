/**
 * @(#) NumberTest.java 1.0 2021-01-13
 * Copyright (c) 2021, YUNXI. All rights reserved.
 * YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sky.springcloud.client.java;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author 太阿
 * @since 0.1.0
 */
public class NumberTest {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void testBigdecimal() {
        BigDecimal bd1 = new BigDecimal(80);
        BigDecimal bd2 = new BigDecimal(1.1);
        BigDecimal bd3 = new BigDecimal(0.11);
        BigDecimal bd4 = new BigDecimal(2.222);

        BigDecimal d1 = bd1.divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);
        BigDecimal d2 = bd2.divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);
        BigDecimal d3 = bd3.divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);
        BigDecimal d4 = bd4.divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);

        logger.info("d1:{},d2:{},d3:{},d4:{}", d1, d2, d3, d4);
    }
}
