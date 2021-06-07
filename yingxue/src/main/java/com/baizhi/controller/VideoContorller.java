package com.baizhi.controller;

import com.baizhi.entity.Video;
import com.baizhi.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("video")
public class VideoContorller {
    private static final Logger log = LoggerFactory.getLogger(VideoContorller.class);
    @Autowired
    private VideoService videoService;
    @RequestMapping("queryPage")
    public HashMap<String,Object> queryPage(Integer page,Integer rows){
        HashMap<String, Object> map = videoService.queryPage(page, rows);
        return map;
    }
    @RequestMapping("change")
    public HashMap<String,Object> change(Video video, String oper){
        HashMap<String, Object> map = new HashMap<>();
        if (oper.equals("add")){
            map = videoService.insert(video);

        }
        if (oper.equals("del")){
            map = videoService.delete(video.getId());

        }
        if(oper.equals("edit")){
            map = videoService.update(video);
            log.info("map:{}",map);
        }
        return  map;
    }

    @RequestMapping("upload")
    public HashMap<String, Object>  upload(MultipartFile videoPath, String id){
        log.info("videoPath{}",videoPath.getOriginalFilename());
        HashMap<String, Object> map = videoService.uploadFile(videoPath,id);
        return map;
    }

    @RequestMapping("searchVdieo")
    public List<Video> searchVdieo(String content) {
        List<Video> videos = videoService.queryEs(content);
        return videos;

    }




}
