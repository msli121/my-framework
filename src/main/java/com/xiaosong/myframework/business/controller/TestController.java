package com.xiaosong.myframework.business.controller;

import com.xiaosong.myframework.business.entity.Book;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/04
 */

@RestController
public class TestController {
    @GetMapping("/books")
    public ModelAndView getBooks() {
        List<Book> books = new ArrayList<>();
        Book book = new Book();
        book.setId(1);
        book.setName("《时间简史》");
        book.setAuthor("霍金");
        book.setPublicationDate(new Date());
        books.add(book);
        Book book1 = new Book();
        book1.setId(1);
        book1.setName("《西游记》");
        book1.setAuthor("吴承恩");
        book1.setPublicationDate(new Date());
        books.add(book1);
        ModelAndView mv = new ModelAndView();
        mv.addObject("books", books);
        mv.setViewName("books");
        return mv;
    }

    @GetMapping("/book")
    public Book getBook() {
        Book book = new Book();
        book.setId(1);
        book.setName("《时间简史》");
        book.setAuthor("霍金");
        book.setPublicationDate(new Date());
        return book;
    }
}
