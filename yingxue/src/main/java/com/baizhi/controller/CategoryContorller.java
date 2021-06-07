package com.baizhi.controller;

import com.baizhi.entity.Category;
import com.baizhi.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("category")
public class CategoryContorller {
    private static final Logger log = LoggerFactory.getLogger(CategoryContorller.class);
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("queryByOne")
    public HashMap<String,Object> queryByOne(Integer page,Integer rows){
        HashMap<String, Object> map = categoryService.queryByOne(page, rows);
        return map;
    }


    @RequestMapping("queryByTwo")
    public HashMap<String,Object> queryByTwo(Integer page,Integer rows,String id){
        HashMap<String, Object> map = categoryService.queryByTwo(id, page, rows);
        return map;
    }
    @RequestMapping("adit")
    public HashMap<String,Object> adit(Category category, String oper){

        if(oper.equals("add")){
            categoryService.addLevelOne(category);
        }

        if(oper.equals("edit")){
            log.info("++++++id:{}",category.getId());
            categoryService.update(category);

        }
        if (oper.equals("del")){
            HashMap<String, Object> map = categoryService.delete(category);
            log.info("map:{}",map);
            return map;
        }
        return null;
    }


}
