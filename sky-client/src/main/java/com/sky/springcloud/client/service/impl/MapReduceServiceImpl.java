/**
 * @(#) MapReduceImpl.java 1.0 2021-02-07
 * Copyright (c) 2021, YUNXI. All rights reserved.
 * YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sky.springcloud.client.service.impl;

import com.sky.springcloud.client.service.inf.MapReduceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author 太阿
 * @date 2021-02-07
 * @since 0.1.0
 */
@ConditionalOnBean(FileSystem.class)
@Service
@Slf4j
public class MapReduceServiceImpl implements MapReduceService {
    @Autowired
    private Configuration configuration;

    @Override
    public void runMapReduce(String jobName, String inputFile, Class<? extends Mapper> mapperClass,
                             Class<?> mapOutputKeyClass, Class<?> mapOutputValueClass, Class<? extends Reducer> reducerClass,
                             Class<?> outputKeyClass, Class<?> outputValueClass, Class<? extends Partitioner> partitionerClass, Integer reduceTaskNum, String outputFile, Class<? extends Reducer> combinerClass) throws IOException, ClassNotFoundException, InterruptedException {
        // 定义变量
        Class<TextInputFormat> inputFormatClass = TextInputFormat.class;
        Class<TextOutputFormat> outputFormatClass = TextOutputFormat.class;

        //一、初始化一个Job对象
        Job job = Job.getInstance(configuration, jobName);

        //二、设置Job对象的相关的信息，里面含有8个小步骤
        //1、设置输入的路径，让程序找到源文件的位置
        job.setInputFormatClass(inputFormatClass); // 默认是TextInputFormat.class
        //TextInputFormat.addInputPath(job,new Path("D://input/test1.txt"));  可以写本地地址，也可以写远程hdfs上的地址
//        TextInputFormat.addInputPath(job,new Path("hdfs://192.168.22.128:8020/wordcount.txt"));
        TextInputFormat.addInputPath(job, new Path(inputFile));

        //2、设置Mapper类型，并设置k2 v2
        job.setMapperClass(mapperClass);
        job.setMapOutputKeyClass(mapOutputKeyClass);
        job.setMapOutputValueClass(mapOutputValueClass);

        //3 4 5 6 四个步骤，都是Shuffle阶段，现在使用默认的就可以了
        //3、设置分区
        if (partitionerClass != null) {
            job.setPartitionerClass(partitionerClass);
        }
        //4、排序。体现在自定义key实现类上。see SortBean
        //5、局部合并 Combiner
        if (combinerClass != null) {
            job.setCombinerClass(combinerClass);
        }

        //7、设置Reducer类型，并设置k3 v3
        job.setReducerClass(reducerClass);
        job.setOutputKeyClass(outputKeyClass);
        job.setOutputValueClass(outputValueClass);

        //设置NumReduceTask的个数
        if (partitionerClass != null) {
            job.setNumReduceTasks(reduceTaskNum);
        }

        //8、设置输出的路径，让程序的结果存放到某个地方去
        job.setOutputFormatClass(outputFormatClass); // 默认是TextInputFormat.class
        //TextOutputFormat.setOutputPath(job,new Path("D://word_out")); 可以写本地地址，也可以写远程hdfs上的地址
//        TextOutputFormat.setOutputPath(job,new Path("hdfs://192.168.22.128:8020/word_out0122"));
        TextOutputFormat.setOutputPath(job, new Path(outputFile));

        //三、等待程序完成
        boolean b = job.waitForCompletion(true);
    }
}
