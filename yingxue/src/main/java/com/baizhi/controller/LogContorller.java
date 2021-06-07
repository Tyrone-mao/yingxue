package com.baizhi.controller;

import com.baizhi.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


@RestController
@RequestMapping("log")
public class LogContorller {
    @Autowired
    private LogService logService;
    @RequestMapping("queryPage")
    public HashMap<String,Object> queryPage( Integer page, Integer rows){
        HashMap<String, Object> map = logService.queryPage(page, rows);
        return map;
    }
}
