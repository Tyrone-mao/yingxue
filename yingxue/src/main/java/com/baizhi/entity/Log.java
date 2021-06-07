package com.baizhi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "yx_log")
public class Log {
    @Id
    private String id;
    @Column(name = "admin_name")
    private String adminName;
    @Column(name = "method_name")
    private String methodName;
    @Column(name = "option_time")
    private Date optionTime;

    private String statu;


}