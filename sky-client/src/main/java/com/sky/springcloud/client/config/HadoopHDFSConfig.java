package com.sky.springcloud.client.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * hdfs配置
 *
 * @author 太阿
 * @since 0.1.0
 */
@org.springframework.context.annotation.Configuration
@ConditionalOnProperty(name="hadoop.hdfs.default-fs")
@Slf4j
public class HadoopHDFSConfig {
	@Value("${hadoop.hdfs.user}")
	private String hdfsUser;
	@Value("${hadoop.hdfs.default-fs}")
	private String hdfsDefaultFS;

	/**
	 * hadoop hdfs 配置参数对象
	 * @return
	 */
	@Bean
	public org.apache.hadoop.conf.Configuration  getConfiguration(){
		// 1、创建Configuration对象
		Configuration configuration = new Configuration();
		// 2、设置文件系统类型
		configuration.set("fs.defaultFS", hdfsDefaultFS);
		configuration.set("dfs.client.use.datanode.hostname", "true");
		return configuration;
	}
	/**
	 * hadoop filesystem 文件系统
	 * @return
	 */
	@Bean
	public FileSystem getFileSystem(){
		FileSystem fileSystem = null;
		try {
			fileSystem = FileSystem.get(new URI(hdfsDefaultFS), getConfiguration(), hdfsUser);
		} catch (IOException | InterruptedException | URISyntaxException e) {
			log.error("instance fileSystem error",e.getMessage());
		}
		return fileSystem;
	}

	//方式一：获取FileSystem
	public void getFileSystem1() throws IOException {
		//1、创建Configuration对象
		Configuration conf = new Configuration();

		//2、设置文件系统类型
		conf.set("fs.defaultFS","hdfs://hadoop0:8020");

		//3、获取指定文件系统
		FileSystem fileSystem = FileSystem.get(conf);

		//4、打印输出测试
		System.out.println(fileSystem);
	}

	/**
	 * 方式二：set方式+通过newInstance
	 * @throws IOException
	 */
	public void getFileSystem2() throws IOException {
		//1:创建Configuration对象
		Configuration conf = new Configuration();

		//2:设置文件系统类型
		conf.set("fs.defaultFS", "hdfs://hadoop0:8020");

		//3:获取指定文件系统
		FileSystem fileSystem = FileSystem.newInstance(conf);

		//4:输出测试
		System.out.println(fileSystem);
	}

	/**
	 * 方式三：new URI+get
	 * @throws Exception
	 */
	public void getFileSystem3() throws Exception{
		FileSystem fileSystem = FileSystem.get(new URI("hdfs://hadoop0:8020"), new Configuration());
		System.out.println("fileSystem:"+fileSystem);
	}
	/**
	 * 方式四：newInstance+get
	 * @throws Exception
	 */
	public void getFileSystem4() throws Exception{
		FileSystem fileSystem = FileSystem.newInstance(new URI("hdfs://hadoop0:8020"), new Configuration());
		System.out.println("fileSystem:"+fileSystem);
	}


}