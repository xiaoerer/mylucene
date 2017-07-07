package com.test.base.lucene;

import com.test.app.entity.Book;
import com.test.app.mapper.BookMapper;
import com.test.app.service.IBookService;
import com.test.app.service.impl.BookServiceImpl;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zz on 2017/7/7.
 */
@Service
public class IndexManager {

    public void createIndex(List<Book> list){

        /*IBookService bookService=new BookServiceImpl();
        List<Book> list= bookService.selectAllBook();*/

        List<Document> docList=new ArrayList<Document>();
        Document document;
        for (Book book:list) {
            document=new Document();
            // store:如果是yes，则说明存储到文档域中
            Field id=new TextField("id",Integer.toString(book.getId()),Field.Store.YES);
            Field name=new TextField("name",book.getName(),Field.Store.YES);
            Field price=new TextField("price", book.getPrice().toString(),Field.Store.YES);
            Field pic=new TextField("pic",book.getPic(),Field.Store.YES);
            Field description=new TextField("description",book.getDescription(),Field.Store.YES);

            docList.add(document);
        }

        //JDK1.7以后 open只能接收Path//////////////////////////
        //创建分词器，标准分词器
        Analyzer analyzer=new StandardAnalyzer();

        //创建IndexWriter
        // IndexWriterConfig cfg = new IndexWriterConfig(Version.LUCENE_6_5_0,analyzer);
        IndexWriterConfig cfg=new IndexWriterConfig(analyzer);

        //指定索引库的地址
        //         File indexFile = new File("D:\\L\a\Eclipse\\lecencedemo\\");
        //         Directory directory = FSDirectory.open(indexFile);
        try {
            Directory directory= FSDirectory.open(FileSystems.getDefault().getPath("C:\\index\\test"));
            IndexWriter writer=new IndexWriter(directory,cfg);
            writer.deleteAll();//清除以前的index
            //通过IndexWriter对象将Document写入到索引库中
            for (Document doc:docList) {
                writer.addDocument(doc);
            }
            //关闭writer
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
