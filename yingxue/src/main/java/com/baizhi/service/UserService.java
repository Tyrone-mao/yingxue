package com.baizhi.service;

import com.baizhi.entity.City;
import com.baizhi.entity.Month;
import com.baizhi.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

public interface UserService {

    //添加
    String insert(User user);
    //查询所有
    List<User> queryAll();
    //删除
    HashMap<String, Object> delete(String id);
    //修改
    HashMap<String, Object> update(User user);
    //分页
    public HashMap<String, Object> queryPage(Integer page, Integer rows);
    //查一个
    User queryOne(String id);

    public HashMap<String,Object>  upload(MultipartFile headImg,String id);

    public HashMap<String,Object>  uploadFile(MultipartFile headImg,String id);

    List<City> queryByCity(String sex);

    List<Month> queryByMonth(String sex);



}
