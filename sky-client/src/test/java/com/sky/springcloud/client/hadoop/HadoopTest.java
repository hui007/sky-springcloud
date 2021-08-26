/**
 * @(#) HadoopTest.java 1.0 2021-01-16
 * Copyright (c) 2021, YUNXI. All rights reserved.
 * YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sky.springcloud.client.hadoop;

import com.sky.springcloud.client.config.HadoopHDFSConfig;
import com.sky.springcloud.client.hadoop.mapreduce.combiner.MyCombiner;
import com.sky.springcloud.client.hadoop.mapreduce.flow.partitioner.FlowPartitioner;
import com.sky.springcloud.client.hadoop.mapreduce.flow.sort.FlowSortBean;
import com.sky.springcloud.client.hadoop.mapreduce.flow.sort.FlowSortMapper;
import com.sky.springcloud.client.hadoop.mapreduce.flow.sort.FlowSortReducer;
import com.sky.springcloud.client.hadoop.mapreduce.flow.sum.FlowBean;
import com.sky.springcloud.client.hadoop.mapreduce.flow.sum.FlowCountMapper;
import com.sky.springcloud.client.hadoop.mapreduce.flow.sum.FlowCountReducer;
import com.sky.springcloud.client.hadoop.mapreduce.group.*;
import com.sky.springcloud.client.hadoop.mapreduce.partitioner.WordPartitioner;
import com.sky.springcloud.client.hadoop.mapreduce.sort.SortBean;
import com.sky.springcloud.client.hadoop.mapreduce.sort.SortMapper;
import com.sky.springcloud.client.hadoop.mapreduce.sort.SortReducer;
import com.sky.springcloud.client.hadoop.mapreduce.wordcount.WordMapper;
import com.sky.springcloud.client.hadoop.mapreduce.wordcount.WordReducer;
import com.sky.springcloud.client.service.inf.HDFSService;
import com.sky.springcloud.client.service.inf.MapReduceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author 太阿
 * @since 0.1.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest()
@ActiveProfiles("hadoop")
public class HadoopTest {
    @Autowired
    private HDFSService hdfsService;
    @Autowired
    private MapReduceService mapReduceService;
    @Autowired
    private HadoopHDFSConfig hadoopHDFSConfig;

    /**
     * 所有测试方法执行之前执行该方法
     */
    @Before
    public void before() {
        /*
            解决"org.apache.hadoop.security.AccessControlException: Permission denied: user "问题。默认取的本机操作系统的登录用户。
            也可通过"FileSystem fileSystem = FileSystem.get(URI.create("hdfs://hadoop01:9000/") ,conf, hadoopHDFSConfig.getHdfsUser());"解决
         */
        System.setProperty("HADOOP_USER_NAME", hadoopHDFSConfig.getHdfsUser());
    }

    /**
     * 测试本地多个小文件合并成一个大文件后，上传到远程hdfs文件系统
     */
    @Test
    public void testMergeFileUpload() throws Exception {
        String remotePath = "/mergeFileUpload/testMergeFileUpload.txt";
        String[] localPaths = new String[]{"/Users/jianghui/Downloads/testMergeFileUpload1.txt", "/Users/jianghui/Downloads/testMergeFileUpload2.txt", "/Users/jianghui/Downloads/testMergeFileUpload3.txt"};
        hdfsService.mergeFileUpload(remotePath, localPaths);
    }

    /**
     * 测试将远程hdfs文件系统里的多个小文件下载到本地后，合并成一个大文件
     */
    @Test
    public void testMergeFileDownload() {
        String localPath = "/Users/jianghui/Downloads/testMergeFileDownload.txt";
        String[] remotePaths = new String[]{"/mergeFileUpload/testMergeFileUpload.txt", "/startAllinone.sh"};
        hdfsService.mergeFileDownload(localPath, remotePaths);
    }

    /**
     * MapReduced：使用MapReduce计算文本内每个单词的出现次数
     */
    @Test
    public void testMapReduceWordCount() throws InterruptedException, IOException, ClassNotFoundException {
        // 如果要计算本地文件、计算结果要下发到本地，inputFile和outputFile的url需要带file://前缀。如果是远程文件，格式类似hdfs://taieout:8020/x/y/z.txt
        mapReduceService.runMapReduce("wordCount", "file:///Users/jianghui/Downloads/temp/jh.txt", WordMapper.class,
                Text.class, LongWritable.class, WordReducer.class, Text.class, LongWritable.class, null, 0,"file:///Users/jianghui/Downloads/wordCount", null, null);
    }

    /**
     * MapReduced：分区。有几个分区，就有几个结果文件
     */
    @Test
    public void testMapReducePartitioner() throws InterruptedException, IOException, ClassNotFoundException {
        // 如果要计算本地文件、计算结果要下发到本地，inputFile和outputFile的url需要带file://前缀。如果是远程文件，格式类似hdfs://taieout:8020/x/y/z.txt
        mapReduceService.runMapReduce("wordCountPartitioner", "file:///Users/jianghui/Downloads/temp/jh.txt", WordMapper.class,
                Text.class, LongWritable.class, WordReducer.class, Text.class, LongWritable.class, WordPartitioner.class, 2,"file:///Users/jianghui/Downloads/wordCountPartitioner", null, null);
    }

    /**
     * MapReduced：排序。每个结果文件里的key按字母顺序排列。
     */
    @Test
    public void testMapReduceSort() throws InterruptedException, IOException, ClassNotFoundException {
        // 如果要计算本地文件、计算结果要下发到本地，inputFile和outputFile的url需要带file://前缀。如果是远程文件，格式类似hdfs://taieout:8020/x/y/z.txt
        mapReduceService.runMapReduce("wordCountSort", "file:///Users/jianghui/Downloads/temp/jh.txt", SortMapper.class,
                SortBean.class, NullWritable.class, SortReducer.class, SortBean.class, NullWritable.class, null, 0,"file:///Users/jianghui/Downloads/wordCountSort", null, null);
    }

    /**
     * MapReduced：局部合并。在mapper的输出发送给reducer之前，做一个局部合并，将相同key的输出项合并之后传给reducer。
     */
    @Test
    public void testMapReduceCombiner() throws InterruptedException, IOException, ClassNotFoundException {
        // 如果要计算本地文件、计算结果要下发到本地，inputFile和outputFile的url需要带file://前缀。如果是远程文件，格式类似hdfs://taieout:8020/x/y/z.txt
        mapReduceService.runMapReduce("wordCountCombiner", "file:///Users/jianghui/Downloads/temp/jh.txt", WordMapper.class,
                Text.class, LongWritable.class, WordReducer.class, Text.class, LongWritable.class, null, 0,"file:///Users/jianghui/Downloads/wordCountCombiner", MyCombiner.class, null);
    }

    /**
     * MapReduced：分组。求top n
     */
    @Test
    public void testMapReduceGroup() throws InterruptedException, IOException, ClassNotFoundException {
        // 如果要计算本地文件、计算结果要下发到本地，inputFile和outputFile的url需要带file://前缀。如果是远程文件，格式类似hdfs://taieout:8020/x/y/z.txt
        mapReduceService.runMapReduce("group",
                "file:///Users/jianghui/Downloads/temp/nx/order.txt",
                OrderMapper.class, OrderBean.class, Text.class,
                OrderReducer.class, OrderBean.class, NullWritable.class,
                OrderPartition.class, 1,
                "file:///Users/jianghui/Downloads/group", null, OrderGroup.class);
    }

    /**
     * MapReduced：实战。计算手机流量总和。
     */
    @Test
    public void testMapReduceFlowSum() throws InterruptedException, IOException, ClassNotFoundException {
        // 如果要计算本地文件、计算结果要下发到本地，inputFile和outputFile的url需要带file://前缀。如果是远程文件，格式类似hdfs://taieout:8020/x/y/z.txt
        mapReduceService.runMapReduce("flowSum", "file:///Users/jianghui/Downloads/temp/nx/正式课程资料_05-分布式计算模型Mapreduce实践与原理剖析（二）_05课后资料_测试数据_input_flow.log",
                FlowCountMapper.class,
                Text.class, FlowBean.class, FlowCountReducer.class, Text.class, FlowBean.class, null, 0,
                "file:///Users/jianghui/Downloads/flowSum", null, null);
    }

    /**
     * MapReduced：实战。在testMapReduceFlowSum计算结果文件基础上，按照upFLow流量倒排。
     */
    @Test
    public void testMapReduceFlowSort() throws InterruptedException, IOException, ClassNotFoundException {
        // 如果要计算本地文件、计算结果要下发到本地，inputFile和outputFile的url需要带file://前缀。如果是远程文件，格式类似hdfs://taieout:8020/x/y/z.txt
        mapReduceService.runMapReduce("flowSort", "file:///Users/jianghui/Downloads/temp/nx/flowsum.log",
                FlowSortMapper.class,
                FlowSortBean.class, Text.class, FlowSortReducer.class, Text.class, FlowSortBean.class, null, 0,
                "file:///Users/jianghui/Downloads/flowSort", null, null);
    }

    /**
     * MapReduced：实战。按照手机号分区求和。136的在一个结果文件中，135的在一个结果文件中，其他的手机号类似。
     */
    @Test
    public void testMapReduceFlowPartitioner() throws InterruptedException, IOException, ClassNotFoundException {
        // 如果要计算本地文件、计算结果要下发到本地，inputFile和outputFile的url需要带file://前缀。如果是远程文件，格式类似hdfs://taieout:8020/x/y/z.txt
        mapReduceService.runMapReduce("flowPartitioner", "file:///Users/jianghui/Downloads/temp/nx/正式课程资料_05-分布式计算模型Mapreduce实践与原理剖析（二）_05课后资料_测试数据_input_flow.log",
                FlowCountMapper.class,
                Text.class, FlowBean.class, FlowCountReducer.class, Text.class, FlowBean.class, FlowPartitioner.class, 4,
                "file:///Users/jianghui/Downloads/flowPartitioner", null, null);
    }
}
