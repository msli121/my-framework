package com.xiaosong.myframework.business.repository;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import com.xiaosong.myframework.business.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/05
 */
public interface BookDao extends JpaRepository<Book, Integer> {
    List<Book> getBooksByAuthorStartingWith(String author);

    List<Book> getBooksByPriceGreaterThan(Float price);

    @Query(value = "select * from t_book where id = (select max(id) from t_book)", nativeQuery = true)
    Book getMaxIdBook();

    @Query("select b from t_book b where b.id < ?2 and b.name like %?1%")
    List<Book> getBookByIdAndName(String name, Integer id);
}
