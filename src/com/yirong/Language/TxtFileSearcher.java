package com.yirong.Language;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;  
   
 public class TxtFileSearcher {   
     public static void main(String[] args) throws Exception{   
         //存储了索引文件  
         Directory indexFile = FSDirectory.open(Paths.get("F:/indexFile"));  
         //读取器读取索引文件  ，打开存储位置
         DirectoryReader ireader = DirectoryReader.open(indexFile);  
         //创建搜索器
         IndexSearcher searcher = new IndexSearcher(ireader);  
         //目的查找字符串  ，累死SQL，进行关键字查询
         String queryStr ="make";  
         //构造一个词法分析器，并将查询结果返回到一个队列  
         QueryParser parser = new QueryParser("content",new StandardAnalyzer());  
         Query query = parser.parse(queryStr);  
         TopDocs docs = searcher.search(query, 100);  
         System.out.println("一共搜索到结果："+docs.totalHits+"条");  
         //输出查询结果信息  
         for(ScoreDoc scoreDoc:docs.scoreDocs){  
             System.out.println("序号为:"+scoreDoc.doc);  
             System.out.println("评分为:"+scoreDoc.score);  
             Document document = searcher.doc(scoreDoc.doc);  
             System.out.println("路径为:"+document.get("path"));  
             System.out.println("文件大小为:"+document.get("fileSize"));  
             System.out.println("文件名为:"+document.get("filename"));  
             System.out.println("搜索成功！！");  
         }     
     }   
 }  