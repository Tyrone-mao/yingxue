package com.baizhi;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.baizhi.dao.*;
import com.baizhi.entity.*;
import com.baizhi.service.AdminService;
import com.baizhi.service.UserService;
import com.baizhi.utils.UUIDUtil;
import com.baizhi.vo.VideoVO;
import org.apache.ibatis.session.RowBounds;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

@SpringBootTest
class YingxApplicationTests {
    /*@Resource
    private AdminMapper adminMapper;
    @Resource
    private AdminService adminService;*/
    @Resource
    private UserService userService;
    @Resource
    private UserMapper userMapper;

    /*@Test
    void contextLoads() {
        Admin huxz = adminMapper.queryName("huxz");
        //System.out.println("huxz = " + huxz);

        adminService.insert(huxz);

    }*/

    @Test
    public void testUser(){

        User u=new User();
        /*u.setName("xiaohua");
        u.setPhone("13366667777");
        u.setSign("小花");
        u.setWechat("13366667777");
        u.setHeadImg("00000");
        userService.insert(u);*/
        /*UserExample userExample = new UserExample();
        RowBounds rowBounds = new RowBounds(0, 2);
        List<User> users = userMapper.selectByExampleAndRowBounds(userExample, rowBounds);
        for (User user : users) {
            System.out.println(user);
        }*/

        /*int i = userMapper.selectCount(u);
        System.out.println("i = " + i);*/
        List<User> users = userMapper.selectAll();
        for (User user : users) {
            System.out.println("user = " + user);
            
        }

    }
    @Resource
    private FeedBackMapper feedBackMapper;
    @Test
    public void tsetFB(){
        /*FeedBack feedBack = new FeedBack();
        feedBack.setId(UUIDUtil.getUUID());
        feedBack.setFeedTime(new Date());
        feedBack.setTitle("打雷了");
        feedBack.setContent("太响了");
        feedBack.setUserId("b13efa3d9714470891a01d2403a17efe");
        feedBackMapper.insert(feedBack);*/
        /*List<FeedBack> feedBacks = feedBackMapper.queryAll(0,1);
        for (FeedBack feedBack : feedBacks) {
            System.out.println(feedBack);
        }*/

        int count = feedBackMapper.selectCount(new FeedBack());
        System.out.println("count = " + count);
    }
    @Resource
    private CategoryMapper categoryMapper;
    @Test
    public void testcate(){

        List<Category> categories = categoryMapper.queryAll();
        for (Category categorys : categories) {
            System.out.println("category = " + categorys);
            
        }
       /* Category category = new Category();
        category.setLevels("1");
        List<Category> select = categoryMapper.select(category);
        category.setLevels(null);
        for (Category category1 : select) {
            category.setParentId(category1.getId());
            List<Category> list = categoryMapper.select(category);
            System.out.println("list = " + list);
            category1.setCategoryList(list);

        }*/
        /*category.setId(UUIDUtil.getUUID());
        category.setCateName("aasd");
        category.setLevels("1");
        categoryMapper.insert(category);*/
       // category.setParentId("1");
       // List<Category> categories = categoryMapper.select(category);
        //category.setLevels("1");
        // List<Category> categories = categoryMapper.selectByExample(category); error


        //Integer count =categoryMapper.selectCount(category);
     //   System.out.println("count = " + count);
        //List<Category> categories = categoryMapper.selectByRowBounds(category,new RowBounds(0,4));
       /* for (Category category1 : categories) {
            System.out.println("category1 = " + category1);
        }*/
    }


    @Test
    public void uploadAli(){
        // Endpoint以杭州为例，其它Region请按实际情况填写。
       // String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
        String endpoint = "https://oss-cn-beijing.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
        String accessKeyId = "LTAI5tAj3dfvKnhwBdMmQ4Je";
        String accessKeySecret = "xwBQyRIq4xPM0mYcawoMxaKlp84fmt";


        String bucketName = "yingx-mao";
        // <yourObjectName>上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        String objectName = "aa/183.jfif";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件到指定的存储空间（bucketName）并将其保存为指定的文件名称（objectName）。
        /*String content = "毛羽丰\\新建文件夹\\183.jfif";
        ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content.getBytes()));*/
        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(bucketName, objectName);

        // 关闭OSSClient。
        ossClient.shutdown();
    }
    @Resource
    private VideoMapper v;
    
    @Test
    public void vo(){
       /* List<VideoVO> videoVOS = v.queryByReleaseTime();
        for (VideoVO videoVO : videoVOS) {
            System.out.println("videoVO = " + videoVO);
            
        }*/
        List<Video> videos = v.selectByRowBounds(new Video(), new RowBounds(1, 2));
        for (Video video : videos) {
            System.out.println("video = " + video);
        }

    }


}
