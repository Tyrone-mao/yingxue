package com.baizhi.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "yx_category")
public class Category implements Serializable {
    @Id
    @Excel(name = "ID",needMerge = true)
    private String id;
    @Excel(name = "类别名",needMerge = true)
    @Column(name = "cate_name")
    private String cateName;
    @Excel(name = "级别",needMerge = true)
    private String levels;
    @Column(name = "parent_id")
    @ExcelIgnore
    private String parentId;
    @ExcelCollection(name = "二级类别")
    private List<Categorys> categoryList;

}