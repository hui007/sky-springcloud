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
import java.util.Random;
import java.util.SplittableRandom;
import java.util.concurrent.ThreadLocalRandom;

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

    @Test
    public void testRandom() {
        // 参考：https://blog.csdn.net/weixin_36480510/article/details/113082819

        // 我们用两个整数，表示结果的随机数的取值范围在min和max之间
        int max = 5;
        int min = 4;
        int randomWithMathRandom = (int) ((Math.random() * (max - min)) + min);

        Random random = new Random();
        int randomWithNextInt = random.nextInt();

        // Java 1.7之后，为我们带来了一种通过ThreadLocalRandom类来生成随机数的更高效的新方法
        int randomWithThreadLocalRandomInARange = ThreadLocalRandom.current().nextInt(min, max);

        // Java 8还给我们带来了一个非常快的生成器——SplittableRandom类。这是一个用于并行计算的生成器。重要的是要知道实例不是线程安全的。
        SplittableRandom splittableRandom = new SplittableRandom();
        int randomWithSplittableRandom = splittableRandom.nextInt(min, max);

    }
}
