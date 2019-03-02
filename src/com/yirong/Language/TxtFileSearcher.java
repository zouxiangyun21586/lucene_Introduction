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
         //�洢�������ļ�  
         Directory indexFile = FSDirectory.open(Paths.get("F:/indexFile"));  
         //��ȡ����ȡ�����ļ�  ���򿪴洢λ��
         DirectoryReader ireader = DirectoryReader.open(indexFile);  
         //����������
         IndexSearcher searcher = new IndexSearcher(ireader);  
         //Ŀ�Ĳ����ַ���  ������SQL�����йؼ��ֲ�ѯ
         String queryStr ="make";  
         //����һ���ʷ���������������ѯ������ص�һ������  
         QueryParser parser = new QueryParser("content",new StandardAnalyzer());  
         Query query = parser.parse(queryStr);  
         TopDocs docs = searcher.search(query, 100);  
         System.out.println("һ�������������"+docs.totalHits+"��");  
         //�����ѯ�����Ϣ  
         for(ScoreDoc scoreDoc:docs.scoreDocs){  
             System.out.println("���Ϊ:"+scoreDoc.doc);  
             System.out.println("����Ϊ:"+scoreDoc.score);  
             Document document = searcher.doc(scoreDoc.doc);  
             System.out.println("·��Ϊ:"+document.get("path"));  
             System.out.println("�ļ���СΪ:"+document.get("fileSize"));  
             System.out.println("�ļ���Ϊ:"+document.get("filename"));  
             System.out.println("�����ɹ�����");  
         }     
     }   
 }  