package com.sky.springcloud.client.service.impl;

import com.sky.springcloud.client.service.inf.HDFSService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * hdfs服务实现类
 *
 * @author 太阿
 * @since 0.1.0
 */
@ConditionalOnBean(FileSystem.class)
@Service
@Slf4j
public class HDFSServiceImpl implements HDFSService {

 
	@Autowired
	private FileSystem fileSystem;
	@Autowired
	private Configuration configuration;

	/**
	 * 程序退出之前断开连接
	 * @throws IOException
	 */
	@PreDestroy
	public void close() throws IOException {
		fileSystem.close();
	}



	/**
	 * 将本地小文件合并上传到远程的一个大文件里
	 * @param remotePath
	 * @param localPaths
	 */
	public void mergeFileUpload(String remotePath, String... localPaths) {
		try (
				// 获取hdfs大文件的输出流
				FSDataOutputStream outputStream = fileSystem.create(new Path(remotePath), (short)1);
//				FSDataOutputStream outputStream = fileSystem.append(new Path(remotePath));
				// 获取一个本地文件系统
				LocalFileSystem localFileSystem = FileSystem.getLocal(configuration);
		) {
			for (String localPath : localPaths) {
				FSDataInputStream inputStream = localFileSystem.open(new Path(localPath));
				IOUtils.copy(inputStream, outputStream);
				IOUtils.closeQuietly(inputStream);
			}
			outputStream.flush();
		} catch (IOException e) {
			log.error("mergeFileUpload error", e);
		}
	}

	/**
	 * 将远程小文件合并下载到本地的一个大文件里
	 * @param localPath
	 * @param remotePaths
	 */
	public void mergeFileDownload(String localPath, String... remotePaths) {
		try (
				// 获取一个本地文件系统
				LocalFileSystem localFileSystem = FileSystem.getLocal(configuration);
				// 获取本地大文件的输出流
				FSDataOutputStream outputStream = localFileSystem.create(new Path(localPath));
		) {
			for (String remotePath : remotePaths) {
				FSDataInputStream inputStream = fileSystem.open(new Path(remotePath));
				IOUtils.copy(inputStream, outputStream);
				IOUtils.closeQuietly(inputStream);
			}
			outputStream.flush();
		} catch (IOException e) {
			log.error("mergeFileDownload error", e);
		}
	}


 
}