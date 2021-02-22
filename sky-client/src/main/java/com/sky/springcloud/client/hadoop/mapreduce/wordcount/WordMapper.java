package com.sky.springcloud.client.hadoop.mapreduce.wordcount;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author 太阿
 * @since 0.1.0
 * @date 2021-02-07
 *
 * Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
 *     KEYIN:   是指框架读取到的数据的key的类型，在默认的情况下，读取到的key就是一行数据相对于整个文本开头的偏移量 。key的类型可不可以是Long？LongWritable
 *     VALUEIN: 是指框架读取到的数据的value的类型，在默认的情况下，读取到的value就是一行数据。 value的类型可不可以是String？Text
 *     KEYOUT:  是指用户自定义的业务逻辑方法返回的数据中的key的类型，由用户根据业务逻辑自己决定的，在我们的WordCount程序中，这个key就是单词。这个key的类型可不可以是String？Text
 *     VALUEOUT:    是指用户自定义的业务逻辑方法返回的数据中的value的类型，由用于根据业余逻辑自己决定的，在我们的WordCount程序中，这个value就是次数。这个value的类型可不可以是Long？LongWritable
 *
 *     但是，String、Long、等等 是jdk里面的数据类型，在序列化的时候，效率低
 *     hadoop为了提高效率，自定义一套自己的序列化的类型
 *     在hadoop程序中，如果要进行序列化（写磁盘、网络传输等等），一定要使用hadoop实现的序列化的数据类型
 *
 *     Long ——》 LongWritable
 *     String ——》 Text
 *     Integer ——》 IntWritable
 *     Null ——》 NullWritable
 */
public class WordMapper extends Mapper<LongWritable, Text, Text, LongWritable>{
    /**
     *
     * @param key  就是偏移量
     * @param value  一行文本数据
     * @param context  上下文
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //1、单词的切分
        String[] words = value.toString().split(" ");

        //2、计数一次，帮单词转换成类似于<hello，1>这样的key-value的键值对
        for (String word : words) {
            //3、写入到上下文
            context.write(new Text(word),new LongWritable(1));
        }
    }
}
