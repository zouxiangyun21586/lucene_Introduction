package com.yirong.database;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;  
  /**
   * http://blog.csdn.net/quwenzhe/article/details/46516955
   * 数据库建立索引和查询索引
   * @author luolinghong
   *
   */
public class SearchDatabase {  
	/**
	 * 数据库连接
	 */
    private static Connection conn = null;  
    private static Statement stmt = null; 
    /**
     * 访问数据库返回的ResultSet 
     */
    private static ResultSet rs = null; 
    /**
     * 进行索引文件的写入
     */
    private static IndexWriter writer=null;
   /**
    * 索引保存的目录
    */
    private static IndexSearcher searcher = null;  
    /**
     * 创建分词器
     */
    private static Analyzer analyzer = new IKAnalyzer(true);  
  
    /** 
     * 获取数据库数据 
     * @param queryStr 需要检索的关键字 
     * @return 
     * @throws Exception 
     */  
    public List<User> getResult(String queryStr) throws Exception {  
        List<User> result = null;  
        conn = JdbcUtil.getConnection();  
        if (conn == null) {  
            throw new Exception("数据库连接失败！");  
        }  
        String sql = "select id,account from users";  
        try {  
            stmt = conn.createStatement();  
            rs = stmt.executeQuery(sql);  
            // 给数据库创建索引,此处执行一次，不要每次运行都创建索引  
            // 以后数据有更新可以后台调用更新索引  
            this.createIndex(rs);  
            TopDocs topDocs = this.search(queryStr);  
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;  
            result = this.addHits2List(scoreDocs);  
        } catch (Exception e) {  
            e.printStackTrace();  
            throw new Exception("数据库查询sql出错！ sql : " + sql);  
        } finally {  
            if (rs != null)  
                rs.close();  
            if (stmt != null)  
                stmt.close();  
            if (conn != null)  
                conn.close();  
        }  
        return result;  
    }  
  
    /** 
     * 为数据库检索数据创建索引 
     * @param 访问数据库返回的ResultSet 
     * @throws Exception 
     */  
    private void createIndex(ResultSet rs) throws Exception {  
        // 创建或打开索引  
        Directory directory = FSDirectory.open(Paths.get("F:/indexFile"));
        // 创建IndexWriter  
        IndexWriterConfig conf = new IndexWriterConfig(analyzer);  
        IndexWriter indexWriter = new IndexWriter(directory, conf);  
        // 遍历ResultSet创建索引  
        while (rs.next()) {  
            // 创建document并添加field  
            Document doc = new Document();  
            doc.add(new TextField("id", rs.getString("id"), Field.Store.YES));  
            doc.add(new TextField("account", rs.getString("account"),  
                    Field.Store.YES));  
            // 将doc添加到索引中  
            indexWriter.addDocument(doc);  
        }  
        indexWriter.commit();  //indexWriter必须要提交或者关闭，否则将写入不进去
        indexWriter.close();  
        directory.close();  
    }  
  
    
    /** 
     * 检索索引 
     * @param queryStr 需要检索的关键字 
     * @return 
     * @throws Exception 
     */  
    private TopDocs search(String queryStr) throws Exception {  
        //创建或打开索引目录  
        Directory directory = FSDirectory.open(Paths.get("F:/indexFile")); 
        DirectoryReader reader = DirectoryReader.open(directory);  
        if (searcher == null) {  
            searcher = new IndexSearcher(reader);  
        }  
        //使用查询解析器创建Query  
        QueryParser parser = new QueryParser( "account", analyzer);  
        Query query = parser.parse(queryStr);  
        //从索引中搜索得到排名前10的文档  
        TopDocs topDocs = searcher.search(query, 10);  
        return topDocs;  
    }  
  
    /** 
     * 将检索结果添加到List中 
     * @param scoreDocs 
     * @return 
     * @throws Exception 
     */  
    private List<User> addHits2List(ScoreDoc[] scoreDocs) throws Exception {  
        List<User> listBean = new ArrayList<User>();  
        User bean = null;  
        for (int i = 0; i < scoreDocs.length; i++) {  
            int docId = scoreDocs[i].doc;  
            Document doc = searcher.doc(docId);  
            bean = new User();  
            bean.setId(doc.get("id"));  
            bean.setAccount(doc.get("account"));
            listBean.add(bean);  
        }  
        return listBean;  
    }  
  
    public static void main(String[] args) {  
        SearchDatabase logic = new SearchDatabase();  
        try {  
            Long startTime = System.currentTimeMillis();  
            List<User> result = logic.getResult("account");  
            int i = 0;  
            for (User bean : result) {  
                if (i == 10)  
                    break;  
                System.out.println("name :" + bean.getClass().getName()  
                        + " : id =" + bean.getId() + " :account= "  
                        + bean.getAccount());  
                i++;  
            }  
            System.out.println("size : " + result.size());  
            Long endTime = System.currentTimeMillis();  
            System.out.println("查询所花费的时间为：" + (endTime - startTime) / 1000); 
            
            //查询完成后,将索引全部删除，方便每次的创建和查询
            try {
    			Directory directory = FSDirectory.open(Paths.get("F:/indexFile"));
    			Analyzer analyzer = new StandardAnalyzer();
    			IndexWriterConfig config = new IndexWriterConfig(analyzer);
    			writer = new IndexWriter(directory, config);
    			writer.deleteAll(); //删除全部的索引
    			writer.commit(); 
    			System.out.println("索引删除成功");
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
        } catch (Exception e) {  
            e.printStackTrace();  
            System.out.println(e.getMessage());  
        }  
    }  
}  