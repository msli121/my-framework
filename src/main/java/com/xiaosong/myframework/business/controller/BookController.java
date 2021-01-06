//package com.xiaosong.myframework.business.controller;
//
//import com.xiaosong.myframework.business.entity.Book;
//import com.xiaosong.myframework.business.service.BookService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Random;
//
///**
// * @Description
// * @Author msli
// * @Date 2021/01/05
// */
//@RestController
//public class BookController {
//    @Autowired
//    BookService bookService;
//
//    @GetMapping("/findAll")
//    public List<Book> findAll() {
//        PageRequest pageRequest = PageRequest.of(0, 3);
//        Page<Book> page = bookService.getBookByPage(pageRequest);
//        System.out.println(page.getTotalElements());
//        return page.getContent();
//    }
//
//    @GetMapping("/add")
//    public String add() {
//        Random random = new Random();
//        Book book = new Book();
//        book.setAuthor("xiaosong");
//        book.setName("test-" + random.nextInt(100));
//        book.setPrice(random.nextFloat() * 100 + 20);
//        book.setPublicationDate(new Date());
//        bookService.addBook(book);
//        System.out.println("----------------" + book.getId() + " 保存成功！");
//        return String.format("-----------%s:保存成功", book.getId());
//    }
//}
