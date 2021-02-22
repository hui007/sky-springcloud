package com.sky.springcloud.client.hadoop.mapreduce.sort;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author 太阿
 * @since 0.1.0
 * @date 2021-02-22
 *
 * Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
 *     KEYIN: 一行文本的偏移量 的类型
 *     VALUEIN: 一行文本 的类型
 *     KEYOUT: SortBean
 *     VALUEOUT: NullWritable
 */
public class SortMapper extends Mapper<LongWritable, Text, SortBean, NullWritable>{
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //1、拆分数据，一行文本做一个拆分
        String[] split = value.toString().split(" ");

        //2、将对应的值给传入到MySortBean的实例对象中
        SortBean mySortBean = new SortBean();
        mySortBean.setWord(split[0]);
//        mySortBean.setNum(Integer.parseInt(split[1]));
        mySortBean.setNum(0); // 因为文件内容问题，这里模拟下，固定给0

        //3、写入到上下文
        context.write(mySortBean,NullWritable.get());
    }
}
