package com.atguigu.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author nisang
 * 2024/3/8 17:00
 * @version 1.0
 * Ruoyi-Cloud-Plus开发小组
 */
public class HdfsClient {
    /**
     * 测试创建文件夹
     *
     * @throws IOException
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    @Test
    public void testMkdirs() throws IOException, InterruptedException, URISyntaxException {

        // 1 获取文件系统
        Configuration configuration = new Configuration();
        // 配置在集群上运行
//         configuration.set("fs.defaultFS", "hdfs://hadoop2:9000");
//         FileSystem fs = FileSystem.get(configuration);

        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop2:9000"), configuration, "root");

        // 2 创建目录
        fs.mkdirs(new Path("/9527/aaa/bbb"));

        // 3 关闭资源
        fs.close();
    }

    /**
     * 上传文件
     *
     * @throws IOException
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    @Test
    public void testCopyFromLocalFile() throws IOException, InterruptedException, URISyntaxException {

        // 1 获取文件系统
        Configuration configuration = new Configuration();
        configuration.set("dfs.replication", "2");
        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop2:9000"), configuration, "root");

        // 2 上传文件
        fs.copyFromLocalFile(new Path("d:/大数据/hadoop学习/测试文件/hadoop_test1.txt"), new Path("/hadoop_test1.txt"));

        // 3 关闭资源
        fs.close();

        System.out.println("over");
    }

    /**
     * 下载文件
     *
     * @throws IOException
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    @Test
    public void testCopyToLocalFile() throws IOException, InterruptedException, URISyntaxException {

        // 1 获取文件系统
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop2:9000"), configuration, "atguigu");

        // 2 执行下载操作
        // boolean delSrc 指是否将原文件删除
        // Path src 指要下载的文件路径
        // Path dst 指将文件下载到的路径
        // boolean useRawLocalFileSystem 是否开启文件校验
        fs.copyToLocalFile(false, new Path("/hadoop_test1.txt"), new Path("d:/大数据/hadoop学习/测试文件/hadoop_test1_download.txt"), true);

        // 3 关闭资源
        fs.close();
    }

    /**
     * 文件夹删除
     *
     * @throws IOException
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    @Test
    public void testDelete() throws IOException, InterruptedException, URISyntaxException {

        // 1 获取文件系统
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop2:9000"), configuration, "root");

        // 2 执行删除
        fs.delete(new Path("/9527/"), true);

        // 3 关闭资源
        fs.close();
    }

    /**
     * 文件夹改名
     *
     * @throws IOException
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    @Test
    public void testRename() throws IOException, InterruptedException, URISyntaxException {

        // 1 获取文件系统
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop2:9000"), configuration, "root");

        // 2 修改文件名称
        fs.rename(new Path("/hadoop_test1.txt"), new Path("/hadoop_test2.txt"));

        // 3 关闭资源
        fs.close();
    }

    /**
     * 文件夹详情查看
     *
     * @throws IOException
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    @Test
    public void testListFiles() throws IOException, InterruptedException, URISyntaxException {

        // 1获取文件系统
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop2:9000"), configuration, "root");

        // 2 获取文件详情
        RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/"), true);

        while (listFiles.hasNext()) {
            LocatedFileStatus status = listFiles.next();

            // 输出详情
            // 文件名称
            System.out.println(status.getPath().getName());
            // 长度
            System.out.println(status.getLen());
            // 权限
            System.out.println(status.getPermission());
            // 分组
            System.out.println(status.getGroup());

            // 获取存储的块信息
            BlockLocation[] blockLocations = status.getBlockLocations();

            for (BlockLocation blockLocation : blockLocations) {// 获取块存储的主机节点
                String[] hosts = blockLocation.getHosts();

                for (String host : hosts) {
                    System.out.println(host);
                }
            }

            System.out.println("-----------班长的分割线----------");
        }

// 3 关闭资源
        fs.close();
    }

    /**
     * 文件夹判断
     *
     * @throws IOException
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    @Test
    public void testListStatus() throws IOException, InterruptedException, URISyntaxException {

        // 1 获取文件配置信息
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop2:9000"), configuration, "root");

        // 2 判断是文件还是文件夹
        FileStatus[] listStatus = fs.listStatus(new Path("/"));

        for (FileStatus fileStatus : listStatus) {

            // 如果是文件
            if (fileStatus.isFile()) {
                System.out.println("f:" + fileStatus.getPath().getName());
            } else {
                System.out.println("d:" + fileStatus.getPath().getName());
            }
        }

        // 3 关闭资源
        fs.close();
    }
}
