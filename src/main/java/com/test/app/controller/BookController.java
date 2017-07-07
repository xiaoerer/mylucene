package com.test.app.controller;

import com.test.app.entity.Book;
import com.test.app.service.IBookService;
import com.test.base.lucene.IndexManager;
import com.test.base.lucene.LuceneSearch;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by zz on 2017/7/6.
 */
@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    private IBookService bookService;

    @Autowired
    LuceneSearch luceneSearch;
    @Autowired
    IndexManager indexManager;

    @RequestMapping("/list")
    public ModelAndView list() throws ParseException {
        ModelAndView mav=new ModelAndView();
        List listbook=bookService.selectAllBook();
        mav.addObject("booklist",listbook);
        mav.setViewName("home/test");
        luceneSearch.indexSearch();
        return mav;
    }

    @RequestMapping("/create")
    public ModelAndView create(){
        ModelAndView mav=new ModelAndView();
//        List listbook=bookService.selectAllBook();
//        mav.addObject("booklist",listbook);
        mav.setViewName("home/test");

        List<Book> listbook=bookService.selectAllBook();
        indexManager.createIndex(listbook);

        return mav;
    }
}
