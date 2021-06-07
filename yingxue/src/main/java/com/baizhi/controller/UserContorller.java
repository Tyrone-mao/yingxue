package com.baizhi.controller;

import com.baizhi.entity.City;
import com.baizhi.entity.Month;
import com.baizhi.entity.User;
import com.baizhi.service.UserService;
import com.baizhi.utils.SendPhoneCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserContorller {
    private static final Logger log = LoggerFactory.getLogger(UserContorller.class);
    @Autowired
    private UserService userService;

    @RequestMapping("queryPage")
    public HashMap<String,Object> queryPage(Integer page,Integer rows){
        HashMap<String, Object> map = userService.queryPage(page, rows);
        return map;
    }

    @RequestMapping("change")
    public HashMap<String,Object> change(User user, String oper){
        HashMap<String, Object> map = new HashMap<>();

        if(oper.equals("add")){
            //调用添加的业务方法
            System.out.println("调用添加的业务方法");
            String id = userService.insert(user);
            map.put("id",id);
            return map;
        }

        if(oper.equals("edit")){
            //调用修改的业务方法
            System.out.println("调用修改的业务方法");
            map = userService.update(user);
        }

        if(oper.equals("del")){
            //调用删除的业务方法
            System.out.println("调用删除的业务方法");
            map = userService.delete(user.getId());
        }

        return map;
    }

    @RequestMapping("updateStatus")
    public String updateStatus(String id,String status){
        User user = userService.queryOne(id);
        user.setStatus(status);
        userService.update(user);
        return null;

    }
    @RequestMapping("upload")
    public HashMap<String, Object>  upload(MultipartFile headImg,String id){
        log.info("headimg{}",headImg.getOriginalFilename());
        HashMap<String, Object> map = userService.uploadFile(headImg, id);
        return map;
    }
    @RequestMapping("getPhoneCode")
    public HashMap<String,Object> getPhoneCode(String phone){
        HashMap<String, Object> map = new HashMap<>();
        String massge =null;
        try {
            //获取随机验证码
            String random = SendPhoneCodeUtil.getRandom(6);
            //获取返回信息
             massge = SendPhoneCodeUtil.sendPhoneCode(phone, random);
            if (massge.equals("发送成功")){
                map.put("massge",massge);
                map.put("statu",100);
            }else {
                map.put("massge",massge);
                map.put("statu",101);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("massge",massge);
            map.put("statu",101);
        }
        return map;
    }

    @RequestMapping("getUserDatas")
    public HashMap<String, Object> getUserDatas(){
        //获取月份数据
        List<String> monthList = Arrays.asList("1月", "2月", "3月", "4月", "5月", "6月");

        /**
         Months(month=2, count=2)
         Months(month=3, count=2)
         Months(month=4, count=1)
         Months(month=5, count=1)
         [10],
         * */
        ArrayList<Integer> boysCountList = queryCount("男");
        ArrayList<Integer> girlsCountList = queryCount("女");
        //select * from user where month=1月 and sex=男
        HashMap<String, Object> map = new HashMap<>();
        map.put("month", monthList);
        map.put("boys", boysCountList);
        map.put("girls", girlsCountList);

        return map;
    }

    public ArrayList<Integer> queryCount(String sex){
        List<Month> monthsList = userService.queryByMonth(sex);
        //创建用户数据集合
        ArrayList<Integer> boysCountList = new ArrayList<>();
        boolean b=false;
        //遍历数据集合
        for (int i = 1; i < 7; i++) {
            for (Month month : monthsList) {
                if (month.getMonths()==i){
                    boysCountList.add(month.getValue());
                    b=true;
                    break;
                }

            }
            if (b==false){
                boysCountList.add(0);
            }
        }
        return boysCountList;
    }
    @RequestMapping("getUserCity")
    public HashMap<String, Object> getUserCity(){
        HashMap<String, Object> map = new HashMap<>();
        List<City> boyList = userService.queryByCity("男");
        List<City> girlList = userService.queryByCity("女");
        map.put("boy",boyList);
        map.put("girl",girlList);
        return map;

    }


}
