package com.test.base.lucene;

import com.test.app.entity.Book;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zz on 2017/7/10.
 * 书本Book的索引类
 * 正式
 */
public class LuceneIndex {

    private Directory dir=null;

    /**
     * 获取IndexWriter实例
     * @return
     * @throws Exception
     */
    private IndexWriter getWriter() throws Exception{

        /**
         * 生成的索引我放在C://index/test，可以根据自己的需要放在具体位置
         */
        dir= FSDirectory.open(Paths.get("C://index/test"));
        SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();
        IndexWriterConfig iwc=new IndexWriterConfig(analyzer);
        IndexWriter writer=new IndexWriter(dir,iwc);
        return writer;
    }

    /**
     * 添加博客索引
     * @param book
     * @throws Exception
     */
    public void addIndex(Book book) throws Exception {
        IndexWriter writer=getWriter();
        Document doc=new Document();
        doc.add(new StringField("id",String.valueOf(book.getId()),Field.Store.YES));
        /**
         * yes是会将数据存进索引，如果查询结果中需要将记录像是出去就要存进去，
         * 如果查询结果只是显示标题直接的就可以不用存，而且内容过长不建议存进去
         * 使用TextField类可以用于查询的
         */
        doc.add(new TextField("name",book.getName(), Field.Store.YES));
        doc.add(new TextField("description",book.getDescription(), Field.Store.YES));
        writer.addDocument(doc);
        writer.close();
    }

    /**
     * 更新博客索引
     * @param book
     */
    public void updateIndex(Book book) throws Exception {
        IndexWriter writer=getWriter();
        Document doc=new Document();
        doc.add(new StringField("id",String.valueOf(book.getId()), Field.Store.YES));
        doc.add(new TextField("username", book.getName(), Field.Store.YES));
        doc.add(new TextField("description",book.getDescription(), Field.Store.YES));
        writer.updateDocument(new Term("id",String.valueOf(book.getId())),doc);
        writer.close();
    }

    /**
     * 删除指定博客的索引
     * @param bookId
     * @throws Exception
     */
    public void deleteIndex(String bookId) throws Exception {
        IndexWriter writer=getWriter();
        writer.deleteDocuments(new Term("id",bookId));
        writer.forceMergeDeletes();//强制删除
        writer.commit();
        writer.close();
    }

    public List<Book> searchBook(String q) throws IOException, ParseException, InvalidTokenOffsetsException {
        dir=FSDirectory.open(Paths.get("C://index/test"));
        IndexReader reader= DirectoryReader.open(dir);
        IndexSearcher searcher=new IndexSearcher(reader);
        BooleanQuery.Builder booleanQuery=new BooleanQuery.Builder();

        SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();
        /**
         * username和description就是我们需要进行查找的两个字段
         * 同时在存放索引的时候要使用TextField类进行存放。
         */
        QueryParser parser=new QueryParser("name",analyzer);
        Query query=parser.parse(q);
        QueryParser parser2=new QueryParser("description",analyzer);
        Query query2=parser2.parse(q);
        booleanQuery.add(query, BooleanClause.Occur.SHOULD);
        booleanQuery.add(query2, BooleanClause.Occur.SHOULD);
        TopDocs hits=searcher.search(booleanQuery.build(),100);
        QueryScorer scorer=new QueryScorer(query);
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
        /**
         * 这里可以根据自己的需要来自定义查找关键字高亮时的样式。
         */
        SimpleHTMLFormatter simpleHTMLFormatter=new SimpleHTMLFormatter("<b><font color='red'>","</font></b>");
        Highlighter highlighter=new Highlighter(simpleHTMLFormatter, scorer);
        highlighter.setTextFragmenter(fragmenter);
        List<Book> bookList=new LinkedList<Book>();
        for (ScoreDoc scoreDoc:hits.scoreDocs) {
            Document doc=searcher.doc(scoreDoc.doc);
            Book book=new Book();
            book.setId(Integer.parseInt(doc.get("id")));
            book.setName(doc.get("description"));
            String name=doc.get("name");
            String description=doc.get("description");
            if(name!=null){
                TokenStream tokenStream=analyzer.tokenStream("name",new StringReader(name));
                String husename=highlighter.getBestFragment(tokenStream,name);
                book.setName(husename);
            }
            if(description!=null){
                TokenStream tokenStream=analyzer.tokenStream("description",new StringReader(description));
                String hContent=highlighter.getBestFragment(tokenStream,description);
                if(description.length()<=200){
                    book.setDescription(description);
                }
            }
            bookList.add(book);
        }
        return bookList;
    }


}
