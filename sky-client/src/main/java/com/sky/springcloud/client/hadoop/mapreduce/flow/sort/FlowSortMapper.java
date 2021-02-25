package com.sky.springcloud.client.hadoop.mapreduce.flow.sort;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author 太阿
 * @since 0.1.0
 * @date 2021-02-25
 *
 * Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
 *     KEYIN: 偏移量的 类型
 *     VALUEIN: 一行文档 的类型
 *     KEYOUT:  FlowSortBean的类型
 *     VALUEOUT: 手机号
 *
 */
public class FlowSortMapper extends Mapper<LongWritable, Text, FlowSortBean, Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //1、拆分文本的数据集，得到手机号和想要的数据
        String[] split = value.toString().split("\t");
        String phoneNum = split[0];

        //2、创建FlowBean对象，帮要使用的数据给封装进去
        FlowSortBean flowSortBean = new FlowSortBean();
        flowSortBean.setUpFlow(Integer.parseInt(split[1]));
        flowSortBean.setDownFlow(Integer.parseInt(split[2]));
        flowSortBean.setUpCountFlow(Integer.parseInt(split[3]));
        flowSortBean.setDownCountFlow(Integer.parseInt(split[4]));

        //3、写入到上下文
        context.write(flowSortBean, new Text(phoneNum));
    }
}
