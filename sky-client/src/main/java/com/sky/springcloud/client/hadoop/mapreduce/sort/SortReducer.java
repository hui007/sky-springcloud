package com.sky.springcloud.client.hadoop.mapreduce.sort;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @author 太阿
 * @since 0.1.0
 * @date 2021-02-22
 *
 * Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
 *     KEYIN: SortBean  map阶段输出的key的类型
 *     VALUEIN: NullWritable map阶段输出的value的类型
 *     KEYOUT: SortBean
 *     VALUEOUT: NullWritable
 */
public class SortReducer extends Reducer<SortBean,NullWritable, SortBean,NullWritable>{
    @Override
    protected void reduce(SortBean key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        //将map阶段的结果拿过来做一个汇总，写出到文件中
        context.write(key,NullWritable.get());
    }
}
