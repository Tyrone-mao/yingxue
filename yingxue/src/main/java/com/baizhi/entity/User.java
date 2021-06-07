package com.baizhi.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "yx_user")
public class User implements Serializable {
    @Id
    @Excel(name = "ID")
    private String id;
    @Excel(name = "手机号")
    private String phone;

    @Column(name = "head_img")

    private String headImg;
    @Excel(name = "姓名")
    private String name;
    @Excel(name = "签名")
    private String sign;
    @Excel(name = "微信")
    private String wechat;
    @ExcelIgnore
    private String status;
    @Column(name = "regist_time")
    @Excel(name = "创建日期",format = "yyyy-MM-dd",width = 30)
    private Date registTime;

    private String sex;

    private String city;

}