package com.test.app.service;

import com.test.app.entity.Book;

import java.util.List;

/**
 * Created by zz on 2017/7/6.
 */
public interface IBookService {

    public List<Book> selectAllBook();
}
