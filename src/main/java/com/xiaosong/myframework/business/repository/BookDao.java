package com.xiaosong.myframework.business.repository;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import com.xiaosong.myframework.business.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/05
 */
public interface BookDao extends JpaRepository<Book, Integer> {

}
