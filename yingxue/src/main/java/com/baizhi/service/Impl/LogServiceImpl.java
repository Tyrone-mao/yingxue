package com.baizhi.service.Impl;

import com.baizhi.dao.LogMapper;
import com.baizhi.entity.Log;
import com.baizhi.service.LogService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
@Service
@Transactional
public class LogServiceImpl implements LogService {
    @Resource
    private LogMapper logMapper;
    @Override
    public HashMap<String, Object> queryPage(Integer page, Integer rows) {
        HashMap<String, Object> map = new HashMap<>();
        Log log = new Log();
        //设置当前页
        map.put("page",page);
        int count = logMapper.selectCount(log);
        //总条数
        map.put("records",count);
        //计算总页数 总页数 =总条数/每页展示条数
        Integer total=count%rows==0?count/rows:count/rows+1;
        //设置总页数 =总条数/每页展示条数
        map.put("total",total);
        List<Log> logs = logMapper.selectByRowBounds(log, new RowBounds((page - 1) * rows, rows));
        map.put("rows",logs);
        return map;

    }
}
