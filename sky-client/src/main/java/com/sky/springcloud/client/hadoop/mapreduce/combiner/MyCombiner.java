package com.sky.springcloud.client.hadoop.mapreduce.combiner;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @author 太阿
 * @since 0.1.0
 * @date 2021-02-22
 *
 * Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
 *     KEYIN: map阶段传递过来的key的类型
 *     VALUEIN:map阶段传递过来的value的类型
 *     KEYOUT: 局部汇总的key的类型，也就是单词的类型
 *     VALUEOUT: 局部汇总的value的类型，也就是次数的类型
 */
public class MyCombiner extends Reducer<Text,LongWritable,Text,LongWritable> {
    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
        //1、定义一个变量
        long count = 0;

        //2、迭代累加
        for (LongWritable value : values) {
            count += value.get();
        }

        //3、写入到上下文中
        context.write(key,new LongWritable(count));
    }
}
