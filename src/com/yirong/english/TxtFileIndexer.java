package com.yirong.english;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


public class TxtFileIndexer {
    public static void main(String[] args) throws IOException {
//        String dirPath = "F:/FilePath";
//        String indexPath = "F:/indexDir";
//        createIndex(dirPath,indexPath,true);
    	
    	
    	//�õ�һ��lucene��������������ŵ�Ŀ¼��
    	Path docDirPath = Paths.get("F:/indexDir", new String[0]);
    	 
        Directory directory = FSDirectory.open(docDirPath);
        
        //����Analyzer�����зִʣ�֮�󴴽�IndexWriter��indexWriter������
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        
        IndexWriter indexWriter = new IndexWriter(directory,indexWriterConfig);
        
        
        Document document = new Document();
        StringField name = new StringField("name", "������3333", Field.Store.YES);
        
        
        StringField add = new StringField("add", "����ʡ������3333", Field.Store.YES);
        
        IntField age = new IntField("age", 40, Field.Store.YES);
        
        Field content = new TextField("content", "�����й���3333",Field.Store.YES);
        
        //�ļ���ȡ�����ֲ�ʽ���ȱ���
        
        document.add(age);
        document.add(content);
        document.add(name);
        document.add(add);
        
        //elasticsearch
        //solr
        //update set name = "aa" where name = "bbb";
        //���û�з����������ݣ��Ͳ�������޸ģ�������Ӳ���
        indexWriter.updateDocument(new Term("name", "������2222"), document);
       // indexWriter.addDocument(document);
        indexWriter.commit();//�ύ
        indexWriter.close();//�ر�

//    	List<String> list = getAnalyseResult("�����й���", new StandardAnalyzer());
//    	for (String string : list) {
//			System.out.println(string);
//		}
    }
    
    
    public static InputStream Str2Inputstr(String inStr)  
    {  
        try  
        {  
            // return new ByteArrayInputStream(inStr.getBytes());  
            // return new ByteArrayInputStream(inStr.getBytes("UTF-8"));  
            return new StringBufferInputStream(inStr);  
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
        return null;  
    }  
    
    public static  List<String> getAnalyseResult(String analyzeStr, Analyzer analyzer) {  
        List<String> response = new ArrayList<String>();  
        TokenStream tokenStream = null;  
        try {  
            tokenStream = analyzer.tokenStream("content", new StringReader(analyzeStr));  
            CharTermAttribute attr = tokenStream.addAttribute(CharTermAttribute.class);  
            tokenStream.reset();  
            while (tokenStream.incrementToken()) {  
                response.add(attr.toString());  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (tokenStream != null) {  
                try {  
                    tokenStream.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return response;  
    }  

    /**
     * ��������
     * @param dirPath ��Ҫ�������ļ�Ŀ¼
     * @param indexPath �������Ŀ¼
     * @param createOrAppend
     * @throws IOException
     */
    public static void createIndex(String dirPath,String indexPath,boolean createOrAppend) throws IOException {
        long startTime = System.currentTimeMillis();
        
        
        //����Dierctory
        //�õ�һ��lucene��������������ŵ�Ŀ¼��
        Directory directory = FSDirectory.open(Paths.get(indexPath, new String[0]));
        
        Path docDirPath = Paths.get(dirPath, new String[0]);
        
        //����Analyzer�����зִʣ�֮�󴴽�IndexWriter��indexWriter������
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        
        if(createOrAppend){
        	//������׷��
            indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        }else{
            indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        }
        IndexWriter indexWriter = new IndexWriter(directory,indexWriterConfig);
        indexDocs(indexWriter,docDirPath);
        indexWriter.close();
        System.out.println(System.currentTimeMillis()-startTime);
    }

    /**
     * �����ļ�·�����ļ����ݽ�������
     * �����Ŀ¼��ɨ��Ŀ¼�µ��ļ�
     * @param indexWriter
     * @param path
     * @throws IOException
     */
    public static void indexDocs(final IndexWriter indexWriter,Path path) throws IOException {
    	//indexWrite������������Ķ���path����������������������ԴĿ¼
        if(Files.isDirectory(path,new LinkOption[0])){
            //Ŀ¼
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    indexDoc(indexWriter, file, System.currentTimeMillis());
                    return FileVisitResult.CONTINUE;
                }
            });
            
        } else {
            indexDoc(indexWriter, path, Files.getLastModifiedTime(path,new LinkOption[0]).toMillis());
        }
    }

    /**
     * �����ļ�·�����ļ����ݽ�������
     * @param indexWriter
     * @param path
     * @throws IOException
     */
    public static void indexDoc(IndexWriter indexWriter,Path path,long lastModified) throws IOException {
        //����Document����
    	Document document = new Document();
        Field pathField = new StringField("path",path.toString(),Field.Store.YES);
        //ΪDocument���Field
        document.add(pathField);
        
        
        Field lastModifiedField = new LongField("modified",lastModified,Field.Store.YES);
        document.add(lastModifiedField);
        
        
        
      //������������FieldType ���Ƕ�����TextField����Ϊ��̬����
        Field contentField = new TextField("content",new BufferedReader(new InputStreamReader(Files.newInputStream(path, new OpenOption[0]), StandardCharsets.UTF_8)
        ));
        document.add(contentField);
        if(indexWriter.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE){
        	//ͨ��IndexWriter����ĵ���������
            indexWriter.addDocument(document);
        }else{
        	//path = 
        	//modefied =  
        	//content = 
        	// update set path = ? ,modefied = ? ,content = ? where path = ?
            indexWriter.updateDocument(new Term("path", path.toString()), document);
        }
        indexWriter.commit(); //�ύ��������
    }

}