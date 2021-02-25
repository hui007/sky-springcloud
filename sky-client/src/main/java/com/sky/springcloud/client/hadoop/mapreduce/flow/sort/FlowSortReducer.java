package com.sky.springcloud.client.hadoop.mapreduce.flow.sort;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @author 太阿
 * @date 2021-02-25
 * <p>
 * Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
 * KEYIN:FlowSortBean
 * VALUEIN: map阶段传递过来的手机号的类型   Text类型
 * KEYOUT: Text类型
 * VALUEOUT: FlowSortBean
 * <p>
 * | 上行数据包 | upFlow        | int    |
 * | 下行数据包 | downFlow      | int    |
 * | 上行流量   | upCountFlow   | int    |
 * | 下行流量   | downCountFlow | int    |
 * @since 0.1.0
 */
public class FlowSortReducer extends Reducer<FlowSortBean, Text, Text, FlowSortBean> {
    @Override
    protected void reduce(FlowSortBean key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        //1、遍历values，将k3、v3写入上下文
        for (Text value : values) {
            context.write(value, key);
        }
    }
}
