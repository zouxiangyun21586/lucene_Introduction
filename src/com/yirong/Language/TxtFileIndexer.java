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
     //确定索引文件存储的位置：
     //第一种方式：Directory indexDir = FSDirectory.open(Paths.get("F:/index")); 本地文件存储
     //第二种方式：Directory directory=new RAMDirectory(); 内存存储
     Directory indexDir = FSDirectory.open(Paths.get("F:/indexFile"));
     Analyzer luceneAnalyzer = new StandardAnalyzer(); //创建一个分词器实例  
     //创建IndexWriter,进行索引文件的写入
     //这里的IndexWriterConfig是对IndexWriter的配置，其中包含了俩个参数
     IndexWriterConfig config = new IndexWriterConfig(luceneAnalyzer);  
     IndexWriter indexWriter = new IndexWriter(indexDir,config);   
     File[] dataFiles  = dataDir.listFiles(); //得到所有的文件 
     long startTime = new Date().getTime();   
     for(int i = 0; i < dataFiles.length; i++){   
          if(dataFiles[i].isFile() && dataFiles[i].getName().endsWith(".txt")){  
               System.out.println("Indexing file " + dataFiles[i].getCanonicalPath()); //返回绝对路径  
               Document document = new Document(); //每一个文件都变成一个document对象   
               Reader txtReader = new FileReader(dataFiles[i]);   
               Field field1 = new StringField("path",dataFiles[i].getPath(),Store.YES);  
               Field field2 = new TextField("content",txtReader);  
               Field field3 = new LongField("fileSize", dataFiles[i].length(), Store.YES);   
               Field field4 = new TextField("filename",dataFiles[i].getName(),Store.YES);  
               document.add(field1);  
               document.add(field2);  
               document.add(field3);  
               document.add(field4);  
               indexWriter.addDocument(document);  //提交创建数据，就是一个索引  
          }   
     }  
     
     //在这里indexWriter必须要关掉或者commit。不然，数据将写入不进去
     indexWriter.close();   //关闭IndexWriter
     long endTime = new Date().getTime();   
     System.out.println("It takes " + (endTime - startTime)   
         + " milliseconds to create index for the files in directory "  
         + dataDir.getPath());          
     }   
}  