package com.sky.springcloud.client.hadoop.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * hdfs基本操作
 *
 * @author 太阿
 * @since 0.1.0
 */
@Slf4j
public class HdfsUtil {
    // 全局变量定义
    private static final int bufferSize = 1024 * 1024 * 64;

    @Autowired
    private FileSystem fs;

    //方式一：获取FileSystem
    public static void getFileSystem1() throws IOException {
        //1、创建Configuration对象
        Configuration conf = new Configuration();

        //2、设置文件系统类型
        conf.set("fs.defaultFS","hdfs://197.122.68.42:8020");

        //3、获取指定文件系统
        FileSystem fileSystem = FileSystem.get(conf);

        //4、打印输出测试
        System.out.println(fileSystem);
    }

    /**
     * 方式二：set方式+通过newInstance
     * @throws IOException
     */
    public static void getFileSystem2() throws IOException {
        //1:创建Configuration对象
        Configuration conf = new Configuration();

        //2:设置文件系统类型
        conf.set("fs.defaultFS", "hdfs://197.122.68.42:8020");

        //3:获取指定文件系统
        FileSystem fileSystem = FileSystem.newInstance(conf);

        //4:输出测试
        System.out.println(fileSystem);
    }

    /**
     * 方式三：new URI+get
     * @throws Exception
     */
    public static void getFileSystem3() throws Exception{
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://197.122.68.42:8020"), new Configuration());
        System.out.println("fileSystem:"+fileSystem);
    }
    /**
     * 方式四：newInstance+get
     * @throws Exception
     */
    public static void getFileSystem4() throws Exception{
        FileSystem fileSystem = FileSystem.newInstance(new URI("hdfs://197.122.68.42:8020"), new Configuration());
        System.out.println("fileSystem:"+fileSystem);
    }

    //文件的遍历
    public static void listFiles() throws URISyntaxException, IOException, InterruptedException {
        //1、获取FileSystem实例
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://197.122.68.42:8020"), new Configuration(),"root");

        //2、调用一个方法
        RemoteIterator<LocatedFileStatus> iterator = fileSystem.listFiles(new Path("/"), true);

        //3、遍历迭代
        while (iterator.hasNext()){
            LocatedFileStatus fileStatus = iterator.next();
            //4、打印输出
            System.out.println(fileStatus.getPath() + "===========" + fileStatus.getPath().getName());
        }
    }

    //创建文件夹
    public static void mkdirs() throws URISyntaxException, IOException, InterruptedException {
        //1、获取FileSystem实例
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://197.122.68.42:8020"), new Configuration(), "root");

        //2、创建文件夹
        fileSystem.mkdirs(new Path("/aa/bb/cc"));

        //3、关闭FileSystem
        fileSystem.close();
    }

    //上传文件
    public static void uploadFile() throws URISyntaxException, IOException, InterruptedException {
        //1、获取FileSystem实例
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://197.122.68.42:8020"), new Configuration(), "root");

        //2、上传文件
        fileSystem.copyFromLocalFile(new Path("D://input/test1.txt"),new Path("/aa/bb/cc"));

        //3、关闭实例
        fileSystem.close();
    }

    //下载文件
    public static void downFile() throws URISyntaxException, IOException, InterruptedException {
        //1、获取FileSystem实例
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://197.122.68.42:8020"), new Configuration(), "root");

        //2、获取hdfs的输入流
        FSDataInputStream inputStream = fileSystem.open(new Path("/aa/bb/cc/test1.txt"));

        //3、获取本地路径的输出流
        FileOutputStream outputStream = new FileOutputStream("D://test1_down.txt");

        //4、文件的拷贝
        IOUtils.copy(inputStream,outputStream);

        //5、关闭流
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
        fileSystem.close();
    }


    /**
     * 获取文件系统
     * @param hdfsUri  nameNode地址 如"hdfs://10.10.1.142:9000"
     * @return
     */
    public static FileSystem getFileSystem(String hdfsUri) {
        //读取配置文件
        Configuration conf = new Configuration();
        // 文件系统
        FileSystem fs = null;
        if(StringUtils.isBlank(hdfsUri)){
            // 返回默认文件系统  如果在 Hadoop集群下运行，使用此种方法可直接获取默认文件系统
            try {
                fs = FileSystem.get(conf);
            } catch (IOException e) {
                log.error("", e);
            }
        }else{
            // 返回指定的文件系统,如果在本地测试，需要使用此种方法获取文件系统
            try {
                URI uri = new URI(hdfsUri.trim());
                fs = FileSystem.get(uri,conf);
            } catch (Exception e) {
                log.error("", e);
            }
        }
        return fs;
    }

    /**
     * 创建文件目录
     *
     * @param hdfsUri
     * @param path
     */
    public static void mkdir(String hdfsUri, String path) {
        try {
            // 获取文件系统
            FileSystem fs = getFileSystem(hdfsUri);
            if(StringUtils.isNotBlank(hdfsUri)){
                path = hdfsUri + path;
            }
            // 创建目录
            fs.mkdirs(new Path(path));
            //释放资源
            fs.close();
        } catch (IllegalArgumentException | IOException e) {
            log.error("", e);
        }
    }

    /**
     * 删除文件或者文件目录
     *
     * @param path
     */
    public static void rmdir(String hdfsUri,String path) {
        try {
            // 返回FileSystem对象
            FileSystem fs = getFileSystem(hdfsUri);
            if(StringUtils.isNotBlank(hdfsUri)){
                path = hdfsUri + path;
            }
            // 删除文件或者文件目录  delete(Path f) 此方法已经弃用
            fs.delete(new Path(path),true);
            // 释放资源
            fs.close();
        } catch (IllegalArgumentException | IOException e) {
            log.error("", e);
        }
    }

    /**
     * 根据filter获取目录下的文件
     *
     * @param path
     * @param pathFilter
     * @return String[]
     */
    public static String[] listFile(String hdfsUri, String path,PathFilter pathFilter) {
        String[] files = new String[0];
        try {
            // 返回FileSystem对象
            FileSystem fs = getFileSystem(hdfsUri);

            if(StringUtils.isNotBlank(hdfsUri)){
                path = hdfsUri + path;
            }

            FileStatus[] status;
            if(pathFilter != null){
                // 根据filter列出目录内容
                status = fs.listStatus(new Path(path),pathFilter);
            }else{
                // 列出目录内容
                status = fs.listStatus(new Path(path));
            }
            // 获取目录下的所有文件路径
            Path[] listedPaths = FileUtil.stat2Paths(status);
            // 转换String[]
            if (listedPaths != null && listedPaths.length > 0){
                files = new String[listedPaths.length];
                for (int i = 0; i < files.length; i++){
                    files[i] = listedPaths[i].toString();
                }
            }
            // 释放资源
            fs.close();
        } catch (IllegalArgumentException | IOException e) {
            log.error("", e);
        }
        return files;
    }

    /**
     * 文件上传至 HDFS
     * @param hdfsUri
     * @param delSrc       指是否删除源文件，true为删除，默认为false
     * @param overwrite
     * @param srcFile      源文件，上传文件路径
     * @param destPath     hdfs的目的路径
     */
    public static void copyFileToHDFS(String hdfsUri,boolean delSrc, boolean overwrite,String srcFile,String destPath) {
        // 源文件路径是Linux下的路径，如果在 windows 下测试，需要改写为Windows下的路径，比如D://hadoop/djt/weibo.txt
        Path srcPath = new Path(srcFile);

        // 目的路径
        if(StringUtils.isNotBlank(hdfsUri)){
            destPath = hdfsUri + destPath;
        }
        Path dstPath = new Path(destPath);
        // 实现文件上传
        try {
            // 获取FileSystem对象
            FileSystem fs = getFileSystem(hdfsUri);
            fs.copyFromLocalFile(srcPath, dstPath);
            fs.copyFromLocalFile(delSrc,overwrite,srcPath, dstPath);
            //释放资源
            fs.close();
        } catch (IOException e) {
            log.error("", e);
        }
    }

    /**
     * 从 HDFS 下载文件
     *
     * @param srcFile
     * @param destPath 文件下载后,存放地址
     */
    public static void getFile(String hdfsUri, String srcFile,String destPath) {
        // 源文件路径
        if(StringUtils.isNotBlank(hdfsUri)){
            srcFile = hdfsUri + srcFile;
        }
        Path srcPath = new Path(srcFile);
        Path dstPath = new Path(destPath);
        try {
            // 获取FileSystem对象
            FileSystem fs = getFileSystem(hdfsUri);
            // 下载hdfs上的文件
            fs.copyToLocalFile(srcPath, dstPath);
            // 释放资源
            fs.close();
        } catch (IOException e) {
            log.error("", e);
        }
    }

    /**
     * 获取 HDFS 集群节点信息
     *
     * @return DatanodeInfo[]
     */
    public static DatanodeInfo[] getHDFSNodes(String hdfsUri) {
        // 获取所有节点
        DatanodeInfo[] dataNodeStats = new DatanodeInfo[0];
        try {
            // 返回FileSystem对象
            FileSystem fs = getFileSystem(hdfsUri);
            // 获取分布式文件系统
            DistributedFileSystem hdfs = (DistributedFileSystem)fs;
            dataNodeStats = hdfs.getDataNodeStats();
        } catch (IOException e) {
            log.error("", e);
        }
        return dataNodeStats;
    }

    /**
     * 查找某个文件在 HDFS集群的位置
     *
     * @param filePath
     * @return BlockLocation[]
     */
    public static BlockLocation[] getFileBlockLocations(String hdfsUri, String filePath) {
        // 文件路径
        if(StringUtils.isNotBlank(hdfsUri)){
            filePath = hdfsUri + filePath;
        }
        Path path = new Path(filePath);

        // 文件块位置列表
        BlockLocation[] blkLocations = new BlockLocation[0];
        try {
            // 返回FileSystem对象
            FileSystem fs = getFileSystem(hdfsUri);
            // 获取文件目录
            FileStatus filestatus = fs.getFileStatus(path);
            //获取文件块位置列表
            blkLocations = fs.getFileBlockLocations(filestatus, 0, filestatus.getLen());
        } catch (IOException e) {
            log.error("", e);
        }
        return blkLocations;
    }


    /**
     * 判断目录是否存在
     * @param hdfsUri
     * @param filePath
     * @param create
     * @return
     */
    public boolean existDir(String hdfsUri,String filePath, boolean create){
        boolean flag = false;

        if (StringUtils.isEmpty(filePath)){
            return flag;
        }
        try{
            Path path = new Path(filePath);
            // FileSystem对象
            FileSystem fs = getFileSystem(hdfsUri);
            if (create){
                if (!fs.exists(path)){
                    fs.mkdirs(path);
                }
            }
            if (fs.isDirectory(path)){
                flag = true;
            }
        }catch (Exception e){
            log.error("", e);
        }

        return flag;
    }

    /**
     * 合并上传小文件
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public void mergeFileUpload() throws URISyntaxException, IOException, InterruptedException {
        //1、获取FileSystem
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://hadoop0:8020"), new Configuration(), "root");

        //2、获取hdfs大文件的输出流
        FSDataOutputStream outputStream = fileSystem.create(new Path("/test_big.txt"));

        //3、获取一个本地文件系统
        LocalFileSystem localFileSystem = FileSystem.getLocal(new Configuration());

        //4、获取本地文件夹下面的所有的小文件
        FileStatus[] fileStatuses = localFileSystem.listStatus(new Path("D://input2"));

        //5、遍历
        for (FileStatus fileStatus : fileStatuses) {
            FSDataInputStream inputStream = localFileSystem.open(fileStatus.getPath());
            //6、将小文件给复制到大文件当中
            IOUtils.copy(inputStream,outputStream);
            IOUtils.closeQuietly(inputStream);
        }
        //7、关闭流
        IOUtils.closeQuietly(outputStream);
        localFileSystem.close();
        fileSystem.close();
    }

    /**
     * 创建HDFS文件夹
     *
     * @param path
     * @return
     * @throws Exception
     */
    public boolean mkdir(String path) throws Exception {
        if (StringUtils.isEmpty(path) || null == fs) {
            return false;
        }
        Path hpath = new Path(path);
        if (fs.exists(hpath)) {
            log.info("file exist: " + path);
            return false;
        }

        return fs.mkdirs(hpath);
    }

    // 读取HDFS目录信息
    public List<Map<String, Object>> getPathList(String path) throws FileNotFoundException, IOException {
        List<Map<String, Object>> files = new LinkedList<Map<String, Object>>();

        if (StringUtils.isNotEmpty(path)) {

            Path hpath = new Path(path);
            FileStatus[] filestatus = fs.listStatus(hpath);
            if (fs.exists(hpath) && filestatus.length > 0) {
                for (FileStatus f : filestatus) {

                    Map map = new HashMap<String, Object>();
                    map.put("filePath", f.getPath());
                    map.put("fileStatus", f.toString());
                    files.add(map);
                }
            }
        }
        return files;
    }

    /**
     * 使用IO流将远程文件上传到HDFS
     *
     * @param file
     * @throws Exception
     */
    public boolean uploadFile(String hdfsPath, MultipartFile file) {
        if (StringUtils.isEmpty(hdfsPath) || null == file) {
            return false;
        }
        String fileName = file.getOriginalFilename();

        // 上传时默认当前目录，后面自动拼接文件的目录
        Path newPath = new Path(hdfsPath + "/" + fileName);
        try {
            // 打开一个输出流
            FSDataOutputStream outputStream = fs.create(newPath);
            outputStream.write(file.getBytes());
            outputStream.close();
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 使用IO流从远程HDFS下载文件到本地
     *
     * @throws Exception
     */
    public boolean downLoadFile(String hdfsPath, String localPath) {

        if (StringUtils.isEmpty(hdfsPath) || StringUtils.isEmpty(localPath)) {
            return false;
        }

        try {
            FSDataInputStream open = fs.open(new Path(hdfsPath));
            FileOutputStream f = new FileOutputStream(new File(localPath));

            byte[] b = new byte[bufferSize];
            int len = 0;
            while ((len = open.read(b)) != -1) {
                f.write(b, 0, len);
            }

            open.close();
            f.close();
            return true;

        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 本地文件存入HDFS
     *
     * @return
     * @throws Exception
     */
    public boolean localToHdfs(String localPath, String hdfsPath) {
        if (StringUtils.isEmpty(localPath) || StringUtils.isEmpty(hdfsPath)) {
            return false;
        }

        // 上传路径
        Path clientPath = new Path(localPath);
        // 目标路径
        Path serverPath = new Path(hdfsPath);

        try {
            // 调用文件系统的文件复制方法，第一个参数是否删除原文件true为删除，默认为false
            fs.copyFromLocalFile(false, clientPath, serverPath);
        } catch (IOException e) {
            log.info(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 本地下载HDFS文件
     *
     * @throws Exception
     */
    public boolean HdfsToLocal(String hdfsPath, String localPath) {
        if (StringUtils.isEmpty(hdfsPath) || StringUtils.isEmpty(localPath)) {
            return false;
        }

        // 下载路径
        Path clientPath = new Path(hdfsPath);
        // 目标路径
        Path serverPath = new Path(localPath);

        try {
            // 调用文件系统的文件复制方法，第一个参数是否删除原文件true为删除，默认为false
            fs.copyToLocalFile(false, clientPath, serverPath);
        } catch (Exception e) {
            log.debug(e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * 读取HDFS文件内容
     *
     * @param path
     * @return
     * @throws Exception
     */
    public String readFile(String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        if (!fs.exists(new Path(path))) {
            return null;
        }

        // 目标路径
        Path srcPath = new Path(path);
        FSDataInputStream inputStream = null;
        try {
            inputStream = fs.open(srcPath);
            // 防止中文乱码
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String lineTxt = "";
            StringBuffer sb = new StringBuffer();
            while ((lineTxt = reader.readLine()) != null) {
                sb.append(lineTxt);
            }
            return sb.toString();
        } finally {
            inputStream.close();

        }
    }

    /**
     * 读取HDFS文件列表
     *
     * @param path
     * @return
     * @throws Exception
     */
    public List<Map<String, String>> listFile(String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        if (!fs.exists(new Path(path))) {
            return null;
        }

        // 目标路径
        Path srcPath = new Path(path);
        // 递归找到所有文件
        RemoteIterator<LocatedFileStatus> filesList = fs.listFiles(srcPath, true);
        List<Map<String, String>> returnList = new ArrayList<>();
        while (filesList.hasNext()) {
            LocatedFileStatus next = filesList.next();
            String fileName = next.getPath().getName();
            Path filePath = next.getPath();
            Map<String, String> map = new HashMap<>();
            map.put("fileName", fileName);
            map.put("filePath", filePath.toString());
            returnList.add(map);
        }

        return returnList;
    }

    /**
     * HDFS重命名文件
     *
     * @param oldName
     * @param newName
     * @return
     * @throws Exception
     */
//    public boolean renameFile(String oldName, String newName) throws Exception {
//        if (StringUtils.isEmpty(oldName) || StringUtils.isEmpty(newName)) {
//            return false;
//        }
//
//        // 原文件目标路径
//        Path oldPath = new Path(oldName);
//        // 重命名目标路径
//        Path newPath = new Path(newName);
//        boolean isOk = fs.rename(oldPath, newPath);
//
//        return isOk;
//    }

    /**
     * 删除HDFS文件
     *
     * @param path
     * @return
     * @throws Exception
     */
//    public boolean deleteFile(String path) throws Exception {
//        if (StringUtils.isEmpty(path)) {
//            return false;
//        }
//        if (!fs.exists(new Path(path))) {
//            return false;
//        }
//
//        Path srcPath = new Path(path);
////		boolean isOk = fs.deleteOnExit(srcPath);
////		永久性删除指定的文件或目录，只有recursive＝true时，一个非空目录及其内容才会被删除
//        boolean isOk = fs.delete(srcPath,true);
//
//        return isOk;
//    }

    /**
     * HDFS文件复制
     *
     * @param sourcePath
     * @param targetPath
     * @throws Exception
     */
    public void copyFile(String sourcePath, String targetPath) throws Exception {
        if (StringUtils.isEmpty(sourcePath) || StringUtils.isEmpty(targetPath)) {
            return;
        }

        // 原始文件路径
        Path oldPath = new Path(sourcePath);
        // 目标路径
        Path newPath = new Path(targetPath);

        FSDataInputStream inputStream = null;
        FSDataOutputStream outputStream = null;
        try {
            inputStream = fs.open(oldPath);
            outputStream = fs.create(newPath);

//            IOUtils.copyBytes(inputStream, outputStream, bufferSize, false);
        } finally {
            inputStream.close();
            outputStream.close();

        }
    }

    /**
     * 打开HDFS上的文件并返回byte数组
     *
     * @param path
     * @return
     * @throws Exception
     */
//    public byte[] openFileToBytes(String path) throws Exception {
//        if (StringUtils.isEmpty(path)) {
//            return null;
//        }
//        if (!fs.exists(new Path(path))) {
//            return null;
//        }
//
//        // 目标路径
//        Path srcPath = new Path(path);
//        try {
//            FSDataInputStream inputStream = fs.open(srcPath);
////            return IOUtils.readFullyToByteArray(inputStream);
//        } finally {
//
//        }
//    }

    /**
     * 打开HDFS上的文件并返回java对象
     *
     * @param path
     * @return
     * @throws Exception
     */
    // public <T extends Object> T openFileToObject(String path, Class<T> clazz)
    // throws Exception {
    // if (StringUtils.isEmpty(path)) {
    // return null;
    // }
    // if (!fs.exists(new Path(path))) {
    // return null;
    // }
    // String jsonStr = readFile(path);
    // return JsonUtil.fromObject(jsonStr, clazz);
    // }

    /**
     * 获取某个文件在HDFS的集群位置
     *
     * @param path
     * @return
     * @throws Exception
     */
//    public BlockLocation[] getFileBlockLocations(String path) throws Exception {
//        if (StringUtils.isEmpty(path)) {
//            return null;
//        }
//        if (!fs.exists(new Path(path))) {
//            return null;
//        }
//        // 目标路径
//        Path srcPath = new Path(path);
//        FileStatus fileStatus = fs.getFileStatus(srcPath);
//        return fs.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
//    }

    
//    public boolean mkdirFolder(String path) {
//        // TODO Auto-generated method stub
//        boolean target = false;
//        if (StringUtils.isEmpty(path)) {
//            return false;
//        }
//        if (existFile(path)) {
//            return true;
//        }
//        Path src = new Path(path);
//        try {
//            target = fileSystem.mkdirs(src);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            logger.error(e.getMessage());
//        }
//        return target;
//    }
//
//
//    public boolean existFile(String path) {
//        // TODO Auto-generated method stub
//        boolean target = false;
//
//        if (StringUtils.isEmpty(path)) {
//            return target;
//        }
//        Path src = new Path(path);
//        try {
//            target = fileSystem.exists(src);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            logger.error(e.getMessage());
//        }
//        return target;
//    }
//
//
//    public List<Map<String, Object>> readCatalog(String path) {
//        // TODO Auto-generated method stub
//        if (StringUtils.isEmpty(path)) {
//            return null;
//        }
//        if (!existFile(path)) {
//            return null;
//        }
//
//        // 目标路径
//        Path newPath = new Path(path);
//        FileStatus[] statusList = null;
//        try {
//            statusList = fileSystem.listStatus(newPath);
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            logger.error(e.getMessage());
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            logger.error(e.getMessage());
//        }
//        List<Map<String, Object>> list = new ArrayList<>();
//        if (null != statusList && statusList.length > 0) {
//            for (FileStatus fileStatus : statusList) {
//                Map<String, Object> map = new HashMap<>();
//                map.put("filePath", fileStatus.getPath());
//                map.put("fileStatus", fileStatus.toString());
//                list.add(map);
//            }
//            return list;
//        } else {
//            return null;
//        }
//
//    }
//
//
//    public void createFile(String path, MultipartFile file) {
//        // TODO Auto-generated method stub
//        if (StringUtils.isEmpty(path)) {
//            return;
//        }
//        String fileName = file.getName();
//        Path newPath = new Path(path + "/" + fileName);
//        // 打开一个输出流
//        FSDataOutputStream outputStream;
//        try {
//            outputStream = fileSystem.create(newPath);
//            outputStream.write(file.getBytes());
//            outputStream.close();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            logger.error(e.getMessage());
//        }
//    }
//
//
//    public String readFileContent(String path) {
//        // TODO Auto-generated method stub
//        StringBuffer sb = new StringBuffer();
//        if (StringUtils.isEmpty(path)) {
//            return null;
//        }
//        if (!existFile(path)) {
//            return null;
//        }
//        // 目标路径
//        Path srcPath = new Path(path);
//        FSDataInputStream inputStream = null;
//        try {
//            inputStream = fileSystem.open(srcPath);
//            // 防止中文乱码
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//            String lineTxt = "";
//            while ((lineTxt = reader.readLine()) != null) {
//                sb.append(lineTxt);
//            }
//        }catch(Exception e){
//            logger.error(e.getMessage());
//        } finally {
//            try {
//                inputStream.close();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                logger.error(e.getMessage());
//            }
//        }
//        return sb.toString();
//    }
//
//
//    public List<Map<String, String>> listFile(String path) {
//        // TODO Auto-generated method stub
//        List<Map<String, String>> returnList = new ArrayList<>();
//        if (StringUtils.isEmpty(path)) {
//            return null;
//        }
//        if (!existFile(path)) {
//            return null;
//        }
//        // 目标路径
//        Path srcPath = new Path(path);
//        // 递归找到所有文件
//        try{
//            RemoteIterator<LocatedFileStatus> filesList = fileSystem.listFiles(srcPath, true);
//            while (filesList.hasNext()) {
//                LocatedFileStatus next = filesList.next();
//                String fileName = next.getPath().getName();
//                Path filePath = next.getPath();
//                Map<String, String> map = new HashMap<>();
//                map.put("fileName", fileName);
//                map.put("filePath", filePath.toString());
//                returnList.add(map);
//            }
//        }catch(Exception e){
//            logger.error(e.getMessage());
//        }
//        return returnList;
//    }
//
//
//    public boolean renameFile(String oldName, String newName) {
//        // TODO Auto-generated method stub
//        boolean target = false;
//        if (StringUtils.isEmpty(oldName) || StringUtils.isEmpty(newName)) {
//            return false;
//        }
//        // 原文件目标路径
//        Path oldPath = new Path(oldName);
//        // 重命名目标路径
//        Path newPath = new Path(newName);
//        try{
//            target = fileSystem.rename(oldPath, newPath);
//        }catch(Exception e){
//            logger.error(e.getMessage());
//        }
//
//        return target;
//    }
//
//
//    public boolean deleteFile(String path) {
//        // TODO Auto-generated method stub
//        boolean target = false;
//        if (StringUtils.isEmpty(path)) {
//            return false;
//        }
//        if (!existFile(path)) {
//            return false;
//        }
//        Path srcPath = new Path(path);
//        try{
//            target = fileSystem.deleteOnExit(srcPath);
//        }catch(Exception e){
//            logger.error(e.getMessage());
//        }
//
//        return target;
//
//    }
//
//
//    public void uploadFile(String path, String uploadPath) {
//        // TODO Auto-generated method stub
//        if (StringUtils.isEmpty(path) || StringUtils.isEmpty(uploadPath)) {
//            return;
//        }
//        // 上传路径
//        Path clientPath = new Path(path);
//        // 目标路径
//        Path serverPath = new Path(uploadPath);
//
//        // 调用文件系统的文件复制方法，第一个参数是否删除原文件true为删除，默认为false
//        try {
//            fileSystem.copyFromLocalFile(false, clientPath, serverPath);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            logger.error(e.getMessage());
//        }
//
//    }
//
//
//    public void downloadFile(String path, String downloadPath) {
//        // TODO Auto-generated method stub
//        if (StringUtils.isEmpty(path) || StringUtils.isEmpty(downloadPath)) {
//            return;
//        }
//        // 上传路径
//        Path clientPath = new Path(path);
//        // 目标路径
//        Path serverPath = new Path(downloadPath);
//
//        // 调用文件系统的文件复制方法，第一个参数是否删除原文件true为删除，默认为false
//        try {
//            fileSystem.copyToLocalFile(false, clientPath, serverPath);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            logger.error(e.getMessage());
//        }
//
//    }
//
//
//    public void copyFile(String sourcePath, String targetPath) {
//        // TODO Auto-generated method stub
//        if (StringUtils.isEmpty(sourcePath) || StringUtils.isEmpty(targetPath)) {
//            return;
//        }
//        // 原始文件路径
//        Path oldPath = new Path(sourcePath);
//        // 目标路径
//        Path newPath = new Path(targetPath);
//
//        FSDataInputStream inputStream = null;
//        FSDataOutputStream outputStream = null;
//        try {
//            try{
//                inputStream = fileSystem.open(oldPath);
//                outputStream = fileSystem.create(newPath);
//
//                IOUtils.copyBytes(inputStream, outputStream, bufferSize, false);
//            }catch(Exception e){
//                logger.error(e.getMessage());
//            }
//        } finally {
//            try{
//                inputStream.close();
//                outputStream.close();
//            }catch(Exception e){
//                logger.error(e.getMessage());
//            }
//
//        }
//
//    }
//
//
//    public byte[] openFileToBytes(String path) {
//        // TODO Auto-generated method stub
//        byte[] bytes= null;
////		if (StringUtils.isEmpty(path)) {
////			return null;
////		}
////		if (!existFile(path)) {
////			return null;
////		}
////		// 目标路径
////		Path srcPath = new Path(path);
////		try {
////			FSDataInputStream inputStream = fileSystem.open(srcPath);
////			bytes = IOUtils.readFullyToByteArray(inputStream);
////		}catch(Exception e){
////			logger.error(e.getMessage());
////		}
//        return bytes;
//
//    }
//
//
//    public BlockLocation[] getFileBlockLocations(String path) {
//        // TODO Auto-generated method stub
//        BlockLocation[] blocks = null;
//        if (StringUtils.isEmpty(path)) {
//            return null;
//        }
//        if (!existFile(path)) {
//            return null;
//        }
//        // 目标路径
//        Path srcPath = new Path(path);
//        try{
//            FileStatus fileStatus = fileSystem.getFileStatus(srcPath);
//            blocks = fileSystem.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
//        }catch(Exception e){
//            logger.error(e.getMessage());
//        }
//        return blocks;
//
//    }
}