package com.sky.springcloud.client.hadoop.mapreduce.group;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author 太阿
 * @since 0.1.0
 * @date 2021-02-23
 *
 * Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
 *     KEYIN：偏移量
 *     VALUEIN：一行文本
 *     KEYOUT：k2 OrderBean
 *     VALUEOUT：v2 文本
 */
public class OrderMapper extends Mapper<LongWritable,Text,OrderBean,Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //1、拆分行文本数据，得到订单的id和订单的金额
        String[] split = value.toString().split("\t");

        //2、封装OrderBean实体类
        OrderBean orderBean = new OrderBean();
        orderBean.setOrderId(split[0]);
        orderBean.setPrice(Double.parseDouble(split[2]));

        //3、写入上下文
        context.write(orderBean,value);
    }
}
