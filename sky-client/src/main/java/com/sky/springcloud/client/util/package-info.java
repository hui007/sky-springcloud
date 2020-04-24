package com.sky.springcloud.client.util;

import com.sky.springcloud.client.controller.UserHystrixController;
import com.sky.springcloud.client.service.UserHystrixService;

/**
 * <b>package-info不是平常类，其作用有三个:</b><br>
 * 1、为标注在包上Annotation提供便利；<br>
 * 2、声明包的私有类和常量；<br>
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

class CollectionsUtilDesc{
    /**
     * 集合转字符串：{@link UserHystrixService#getUserByIds(java.util.List)}<br>
     */
    public void desc(){
        System.out.println("noThing");
    }
}