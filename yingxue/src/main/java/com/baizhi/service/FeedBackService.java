package com.baizhi.service;

import com.baizhi.dao.FeedBackMapper;
import com.baizhi.entity.FeedBack;

import java.util.HashMap;
import java.util.List;

public interface FeedBackService {

    public HashMap<String,Object> queryAll(Integer page,Integer rows);

}
