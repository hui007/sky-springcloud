/**
 * @(#) LoggingTest.java 1.0 2021-02-08
 * Copyright (c) 2021, YUNXI. All rights reserved.
 * YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sky.springcloud.client.java;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 太阿
 * @since 0.1.0
 * @date 2021-02-08
 */
//@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest()
public class LoggingTest {
    Logger logger = LoggerFactory.getLogger("com.sky.springcloud.client.java.LoggingTest");

    /**
     * Logback can report information about its internal state using a built-in status system
     * 官网教程里会打印出"INFO in ch.qos.logback.classic.LoggerContext"，事实是我自己测试时，并没有出现这个
     * @throws Exception
     */
    @Test
    public void testLogbackInternalState() throws Exception {
        logger.debug("Hello world.");

        // print internal state
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(lc);
    }
}
