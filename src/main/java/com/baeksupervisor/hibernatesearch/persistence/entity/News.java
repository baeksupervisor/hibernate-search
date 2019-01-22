package com.baeksupervisor.hibernatesearch.persistence.entity;

import lombok.Data;

@Data
@javax.persistence.Entity(name = "news")
@org.hibernate.search.annotations.Indexed   // 인덱싱해야할 엔티티
public class News {

    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    @javax.persistence.Column(name = "news_no")
    private Integer newsroomNo;

    @javax.persistence.Column(name = "link")
    private String link;

    @javax.persistence.Column(name = "title")
    @org.hibernate.search.annotations.Field // 인덱싱해야할 필드
    private String title;

    @javax.persistence.Column(name = "description", length = 1024)
    @org.hibernate.search.annotations.Field // 인덱싱해야할 필드
    private String description;

    @javax.persistence.Column(name = "image")
    private String image;
}