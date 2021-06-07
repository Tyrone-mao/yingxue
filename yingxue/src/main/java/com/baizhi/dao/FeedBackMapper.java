package com.baizhi.dao;

import com.baizhi.entity.FeedBack;
import com.baizhi.entity.FeedBackExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface FeedBackMapper extends Mapper<FeedBack> {

    List<FeedBack> queryAll(@Param("page") Integer page,@Param("rows") Integer rows);

}