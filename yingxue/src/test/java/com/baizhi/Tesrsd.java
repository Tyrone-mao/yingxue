package com.baizhi;

import com.baizhi.dao.UserMapper;
import com.baizhi.entity.User;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class Tesrsd {
    @Resource
    private UserMapper userMapper;

    @Test
    public void asd(){
        List<User> users = userMapper.selectAll();
        for (User user : users) {
            System.out.println("user = " + user);

        }
    }
}
