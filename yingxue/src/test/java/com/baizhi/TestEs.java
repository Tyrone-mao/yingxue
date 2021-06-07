package com.baizhi;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baizhi.dao.CategoryMapper;
import com.baizhi.dao.VideoMapper;
import com.baizhi.elasticsearch.VideoRepository;
import com.baizhi.entity.Category;
import com.baizhi.entity.User;
import com.baizhi.entity.Video;
import com.baizhi.service.UserService;
import com.baizhi.service.VideoService;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

@SpringBootTest(classes = YingxApplication.class)
public class TestEs {

    @Autowired
    private VideoService videoService;
    @Resource
    private VideoMapper videoMapper;

    /*@Autowired
    private VideoRepository videoRepository;*/

    @Test
    public void test1(){
        Video video = new Video();
        List<Video> select = videoMapper.selectAll();
        for (Video video1 : select) {
            System.out.println("video1 = " + video1);
        }
    }
}
