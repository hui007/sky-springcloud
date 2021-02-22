package com.sky.springcloud.client.service.inf;

import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * hdfs服务类
 *
 * @author 太阿
 * @since 0.1.0
 * @date 2021-02-07
 */
public interface MapReduceService {
	/**
	 * 执行一个MapReduce进程
	 * @param jobName
	 * @param inputFile
	 * @param mapperClass
	 * @param mapOutputKeyClass
	 * @param mapOutputValueClass
	 * @param reducerClass
	 * @param outputKeyClass
	 * @param outputValueClass
	 * @param outputFile
	 */
	public void runMapReduce(String jobName, String inputFile, Class<? extends Mapper> mapperClass, Class<?> mapOutputKeyClass,
							 Class<?> mapOutputValueClass, Class<? extends Reducer> reducerClass, Class<?> outputKeyClass,
							 Class<?> outputValueClass, String outputFile) throws IOException, ClassNotFoundException, InterruptedException;
}