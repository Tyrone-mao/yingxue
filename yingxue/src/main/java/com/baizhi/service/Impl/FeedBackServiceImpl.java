package com.baizhi.service.Impl;

import com.baizhi.dao.FeedBackMapper;
import com.baizhi.entity.FeedBack;

import com.baizhi.service.FeedBackService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
@Service
@Transactional
public class FeedBackServiceImpl implements FeedBackService {

    private static final Logger log = LoggerFactory.getLogger(FeedBackServiceImpl.class);
    @Resource
    private FeedBackMapper feedBackMapper;
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public HashMap<String,Object> queryAll(Integer page, Integer rows){

        HashMap<String, Object> map = new HashMap<>();
        FeedBack feedBack=new FeedBack();
        //设置当前页
        map.put("page",page);

        //查询总条数
        int count = feedBackMapper.selectCount(new FeedBack());
        log.info("count{}",count);
        //设置总条数
        map.put("records",count);


        //计算总页数 总页数 =总条数/每页展示条数
        Integer total=count%rows==0?count/rows:count/rows+1;

        //设置总页数 =总条数/每页展示条数
        map.put("total",total);

        /*  3
         *   1   1~3
         *   2   4~6
         * */
        List<FeedBack> feedBacks = feedBackMapper.queryAll((page-1)*rows, rows);
        //返回的数据
        map.put("rows",feedBacks);
        return map;
    }
}
