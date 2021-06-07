package com.baizhi.service;

import java.util.HashMap;

public interface LogService {
    //分页查询
    HashMap<String,Object> queryPage(Integer page,Integer rows);

}
