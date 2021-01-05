package com.xiaosong.myframework.business.service;

import com.xiaosong.myframework.business.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/05
 */
@Service("bookService")
public class BookService {
    @Autowired
    BookDao bookDao;

    public int addBook(Book book) {
        return 1;
    }
}
