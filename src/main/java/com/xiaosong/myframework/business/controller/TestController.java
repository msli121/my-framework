package com.xiaosong.myframework.business.controller;

import com.xiaosong.myframework.business.entity.ApiResult;
import com.xiaosong.myframework.business.entity.Book;
import com.xiaosong.myframework.business.entity.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.Objects;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/04
 */

@RestController
public class TestController {

    @PostMapping(value = "/api/login")
    public ApiResult login(@RequestBody User requestUser) {
        // 对 html 标签进行转义，防止 XSS 攻击
        String username = requestUser.getUsername();
        username = HtmlUtils.htmlEscape(username);

        if (!Objects.equals("admin", username) || !Objects.equals("123456", requestUser.getPassword())) {
            String message = "账号密码错误";
            System.out.println("test");
            return new ApiResult(400);
        } else {
            return new ApiResult(200);
        }
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
