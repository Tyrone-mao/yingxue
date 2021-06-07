package com.baizhi.service.Impl;

import com.baizhi.dao.AdminMapper;
import com.baizhi.entity.Admin;
import com.baizhi.service.AdminService;
import com.baizhi.utils.Md5Utils;
import com.baizhi.utils.UUIDUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;


@Service
@Transactional
public class AdminServiceImpl implements AdminService {
    @Resource
    private AdminMapper adminMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Admin login(String code,String name, String password, HttpServletRequest request) {
        Admin admin = adminMapper.queryName(name);
        String code1 = (String)request.getSession().getAttribute("code");
        if(code1.equals(code)){
            if (admin==null){
                throw new RuntimeException("用户不存在");
            }else{
                String salt=admin.getSalt();
                String md5Code = Md5Utils.getMd5Code(salt+ password);
                if (md5Code.equals(admin.getPassword())){
                    request.getSession().setAttribute("admin",admin);
                    return admin;
                }else{
                    throw new RuntimeException("密码错误");
                }

            }
        }else {
            throw new RuntimeException("验证码错误");
        }

    }

    @Override
    public void insert(Admin admin) {
        //获取id
        admin.setId(UUIDUtil.getUUID());
        String salt = Md5Utils.getSalt(6);
        //获取盐
        admin.setSalt(salt);
        //获取xin密码
        String md5Code = Md5Utils.getMd5Code(salt + admin.getPassword());
        admin.setPassword(md5Code);
        adminMapper.insert(admin);

    }

    @Override
    public void update(Admin admin) {
        adminMapper.updateByPrimaryKeySelective(admin);
    }

    @Override
    public void delete(String id) {
        adminMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Admin>  queryAll() {
        List<Admin> admins = adminMapper.selectAll();
        return admins;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public HashMap<String, Object> queryPage(Integer page, Integer rows) {
        HashMap<String, Object> map = new HashMap<>();
        Admin admin = new Admin();
        //设置当前页
        map.put("page",page);

        //查询总条数
        Integer count =adminMapper.selectCount(admin);
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
        RowBounds rowBounds = new RowBounds((page-1)*rows, rows);
        List<Admin> admins = adminMapper.selectByRowBounds(admin,rowBounds);
        //返回的数据
        map.put("rows",admins);

        return map;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Admin queryOne(String id) {
        Admin admin = adminMapper.selectByPrimaryKey(id);
        return admin;
    }


}
