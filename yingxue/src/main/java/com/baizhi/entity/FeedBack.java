package com.baizhi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "yx_feedback")
public class FeedBack implements Serializable {
    @Id
    private String id;

    private String title;

    private String content;

    private Date feedTime;

    private String userId;
    //关系属性
    @Transient
    private User user;


}