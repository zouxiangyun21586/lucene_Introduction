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
	
	//LUCENEֻ��Ϊ�������裬��������ȡ����
	//ik ������ʾ
	
	 public static void main(String[] args) throws Exception {
	        Directory indexDir = FSDirectory.open( Paths.get("F:/indexDir"));
	        DirectoryReader directoryReader = DirectoryReader.open(indexDir);
	        
	        IndexSearcher searcher = new IndexSearcher(directoryReader);
	        

	       // Term term=new Term("content", "��");
	       // TermQuery luceneQuery = new TermQuery(term);
	        QueryParser parser = new QueryParser("content",new StandardAnalyzer());  
	        Query query = parser.parse("�����й���");//new MatchAllDocsQuery();  
	        
	        
	        

	        
	        TopDocs topDocs = searcher.search(query, 100);
	        int docsNum = topDocs.totalHits;
	        ScoreDoc[] docs = topDocs.scoreDocs;
	        for (int i = 0; i < docsNum; i++) {
	            System.out .println("File·��: " + searcher.doc(docs[i].doc).get("name"));
	            
	        }
	    }
	 
//    public static void main(String[] args) throws Exception {
//        //����Directory
//        Directory indexDir = FSDirectory.open( Paths.get("F:/indexDir"));
//        //����DirectoryReader
//        DirectoryReader directoryReader = DirectoryReader.open(indexDir);
//        
//        //����DirectoryReader����IndexSearch
//        IndexSearcher searcher = new IndexSearcher(directoryReader);
//        
//        String queryStr ="51741298";
//        //��������������һ�������ֶ������ڶ����������ؼ���
////      Term term = new Term("content", queryStr.toLowerCase());
////      TermQuery luceneQuery = new TermQuery(term);
////        
//        //����һ���ʷ���������������ѯ������ص�һ������  
//        QueryParser parser = new QueryParser("content",new StandardAnalyzer());  
//        Query query = parser.parse(queryStr);  
//        
//        
//        //����Serarcher�������ҷ���TopDocs
//        TopDocs topDocs = searcher.search(query, 100);
//        //topDocs.totalHits; ��ȡ��ѯ������
//        
//        //����TopDocs��ȡScoreDoc����
//        ScoreDoc[] docs = topDocs.scoreDocs;
//        int docsNum = topDocs.totalHits;
//        for (int i = 0; i < docsNum; i++) {
//            System.out .println("File·��: " + searcher.doc(docs[i].doc).get("modified"));
//            
//        }
//    }
}
