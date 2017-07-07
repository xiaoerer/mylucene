package com.test.base.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileSystems;

/**
 * Created by zz on 2017/7/6.
 */
@Service
public class LuceneSearch {

    private void doSearch(Query query){
        try {
            //          File indexFile = new File("D:\\Lpj\\Eclipse\\lecencedemo\\");
            //          Directory directory = FSDirectory.open(indexFile);
            //1、创建Directory
            //JDK 1.7 以后 open只能接收Path
            Directory directory= FSDirectory.open(FileSystems.getDefault().getPath("C:\\index\\test"));
            IndexReader reader= DirectoryReader.open(directory);
            IndexSearcher searcher=new IndexSearcher(reader);
            //通过searcher来搜索索引库
            //第二个参数：指定需要显示的顶部记录的N条
            TopDocs topDocs=searcher.search(query,10);

            //根据查询条件匹配出的记录总数
            int count=topDocs.totalHits;
            System.out.println("匹配出的记录总数："+count);
            //根据查询条件匹配出的记录
            ScoreDoc[] scoreDocs=topDocs.scoreDocs;

            for (ScoreDoc scoreDoc: scoreDocs) {
                //获取文档id
                int docId=scoreDoc.doc;

                //通过ID获取文档
                Document doc=searcher.doc(docId);
                System.out.println("书本id："+doc.get("id"));
                System.out.println("书本名称："+doc.get("name"));
                System.out.println("书本价格："+doc.get("price"));
                System.out.println("书本图片："+doc.get("pic"));
                System.out.println("============================");
            }

            //关闭资源
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void indexSearch() throws ParseException {
        //创建query对象
        Analyzer analyzer=new StandardAnalyzer();
        //使用QueryParser搜索时，需要指定分词器，搜索时的分词器要和索引时的分词器一致
        //第一个参数：默认搜索的域的名称
        QueryParser parser=new QueryParser("description",analyzer);

        //通过queryparser来创建query对象
        //参数：输入的lucene的查询语句（关键字一定要大写）
        Query query=parser.parse("description:一 AND 二");

        doSearch(query);
    }

}
