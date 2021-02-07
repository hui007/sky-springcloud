package com.sky.springcloud.client.service.inf;

/**
 * hdfs服务类
 *
 * @author 太阿
 * @since 0.1.0
 */
public interface HDFSService {

	/**
	 * 将本地小文件合并上传到远程的一个大文件里
	 * @param remotePath
	 * @param localPaths
	 */
	public void mergeFileUpload(String remotePath, String... localPaths);

	/**
	 * 将远程小文件合并下载到本地的一个大文件里
	 * @param localPath
	 * @param remotePaths
	 */
	public void mergeFileDownload(String localPath, String... remotePaths);
}