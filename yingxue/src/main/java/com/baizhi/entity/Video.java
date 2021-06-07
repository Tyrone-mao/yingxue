package com.baizhi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "yx_video")
//@Document(indexName = "yingx",type = "video")
public class Video implements Serializable {
    @Id
    private String id;
   //@Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String title;
   // @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String description;
    @Column(name = "video_path")
  //  @Field(type = FieldType.Text)
    private String videoPath;
    @Column(name = "civer_path")
   // @Field(type = FieldType.Text)
    private String civerPath;
   // @Field(type = FieldType.Date)
    @Column(name = "upload_time")
    private Date uploadTime;
    @Column(name = "cate_id")
   // @Field(type = FieldType.Text)
    private String cateId;
    @Column(name = "group_id")
   // @Field(type = FieldType.Text)
    private String groupId;
    @Column(name = "user_id")
   // @Field(type = FieldType.Text)
    private String userId;
    @Transient
   // @Field(type = FieldType.Integer)
    private Integer likes;
    @Transient
   // @Field(type = FieldType.Integer)
    private Integer counts;

}