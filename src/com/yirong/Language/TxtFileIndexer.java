package com.yirong.Language;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;  
  
public class TxtFileIndexer {   
     public static void main(String[] args) throws Exception{ 
    	 
     File dataDir = new File("F:/filePath");   
     //ȷ�������ļ��洢��λ�ã�
     //��һ�ַ�ʽ��Directory indexDir = FSDirectory.open(Paths.get("F:/index")); �����ļ��洢
     //�ڶ��ַ�ʽ��Directory directory=new RAMDirectory(); �ڴ�洢
     Directory indexDir = FSDirectory.open(Paths.get("F:/indexFile"));
     Analyzer luceneAnalyzer = new StandardAnalyzer(); //����һ���ִ���ʵ��  
     //����IndexWriter,���������ļ���д��
     //�����IndexWriterConfig�Ƕ�IndexWriter�����ã����а�������������
     IndexWriterConfig config = new IndexWriterConfig(luceneAnalyzer);  
     IndexWriter indexWriter = new IndexWriter(indexDir,config);   
     File[] dataFiles  = dataDir.listFiles(); //�õ����е��ļ� 
     long startTime = new Date().getTime();   
     for(int i = 0; i < dataFiles.length; i++){   
          if(dataFiles[i].isFile() && dataFiles[i].getName().endsWith(".txt")){  
               System.out.println("Indexing file " + dataFiles[i].getCanonicalPath()); //���ؾ���·��  
               Document document = new Document(); //ÿһ���ļ������һ��document����   
               Reader txtReader = new FileReader(dataFiles[i]);   
               Field field1 = new StringField("path",dataFiles[i].getPath(),Store.YES);  
               Field field2 = new TextField("content",txtReader);  
               Field field3 = new LongField("fileSize", dataFiles[i].length(), Store.YES);   
               Field field4 = new TextField("filename",dataFiles[i].getName(),Store.YES);  
               document.add(field1);  
               document.add(field2);  
               document.add(field3);  
               document.add(field4);  
               indexWriter.addDocument(document);  //�ύ�������ݣ�����һ������  
          }   
     }  
     
     //������indexWriter����Ҫ�ص�����commit����Ȼ�����ݽ�д�벻��ȥ
     indexWriter.close();   //�ر�IndexWriter
     long endTime = new Date().getTime();   
     System.out.println("It takes " + (endTime - startTime)   
         + " milliseconds to create index for the files in directory "  
         + dataDir.getPath());          
     }   
}  