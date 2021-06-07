package com.baizhi.service.Impl;

import com.baizhi.annotation.AddCache;
import com.baizhi.annotation.AddLog;
import com.baizhi.annotation.DelCache;
import com.baizhi.dao.UserMapper;
import com.baizhi.entity.City;
import com.baizhi.entity.Month;
import com.baizhi.entity.User;
import com.baizhi.service.UserService;
import com.baizhi.utils.UUIDUtil;
import com.baizhi.utils.UploadAliyun;
import org.apache.commons.io.FilenameUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Resource
    private UserMapper userMapper;
    @Autowired
    private HttpSession session;
    @AddLog("添加用户")
    @Override
    @DelCache
    public String insert(User user) {
        user.setId(UUIDUtil.getUUID());
        user.setRegistTime(new Date());
        user.setStatus("1");
        user.setWechat(user.getPhone());
        userMapper.insert(user);
        return user.getId();
    }
    @AddCache
    @Override
    public List<User> queryAll() {
        List<User> users = userMapper.selectAll();
        return users;
    }
    @DelCache
    @AddLog("删除用户")
    @Override
    public HashMap<String, Object> delete(String id) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            User user = userMapper.selectByPrimaryKey(id);
            /*//获取绝对路径
            String realPath = session.getServletContext().getRealPath(user.getHeadImg());
            File file = new File(realPath);
            //判断文件 是否存在
            if (file.exists()&&file.isFile())file.delete();*/
            UploadAliyun.deleteFile("yingx-mao",user.getHeadImg());
            userMapper.deleteByPrimaryKey(id);
            map.put("massge","删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("massge","删除失败");
        }
        return map;
    }
    @DelCache
    @AddLog("修改用户")
    @Override
    public HashMap<String, Object> update(User user) {
        HashMap<String, Object> map = new HashMap<>();
        //获取旧文件名
        User user1 = userMapper.selectByPrimaryKey(user.getId());
        log.info("user1:{}",user1);
        //判段文件是否修改
        if(!user.getHeadImg().equals("")){
            /*//获取绝对路径
            String realPath = session.getServletContext().getRealPath(user1.getHeadImg());
            File file = new File(realPath);
            //删除旧文件
            if (file.exists()&&file.isFile())file.delete();*/
            UploadAliyun.deleteFile("yingx-mao",user1.getHeadImg());
            map.put("statu",true);
            map.put("id",user.getId());
        }else {
            user.setHeadImg(user1.getHeadImg());
            map.put("statu",false);
        }
        userMapper.updateByPrimaryKeySelective(user);

        return map;
    }
    @AddCache
    @Override
    public HashMap<String, Object> queryPage(Integer page, Integer rows) {
        HashMap<String, Object> map = new HashMap<>();
        User user = new User();
        //设置当前页
        map.put("page",page);

        //查询总条数
        Integer count =userMapper.selectCount(user);
        //设置总条数
        map.put("records",count);


        //计算总页数 总页数 =总条数/每页展示条数
        Integer total=count%rows==0?count/rows:count/rows+1;

        //设置总页数 =总条数/每页展示条数
        map.put("total",total);

        /*  3
         *   1   1~3
         *   2   4~6
         * */
        RowBounds rowBounds = new RowBounds((page-1)*rows, rows);
        List<User> users = userMapper.selectByRowBounds(user,rowBounds);
        //返回的数据
        map.put("rows",users);

        return map;
    }
    @AddCache
    @Override
    public User queryOne(String id) {
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }
    @DelCache
    @Override
    public HashMap<String,Object> upload(MultipartFile headImg,String id){

        HashMap<String, Object> map = new HashMap<>();
        String newname= null;
        String time = null;
        try {
            //获取文件名
            String filename = headImg.getOriginalFilename();
            //获取后缀
            String extension = FilenameUtils.getExtension(filename);
            //获取时间戳
            String timename = new SimpleDateFormat("yyyyMMddHHmmssSS").format(new Date()).toString();
            //拼接新名字
            newname = timename+"."+extension;
            //获取时间目录名
            time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            //获取路径
            String realPath = session.getServletContext().getRealPath("/upload/" + time);
            //判断文件夹是否存在
            File file = new File(realPath);
            if(!file.exists())file.mkdirs();
            //上传文件
            headImg.transferTo(new File(realPath,newname));
            User user = new User();
            user.setId(id);
            String pa="/upload/" + time+"/"+newname;
            user.setHeadImg(pa);
            userMapper.updateByPrimaryKeySelective(user);
            map.put("massge","文件上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            map.put("massge","文件上传失败");
        }
        return map;
    }
    @DelCache
    @Override
    public HashMap<String, Object> uploadFile(MultipartFile headImg, String id) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            //1.获取文件名
            String filename = headImg.getOriginalFilename();
            //2.修改文件名+时间戳
            String newName=new Date().getTime()+"-"+filename;
            //3.文件上传
            UploadAliyun.upload("yingx-mao","headImg/"+newName,headImg);
            //4.修改用户地址 https://yingx-test.oss-cn-beijing.aliyuncs.com/
            User user = new User();
            user.setId(id);
            user.setHeadImg("https://yingx-mao.oss-cn-beijing.aliyuncs.com/headImg/"+newName);
            userMapper.updateByPrimaryKeySelective(user);
            map.put("massge","文件上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("massge","文件上传失败");
        }
        return map;
    }
    @AddCache
    @Override
    public List<City> queryByCity(String sex) {

        return userMapper.queryCity(sex);
    }
    @AddCache
    @Override
    public List<Month> queryByMonth(String sex) {
        return userMapper.queryMonth(sex);
    }
}
