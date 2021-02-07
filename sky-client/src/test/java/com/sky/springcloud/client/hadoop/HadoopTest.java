/**
 * @(#) HadoopTest.java 1.0 2021-01-16
 * Copyright (c) 2021, YUNXI. All rights reserved.
 * YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sky.springcloud.client.hadoop;

import com.sky.springcloud.client.service.inf.HDFSService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
}
