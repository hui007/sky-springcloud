package com.sky.springcloud.client.controller;

import com.sky.springcloud.client.config.HadoopHDFSConfig;
import com.sky.springcloud.client.service.inf.HDFSService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
 
@Controller
@RequestMapping("/api/hadoop/hdfs")
@Api(value = "HDFS Controlle", tags = "HDFS 操作服务")
@ConditionalOnBean(HadoopHDFSConfig.class)
public class HDFSController {
	// 日志记录
	private static final Logger logger = LoggerFactory.getLogger(HDFSController.class);	
	@Autowired
	private HDFSService service;
	
	/**
	 * 创建的文件夹权限不够，需要设置权限问题
	 * @param entity
	 * @return
	 */
	
//	@ApiOperation(httpMethod = "POST", value = "创建文件夹")
//	@RequestMapping(value = "/mkdirFolder", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public JreportResponse mkdirFolder(
//			@RequestBody @ApiParam(name = "JSON对象", value = "json格式对象", required = true) JSONObject entity) {
//		boolean target = service.mkdirFolder(entity.getString("path"));
//		return JreportResponse.ok(target);
//
//	}
//
//	@ApiOperation(httpMethod = "POST", value = "判断文件夹是否存在")
//	@RequestMapping(value = "/existFile", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public JreportResponse existFile(
//			@RequestBody @ApiParam(name = "JSON对象", value = "json格式对象", required = true) JSONObject entity) {
//		boolean target = service.existFile(entity.getString("path"));
//		return JreportResponse.ok(target);
//	}
//
//	@ApiOperation(httpMethod = "POST", value = "读取目录")
//	@RequestMapping(value = "/readCatalog", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public JreportResponse readCatalog(
//			@RequestBody @ApiParam(name = "JSON对象", value = "json格式对象", required = true) JSONObject entity) {
//		 List<Map<String, Object>> list = service.readCatalog(entity.getString("path"));
//		return JreportResponse.ok(list);
//	}
//
//	@ApiOperation(httpMethod = "POST", value = "新建文件")
//	@RequestMapping(value = "/createFile", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public JreportResponse createFile(
//			@RequestBody @ApiParam(name = "JSON对象", value = "json格式对象", required = true) JSONObject entity) {
//		FileInputStream inputStream = null;
//		MultipartFile file = null;
//		try {
//			inputStream = new FileInputStream("C:\\data\\words.txt");
//			file = new MockMultipartFile("test.txt", inputStream);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			logger.error(e.getMessage());
//		}finally{
//			try {
//				inputStream.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				logger.error(e.getMessage());
//			}
//		}
//		service.createFile(entity.getString("path"),file);
//		return JreportResponse.ok();
//	}
//
//	@ApiOperation(httpMethod = "POST", value = "读取文件内容")
//	@RequestMapping(value = "/readFileContent", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public JreportResponse readFileContent(
//			@RequestBody @ApiParam(name = "JSON对象", value = "json格式对象", required = true) JSONObject entity) {
//		String content = service.readFileContent(entity.getString("path"));
//		return JreportResponse.ok(content);
//	}
//
//	@ApiOperation(httpMethod = "POST", value = "文件列表")
//	@RequestMapping(value = "/listFile", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public JreportResponse listFile(
//			@RequestBody @ApiParam(name = "JSON对象", value = "json格式对象", required = true) JSONObject entity) {
//		List<Map<String, String>> list = service.listFile(entity.getString("path"));
//		return JreportResponse.ok(list);
//	}
//
//	@ApiOperation(httpMethod = "POST", value = "文件重命名")
//	@RequestMapping(value = "/renameFile", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public JreportResponse renameFile(
//			@RequestBody @ApiParam(name = "JSON对象", value = "json格式对象", required = true) JSONObject entity) {
//		boolean target = service.renameFile(entity.getString("oldName"),entity.getString("newName"));
//		return JreportResponse.ok(target);
//	}
//
//	/**
//	 * 指定文件位删除成功，需要寻找原因
//	 * @param entity
//	 * @return
//	 */
//	@ApiOperation(httpMethod = "POST", value = "文件删除")
//	@RequestMapping(value = "/deleteFile", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public JreportResponse deleteFile(
//			@RequestBody @ApiParam(name = "JSON对象", value = "json格式对象", required = true) JSONObject entity) {
//		boolean target = service.deleteFile(entity.getString("path"));
//		return JreportResponse.ok(target);
//	}
//
//
//	@ApiOperation(httpMethod = "POST", value = "文件拷贝")
//	@RequestMapping(value = "/uploadFile", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public JreportResponse uploadFile(
//			@RequestBody @ApiParam(name = "JSON对象", value = "json格式对象", required = true) JSONObject entity) {
//		service.uploadFile(entity.getString("path"), entity.getString("uploadPath"));
//		return JreportResponse.ok();
//	}
	
	
	
	
 
}