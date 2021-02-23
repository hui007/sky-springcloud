package com.sky.springcloud.client.hadoop.mapreduce.flow.sum;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author 太阿
 * @since 0.1.0
 * @date 2021-02-22
 *
 * Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
 *     KEYIN: 偏移量的 类型
 *     VALUEIN: 一行文档 的类型
 *     KEYOUT: 手机号 的类型
 *     VALUEOUT: FlowBean
 *

示例数据：
1363157995033 	15920133257	5C-0E-8B-C7-BA-20:CMCC	120.197.40.4	sug.so.360.cn	信息安全	20			20		    3156	  2936	  200
时间戳			手机号		基站编号				IP				URL				URL类型		上行数据包  下行数据包	上行流量 下行流量 响应

 */
public class FlowCountMapper extends Mapper<LongWritable, Text, Text, FlowBean> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //1、拆分文本的数据集，得到手机号和想要的数据
        String[] split = value.toString().split("\t");
        String phoneNum = split[1];

        //2、创建FlowBean对象，帮要使用的数据给封装进去
        FlowBean flowBean = new FlowBean();
        flowBean.setUpFlow(Integer.parseInt(split[6]));
        flowBean.setDownFlow(Integer.parseInt(split[7]));
        flowBean.setUpCountFlow(Integer.parseInt(split[8]));
        flowBean.setDownCountFlow(Integer.parseInt(split[9]));

        //3、写入到上下文
        context.write(new Text(phoneNum),flowBean);
    }
}
