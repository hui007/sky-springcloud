package com.sky.springcloud.client.util;

import com.sky.springcloud.client.controller.UserHystrixController;
import com.sky.springcloud.client.service.UserHystrixService;

/**
 * <b>package-info不是平常类，其作用有三个:</b><br>
 * 1、为标注在包上Annotation提供便利；<br>
 * 2、声明包的私有类和常量：只能在包内被使用；<br>
 * 3、提供包的整体注释说明。<br>
 *
 */

class BeanUtilDesc{
    /**
     * bean和map相互转换：{@link UserHystrixService#getUserFuture(java.lang.Long)}<br>
     */
    public void desc(){
        System.out.println("noThing");
    }
}

class ThreadUtilDesc{
    /**
     * 线程休眠：{@link UserHystrixController#testCollapser()}<br>
     */
    public void desc(){
        System.out.println("noThing");
    }
}

class TimeUtilDesc{
    /**
     * 日期操作大全：https://mp.weixin.qq.com/s/Y5GPjsezLNWzvz7Ke1x4Ew <br>
     * 线程休眠：{@link TimeUtil}<br>
     */
    public void desc(){
        System.out.println("noThing");
    }
}

class CollectionsUtilDesc{
    /**
     * 集合转字符串：{@link UserHystrixService#getUserByIds(java.util.List)}<br>
     */
    public void desc(){
        System.out.println("noThing");
    }
}

class NumberUtilDesc{
    /**
     * 原码/反码/补码：计算机中以补码形式表示数字<br>
     *  原码：正数是大小直接转为二进制；负数是正数的原码最高位为1<br>
     *  反码：正数的反码与原码相同；负数的反码为对该数的原码除符号位外各位取反<br>
     *  补码：正数的补码与原码相同；负数的补码为对该数的原码除符号位外各位取反，然后在最后一位加1。<br><br>
     * 转为二进制：Integer.toBinaryString(-4) ： 11111111111111111111111111111100<br>
     * 带符号右移：-4>>1 = -2<br>
     * 无符号右移：-4>>>1 = 2147483646 ： 1111111111111111111111111111110（31位，移出右边的被丢弃）<br>
     */
    public void desc(){
        System.out.println("noThing");
    }
}