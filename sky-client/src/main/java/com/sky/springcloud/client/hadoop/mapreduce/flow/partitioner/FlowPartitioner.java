package com.sky.springcloud.client.hadoop.mapreduce.flow.partitioner;

import com.sky.springcloud.client.hadoop.mapreduce.flow.sum.FlowBean;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * @author 太阿
 * @since 0.1.0
 * @date 2021-02-25
 *
 * Partitioner<KEY, VALUE>
 *     KEY: 单词的类型  Text类型
 *     VALUE: 次数的类型   LongWritable类型
 */
public class FlowPartitioner extends Partitioner<Text, FlowBean> {
    @Override
    public int getPartition(Text text, FlowBean flowBean, int numPartitions) {
        /**
         * 分区的数量要跟reducerTask的个数一致。 通过"job.setNumReduceTasks(reduceTaskNum)"指定
         */
        String line = text.toString();
        if (line.startsWith("135")) {
            return 0;
        } else if (line.startsWith("136")) {
            return 1;
        } else if (line.startsWith("137")) {
            return 2;
        } else {
            return 3;
        }
    }
}
