package com.baizhi;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baizhi.dao.CategoryMapper;
import com.baizhi.entity.Category;
import com.baizhi.entity.User;
import com.baizhi.service.UserService;
import org.apache.poi.ss.usermodel.Workbook;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.*;
import java.util.List;
@SpringBootTest
public class TestEasyPOI {
    @Autowired
    private UserService userService;
    @Test//导出表
    public void testOut(){
        List<User> users = userService.queryAll();
        //title：标题栏  sheetName: 目录表名
        ExportParams exportParams = new ExportParams("用户信息表", "用户表");
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, User.class, users);
        try {
            workbook.write(new FileOutputStream(new File("E:/userExcel.xls")));
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Resource
    private CategoryMapper categoryMapper;
    @Test//1对多
    public void testOut2(){
        List<Category> categories = categoryMapper.queryAll();
        //title：标题栏  sheetName: 目录表名
        ExportParams exportParams = new ExportParams("分类信息表", "分类");
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, Category.class, categories);
        try {
            workbook.write(new FileOutputStream(new File("E:/cateExcel.xls")));
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testIn() throws Exception {
        //创建导入对象
        ImportParams params = new ImportParams();
        params.setTitleRows(1); //表格标题行数,默认0
        params.setHeadRows(1);  //表头行数,默认1
        List<User> list = ExcelImportUtil.importExcel(new FileInputStream(new File("E:/userExcel.xls")), User.class, params);
        for (User user : list) {
            System.out.println("user = " + user);
        }
    }

}
