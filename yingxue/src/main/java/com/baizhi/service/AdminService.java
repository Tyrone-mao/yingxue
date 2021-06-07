package com.baizhi.service;

import com.baizhi.entity.Admin;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;


public interface AdminService {
    //登录
    Admin login(String code,String name, String password, HttpServletRequest request);
    //添加
    void insert(Admin admin);
    //修改
    void update(Admin admin);
    //删除
    void delete(String id);
    //查所有
    List<Admin> queryAll();
    //分页
    public HashMap<String, Object> queryPage(Integer page, Integer rows);

    Admin queryOne(String id);



}
