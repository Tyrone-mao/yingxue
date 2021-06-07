package com.baizhi.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "yx_category")
public class Categorys {
    @Id
    @Excel(name = "ID" )
    private String id;
    @Excel(name = "类别名")
    @Column(name = "cate_name")
    private String cateName;
    @Excel(name = "级别")
    private String levels;
    @Column(name = "parent_id")
    @Excel(name = "父类ID")
    private String parentId;


}