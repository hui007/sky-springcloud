package com.sky.springcloud.client.hadoop.mapreduce.flow.sum;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @author 太阿
 * @since 0.1.0
 * @date 2021-02-22
 *
 * Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
 *     KEYIN:map阶段传递过来的手机号的类型   Text类型
 *     VALUEIN: FlowBean
 *     KEYOUT: Text类型
 *     VALUEOUT: FlowBean

| 上行数据包 | upFlow        | int    |
| 下行数据包 | downFlow      | int    |
| 上行流量   | upCountFlow   | int    |
| 下行流量   | downCountFlow | int    |
 */
public class FlowCountReducer extends Reducer<Text,FlowBean,Text,FlowBean> {
    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
        //1、遍历values，将四个字段的对应的各自值进行累加
        Integer upFlow = 0;
        Integer downFlow = 0;
        Integer upCountFlow = 0;
        Integer downCountFlow = 0;
        for (FlowBean value : values) {
            upFlow += value.getUpFlow();
            downFlow += value.getDownFlow();
            upCountFlow += value.getUpCountFlow();
            downCountFlow += value.getDownCountFlow();
        }

        //2、创建FlowBean对象，给这个对象赋值
        FlowBean flowBean = new FlowBean();
        flowBean.setUpFlow(upFlow);
        flowBean.setDownFlow(downFlow);
        flowBean.setUpCountFlow(upCountFlow);
        flowBean.setDownCountFlow(downCountFlow);

        //3、写入到上下文中
        context.write(key,flowBean);
    }
}
