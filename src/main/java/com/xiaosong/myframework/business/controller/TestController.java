package com.xiaosong.myframework.business.controller;

import org.springframework.web.bind.annotation.*;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/04
 */

@RestController
public class TestController {

//    @GetMapping("/books")
//    public ModelAndView getBooks() {
//        List<Book> books = new ArrayList<>();
//        Book book = new Book();
//        book.setId(1);
//        book.setName("《时间简史》");
//        book.setAuthor("霍金");
//        book.setPublicationDate(new Date());
//        books.add(book);
//        Book book1 = new Book();
//        book1.setId(1);
//        book1.setName("《西游记》");
//        book1.setAuthor("吴承恩");
//        book1.setPublicationDate(new Date());
//        books.add(book1);
//        ModelAndView mv = new ModelAndView();
//        mv.addObject("books", books);
//        mv.setViewName("books");
//        return mv;
//    }

//    @GetMapping("/book")
//    public Book getBook() {
//        Book book = new Book();
//        book.setId(1);
//        book.setName("《时间简史》");
//        book.setAuthor("霍金");
//        book.setPublicationDate(new Date());
//        return book;
//    }

    @PostMapping("/book")
    public String addBook(String name) {
        return "receive" + name;
    }

    @DeleteMapping("/book/{id}")
    public String deleteBookById(@PathVariable Integer id) {
        return String.valueOf(id);
    }

    @GetMapping("/admin/hello")
    public String admin() {
        return "Hello, admin";
    }

    @GetMapping("/user/hello")
    public String user() {
        return "Hello, user";
    }

    @GetMapping("/db/hello")
    public String dba() {
        return "Hello, dba!";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, world!";
    }
}
