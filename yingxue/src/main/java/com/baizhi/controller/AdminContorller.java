package com.baizhi.controller;

import com.baizhi.entity.Admin;
import com.baizhi.service.AdminService;
import com.baizhi.utils.ImageCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("admin")
public class AdminContorller {
    private static final Logger log = LoggerFactory.getLogger(AdminContorller.class);
    @Resource
    private AdminService adminService;
    @RequestMapping("login")
    @ResponseBody
    public Map<String,Object> login(String code,String name, String password, HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();
        try {
            Admin admin = adminService.login(code, name, password, request);
            map.put("statu",true);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("statu",false);
            map.put("massge",e.getMessage());
        }
        return map;

    }

    @RequestMapping("getImageCode")
    public void getImageCode(HttpServletResponse response, HttpSession session){
        String code = ImageCodeUtil.getSecurityCode();
        BufferedImage image = ImageCodeUtil.createImage(code);
        session.setAttribute("code",code);
        System.out.println("******code = " + code);
        try {
            ServletOutputStream os = response.getOutputStream();
            ImageIO.write(image, "png",os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("logout")
    public String logout(HttpServletRequest request){
        request.getSession().removeAttribute("admin");
        log.info("123");
        return "redirect:/login/login.jsp";
    }
    @RequestMapping("queryPage")
    @ResponseBody
    public HashMap<String,Object> queryPage(Integer page,Integer rows){
        HashMap<String, Object> map = adminService.queryPage(page, rows);
        return map;
    }
    @ResponseBody
    @RequestMapping("change")
    public Object change(String oper,Admin admin){
        if (oper.equals("add")){
            //添加
            adminService.insert(admin);
        }else if (oper.equals("edit")){
            //修改
            adminService.update(admin);
        }else if (oper.equals("del")){
            adminService.delete(admin.getId());
        }
        return null;
    }
    @RequestMapping("updateStatus")
    @ResponseBody
    public String updateStatus(String id,String status){
        Admin admin = adminService.queryOne(id);
        admin.setStatus(status);
        adminService.update(admin);
        return null;
    }

}
