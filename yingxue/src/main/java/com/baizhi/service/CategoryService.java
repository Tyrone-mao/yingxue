package com.baizhi.service;

import com.baizhi.entity.Category;

import java.util.HashMap;
import java.util.List;

public interface CategoryService {

    //添加1级类别
    void addLevelOne(Category category);
    //查询所有一级
    HashMap<String, Object> queryByOne(Integer  page,Integer rows);
    //查询一级下的2级类别
    HashMap<String, Object>queryByTwo(String id,Integer  page,Integer rows);
    //修改
    void update(Category category);
    //删除
    HashMap<String, Object>  delete(Category category);
    //查询所有
    HashMap<String, Object>  queryAllCategory();
}
