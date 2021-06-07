package com.baizhi.app;

import com.baizhi.service.CategoryService;
import com.baizhi.service.VideoService;
import com.baizhi.utils.SendPhoneCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("app")
public class AppInterface {
    @Autowired
    private VideoService videoService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("getPhoneCode")
    public HashMap<String,Object> getPhoneCode(String phone){
        HashMap<String, Object> map = new HashMap<>();
        String massge =null;

        try {
            //获取随机验证码
            String random = SendPhoneCodeUtil.getRandom(6);
            //获取返回信息
            massge = SendPhoneCodeUtil.sendPhoneCode(phone, random);
            if (massge.equals("发送成功")){
                map.put("data",phone);
                map.put("message",massge);
                map.put("status",100);
            }else {
                map.put("data",phone);
                map.put("message",massge);
                map.put("status",101);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("data",phone);
            map.put("message",massge);
            map.put("status",101);
        }
        return map;
    }
    //主页查询
    @GetMapping("queryByReleaseTime")
    public HashMap<String,Object> queryByReleaseTime() {
        HashMap<String, Object> map = videoService.queryByReleaseTime();
        return map;
    }
    //查询所有类别
    @GetMapping("queryAllCategory")
    public HashMap<String, Object> queryAllCategory() {
        HashMap<String, Object> map = categoryService.queryAllCategory();
        return map;
    }
    @GetMapping("queryCateVideoList")
    public HashMap<String, Object> queryCateVideoList(String cataId) {
        HashMap<String, Object> map = videoService.queryCateVideoList(cataId);
        return map;
    }

}
