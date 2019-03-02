package com.yirong.english;

import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class TxtFileSearcher {
	
	//LUCENE只分为二个步骤，存数据与取数据
	//ik 高亮显示
	
	 public static void main(String[] args) throws Exception {
	        Directory indexDir = FSDirectory.open( Paths.get("F:/indexDir"));
	        DirectoryReader directoryReader = DirectoryReader.open(indexDir);
	        
	        IndexSearcher searcher = new IndexSearcher(directoryReader);
	        

	       // Term term=new Term("content", "我");
	       // TermQuery luceneQuery = new TermQuery(term);
	        QueryParser parser = new QueryParser("content",new StandardAnalyzer());  
	        Query query = parser.parse("我是中国人");//new MatchAllDocsQuery();  
	        
	        
	        

	        
	        TopDocs topDocs = searcher.search(query, 100);
	        int docsNum = topDocs.totalHits;
	        ScoreDoc[] docs = topDocs.scoreDocs;
	        for (int i = 0; i < docsNum; i++) {
	            System.out .println("File路径: " + searcher.doc(docs[i].doc).get("name"));
	            
	        }
	    }
	 
//    public static void main(String[] args) throws Exception {
//        //创建Directory
//        Directory indexDir = FSDirectory.open( Paths.get("F:/indexDir"));
//        //创建DirectoryReader
//        DirectoryReader directoryReader = DirectoryReader.open(indexDir);
//        
//        //根据DirectoryReader创建IndexSearch
//        IndexSearcher searcher = new IndexSearcher(directoryReader);
//        
//        String queryStr ="51741298";
//        //按词条搜索，第一个参数字段名，第二个是搜索关键字
////      Term term = new Term("content", queryStr.toLowerCase());
////      TermQuery luceneQuery = new TermQuery(term);
////        
//        //构造一个词法分析器，并将查询结果返回到一个队列  
//        QueryParser parser = new QueryParser("content",new StandardAnalyzer());  
//        Query query = parser.parse(queryStr);  
//        
//        
//        //根据Serarcher搜索并且返回TopDocs
//        TopDocs topDocs = searcher.search(query, 100);
//        //topDocs.totalHits; 获取查询的总数
//        
//        //根据TopDocs获取ScoreDoc对象
//        ScoreDoc[] docs = topDocs.scoreDocs;
//        int docsNum = topDocs.totalHits;
//        for (int i = 0; i < docsNum; i++) {
//            System.out .println("File路径: " + searcher.doc(docs[i].doc).get("modified"));
//            
//        }
//    }
}
