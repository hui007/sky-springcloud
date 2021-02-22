/**
 * @(#) HadoopTest.java 1.0 2021-01-16
 * Copyright (c) 2021, YUNXI. All rights reserved.
 * YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sky.springcloud.client.hadoop;

import com.sky.springcloud.client.config.HadoopHDFSConfig;
import com.sky.springcloud.client.hadoop.mapreduce.WordMapper;
import com.sky.springcloud.client.hadoop.mapreduce.WordReducer;
import com.sky.springcloud.client.service.inf.HDFSService;
import com.sky.springcloud.client.service.inf.MapReduceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
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
     * 使用MapReduce计算文本内每个单词的出现次数
     */
    @Test
    public void testMapReduceWordCount() throws InterruptedException, IOException, ClassNotFoundException {
        /*
            解决"org.apache.hadoop.security.AccessControlException: Permission denied: user "问题。默认取的本机操作系统的登录用户。
            也可通过"FileSystem fileSystem = FileSystem.get(URI.create("hdfs://hadoop01:9000/") ,conf, hadoopHDFSConfig.getHdfsUser());"解决
         */
        System.setProperty("HADOOP_USER_NAME", hadoopHDFSConfig.getHdfsUser());

        // 如果要计算本地文件、计算结果要下发到本地，inputFile和outputFile的url需要带file://前缀。如果是远程文件，格式类似hdfs://193.112.47.33:8020/x/y/z.txt
        mapReduceService.runMapReduce("wordCount", "file:///Users/jianghui/Downloads/temp/jh.txt", WordMapper.class,
                Text.class, LongWritable.class, WordReducer.class, Text.class, LongWritable.class, "file:///Users/jianghui/Downloads/wordCount");
    }
}
