package com.baizhi.service;

import com.baizhi.entity.Video;
import com.baizhi.vo.VideoVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

public interface VideoService {


    //添加视频
    HashMap<String,Object> insert(Video video);

    //分页
    HashMap<String,Object> queryPage(Integer page,Integer rows);

    HashMap<String, Object>  uploadFile(MultipartFile headImg, String id);
    //删除
    HashMap<String, Object> delete(String id);

    //修改
    HashMap<String, Object> update(Video video);
    //查询首页视频
    HashMap<String,Object>queryByReleaseTime();
    //查询分类视频
    HashMap<String,Object> queryCateVideoList(String cateId);

    //创建索引
    void addEs();
    //删除索引
    void delEs();
    //查询高亮
    List<Video> queryEs(String content);


}
