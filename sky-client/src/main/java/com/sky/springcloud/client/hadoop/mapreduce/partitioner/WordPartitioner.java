package com.sky.springcloud.client.hadoop.mapreduce.partitioner;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * @author 太阿
 * @since 0.1.0
 * @date 2021-02-22
 *
 * Partitioner<KEY, VALUE>
 *     KEY: 单词的类型  Text类型
 *     VALUE: 次数的类型   LongWritable类型
 */
public class WordPartitioner extends Partitioner<Text, LongWritable> {
    @Override
    public int getPartition(Text text, LongWritable longWritable, int numPartitions) {
        //分区
        //需求：根据单词的长度给单词出现的次数的结果存储到不同文件中，以便于在快速查询
        //单词长度>=5的在一个结果文件中，<5的在一个结果文件中

        /**
         * 分区的数量要跟reducerTask的个数一致。通过"job.setNumReduceTasks(reduceTaskNum)"指定
         */
        if (text.toString().length() >= 5){
            return 0;
        }else {
            return 1;
        }
    }
}
