package com.baizhi.utils;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

public class UploadAliyun {
    private static final Logger log = LoggerFactory.getLogger(UploadAliyun.class);
    // Endpoint以杭州为例，其它Region请按实际情况填写。
    // String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
   public static String endpoint = "https://oss-cn-beijing.aliyuncs.com";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
    public static String accessKeyId = "LTAI5tAj3dfvKnhwBdMmQ4Je";
    public static String accessKeySecret = "xwBQyRIq4xPM0mYcawoMxaKlp84fmt";
    //public static String bucketName = "yingx-mao";
    public static void upload(String bucketName, String objectName , MultipartFile headImg){

        /*String bucketName = "yingxs";  //存储空间名
        String objectName = "photos/aaa.jpg";  //文件名  可以指定文件目录
        String localFile="C:\\Users\\NANAN\\Pictures\\AliMail\\aaa.jpg";  //本地视频路径*/

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 创建PutObjectRequest对象。 参数：Bucket名字，指定文件名，文件本地路径
        //PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, new File(localFile));
        // 填写Byte数组。

        byte[] bytes = new byte[0];
        try {
            bytes = headImg.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
        ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));
        // 如果需要上传时设置存储类型与访问权限，请参考以下示例代码。
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // metadata.setObjectAcl(CannedAccessControlList.Private);
        // putObjectRequest.setMetadata(metadata);

        // 上传文件。
       // ossClient.putObject(putObjectRequest);
/*
        String str="https://yingx-test.oss-cn-beijing.aliyuncs.com/"+objectName;
        String s = str.replace("https://yingx-test.oss-cn-beijing.aliyuncs.com/", "");*/


        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     *视频截取帧
     *  参数:
     *  bucketName(String):指定存储空间名
     *  fileName(String):文件名
     * */
    public static URL interceptCover(String bucketName,String objectName){

        // 填写视频文件所在的Bucket名称。
        //String bucketName = "yingx-2010";
        // 填写视频文件的完整路径。若视频文件不在Bucket根目录，需携带文件访问路径，例如examplefolder/videotest.mp4。
        //String objectName = "video/1622451747384-2020宣传视频.mp4";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 使用精确时间模式截取视频50s处的内容，输出为JPG格式的图片，宽度为800，高度为600。
        String style = "video/snapshot,t_2000,f_jpg,w_800,h_600";
        // 指定过期时间为10分钟。
        Date expiration = new Date(new Date().getTime() + 1000 * 60 * 10 );
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, objectName, HttpMethod.GET);
        req.setExpiration(expiration);
        req.setProcess(style);
        URL signedUrl = ossClient.generatePresignedUrl(req);
        System.out.println(signedUrl);

        // 关闭OSSClient。
        ossClient.shutdown();

        return signedUrl;
    }

    /**
     *将网络文件上传至阿里云 网络地址
     *  参数:
     *  bucketName(String):指定存储空间名
     *  fileName(String):文件名
     *  netPath(String):网络地址  https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png
     * */
    public static void internetFileUploads(String bucketName,String fileName,String netPath) throws IOException {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 填写网络流地址。
        InputStream inputStream = new URL(netPath).openStream();
        // 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
        ossClient.putObject(bucketName, fileName, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     *视频截取帧并上传
     *  参数:
     *  bucketName(String):指定存储空间名
     *  objectName(String):要截取的视频的名字
     *  fileName(String):保存封面的名字
     * */
    public static URL interceptCoverUpload(String bucketName,String objectName,String fileName) throws IOException {

        // 填写视频文件所在的Bucket名称。
        //String bucketName = "yingx-2010";
        // 填写视频文件的完整路径。若视频文件不在Bucket根目录，需携带文件访问路径，例如examplefolder/videotest.mp4。
        //String objectName = "video/1622451747384-2020宣传视频.mp4";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 使用精确时间模式截取视频50s处的内容，输出为JPG格式的图片，宽度为800，高度为600。
        String style = "video/snapshot,t_2000,f_jpg,w_800,h_600";
        // 指定过期时间为10分钟。
        Date expiration = new Date(new Date().getTime() + 1000 * 60 * 10 );
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, objectName, HttpMethod.GET);
        req.setExpiration(expiration);
        req.setProcess(style);
        URL signedUrl = ossClient.generatePresignedUrl(req);
        System.out.println(signedUrl);

        // 填写网络流地址。
        InputStream inputStream = new URL(signedUrl.toString()).openStream();
        // 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
        ossClient.putObject(bucketName, fileName, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();

        return signedUrl;
    }


    public static void  deleteFile(String bucketName,String objectName){
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        String s = objectName.replace("https://yingx-mao.oss-cn-beijing.aliyuncs.com/", "");
        log.info("sss：{}",s);
        ossClient.deleteObject(bucketName, s);
        // 关闭OSSClient。
        ossClient.shutdown();

    }


}
