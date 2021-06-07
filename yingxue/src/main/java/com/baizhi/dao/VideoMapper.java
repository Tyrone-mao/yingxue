package com.baizhi.dao;

import com.baizhi.entity.Video;
import com.baizhi.entity.VideoExample;
import java.util.List;

import com.baizhi.vo.VideoCateVO;
import com.baizhi.vo.VideoVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface VideoMapper extends Mapper<Video> {

    //查找所有视频
    List<VideoVO> queryByReleaseTime();
    //根据分类查询视频
    List<VideoCateVO> queryCateVideoList(String cateId);
}