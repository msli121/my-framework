package com.xiaosong.myframework.business.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/04
 */
@Data
public class Book {
    private Integer id;
    private String name;
    private String author;
    private Date publicationDate;
}
