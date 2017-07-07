package com.test.app.service.impl;

import com.test.app.mapper.BookMapper;
import com.test.app.entity.Book;
import com.test.app.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zz on 2017/7/6.
 */
@Service("bookService")
public class BookServiceImpl implements IBookService {

    @Autowired
    private BookMapper bookMapper;

    public List<Book> selectAllBook() {
        return bookMapper.selectAllBook();
    }
}
