package com.baizhi.controller;

import com.baizhi.service.FeedBackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("feed")
public class FeedBackContorller {
    private static final Logger log = LoggerFactory.getLogger(FeedBackContorller.class);
    @Autowired
    private FeedBackService feedBackService;

    @RequestMapping("queryPage")
    public HashMap<String,Object>queryPage(Integer page,Integer rows){
        HashMap<String, Object> map = feedBackService.queryAll(page, rows);
        log.info("map:{}",map);
        return map;
    }







}
