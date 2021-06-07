package com.baizhi.service.Impl;

import com.baizhi.annotation.AddCache;
import com.baizhi.annotation.AddLog;
import com.baizhi.annotation.DelCache;
import com.baizhi.dao.CategoryMapper;
import com.baizhi.entity.Category;
import com.baizhi.service.CategoryService;
import com.baizhi.utils.UUIDUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;
    @AddLog("添加类别")
    @Override
    @DelCache
    public void addLevelOne(Category category) {
        category.setId(UUIDUtil.getUUID());
        if(category.getParentId()!=null){
            category.setLevels("2");
        }else {
            category.setLevels("1");
            category.setParentId(null);
        }
        categoryMapper.insert(category);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    @AddCache
    public HashMap<String, Object> queryByOne(Integer  page,Integer rows) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Category category=new Category();
        category.setLevels("1");
        //设置当前页
        map.put("page",page);
        //查询总条数
        Integer count =categoryMapper.selectCount(category);
        //设置总条数
        map.put("records",count);
        //计算总页数 总页数 =总条数/每页展示条数
        Integer total=count%rows==0?count/rows:count/rows+1;
        //设置总页数 =总条数/每页展示条数
        map.put("total",total);
        List<Category> categories = categoryMapper.selectByRowBounds(category, new RowBounds((page - 1) * rows, rows));
        //返回的数据
        map.put("rows",categories);
        return map;
    }

    @Override
    @AddCache
    @Transactional(propagation = Propagation.SUPPORTS)
    public HashMap<String, Object>queryByTwo(String id,Integer  page,Integer rows) {

        HashMap<String, Object> map = new HashMap<String, Object>();
        Category category=new Category();
        category.setParentId(id);
        //设置当前页
        map.put("page",page);
        //查询总条数
        Integer count =categoryMapper.selectCount(category);
        //设置总条数
        map.put("records",count);
        //计算总页数 总页数 =总条数/每页展示条数
        Integer total=count%rows==0?count/rows:count/rows+1;
        //设置总页数 =总条数/每页展示条数
        map.put("total",total);
        List<Category> categories = categoryMapper.selectByRowBounds(category, new RowBounds((page - 1) * rows, rows));
        //返回的数据
        map.put("rows",categories);
        return map;
    }
    @AddLog("修改类别")
    @Override
    @DelCache
    public void update(Category category) {
        categoryMapper.updateByPrimaryKeySelective(category);
    }
    @AddLog("删除类别")
    @Override
    @DelCache
    public HashMap<String, Object>  delete(Category category) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        category = categoryMapper.selectByPrimaryKey(category);
        if("1".equals(category.getLevels())){
            Category category1=new Category();
            category1.setParentId(category.getId());
            List<Category> list = categoryMapper.select(category1);
            if (list.size()==0){
                categoryMapper.deleteByPrimaryKey(category.getId());
                map.put("massge","删除成功");
            }else {
                map.put("massge","存在二级分类，无法删除。");
            }
        }else {
            categoryMapper.deleteByPrimaryKey(category.getId());
            map.put("massge","删除二级类别成功");
        }
        return map;
    }

    @Override
    @AddCache
    @Transactional(propagation = Propagation.SUPPORTS)
    public HashMap<String, Object> queryAllCategory() {
        HashMap<String, Object> map = new HashMap<>();
        try {
            Category category = new Category();
            category.setLevels("1");
            List<Category> select = categoryMapper.select(category);
            category.setLevels(null);
            for (Category category1 : select) {
                category.setParentId(category1.getId());
                List<Category> list = categoryMapper.select(category);
                //category1.setCategoryList(list);
            }
            map.put("data",select);
            map.put("message","查询成功");
            map.put("status",100);

        } catch (Exception e) {
            e.printStackTrace();
            map.put("data",null);
            map.put("message","查询失败");
            map.put("status",104);
        }
        return map;
    }
}
