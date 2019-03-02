package com.yirong.indexcurd;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * ������������
 * �����ĸ��¡�ɾ������ѯ����������ҳ
 * @author Administrator
 * http://www.cnblogs.com/qibotean/articles/4955228.html
 */
public class Lucene5Utils {
	private static Directory directory = null ; //��������
	private static IndexWriter indexWriter = null ; //����IndexWriter
	private static IndexReader indexReader = null ; //����IndexReader
	private static IndexSearcher search = null ; //����searcher
	private static Date[] dates=null ; 
	
	private static String[] ids={"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};  
	private static String[] emails={"aa@aa.org","cc@cc.org","dd@dd.org","bb@bb.org","ee@ee.org","ff@ff.org","cc@cc.org","dd@dd.org","bb@bb.org","ee@ee.org","ff@ff.org","cc@cc.org","dd@dd.org","bb@bb.org","ee@ee.org","ff@ff.org","cc@cc.org","dd@dd.org","bb@bb.org","ee@ee.org","ff@ff.org","cc@cc.org","dd@dd.org","bb@bb.org","ee@ee.org","ff@ff.org","cc@cc.org","dd@dd.org","bb@bb.org","ee@ee.org","ff@ff.org"};  
	private static String[] contents={"welcome to you","hello a boy","hello a girl","how are you","goog luck","oh shit","hello a boy","hello a girl","how are you","goog luck","oh shit","hello a boy","hello a girl","how are you","goog luck","oh shit","hello a boy","hello a girl","how are you","goog luck","oh shit","hello a boy","hello a girl","how are you","goog luck","oh shit","hello a boy","hello a girl","how are you","goog luck","oh shit"};  
	private static String[] names={"liwu","zhangsan","xiaoqinag","laona","dabao","lisi","zhangsan","xiaoqinag","laona","dabao","lisi","zhangsan","xiaoqinag","laona","dabao","lisi","zhangsan","xiaoqinag","laona","dabao","lisi","zhangsan","xiaoqinag","laona","dabao","lisi","zhangsan","xiaoqinag","laona","dabao","lisi"};  
	private static int[] attachs={1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};  
     
    public static void main(String[] args) throws Exception {
    	         createIndex () ;
    	         //delete() ;
    	         //search () ; 
    	         //paserQuery() ;
    	         //searchBypage02(1,2,"welcome to you");
   }
    	     
    	     
     static {
         try {
             setDate() ;
             directory = FSDirectory.open(Paths.get("F:/indexFile")) ;
             indexWriter = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer())) ;
             indexReader = DirectoryReader.open(directory) ;
             search = new IndexSearcher(indexReader);
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
     
     public static void setDate() {  
          SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-kk");  
          try {  
              dates=new Date[ids.length];  
              dates[0]=sdf.parse("2010-08-17");  
              dates[1]=sdf.parse("2011-02-17");  
              dates[2]=sdf.parse("2012-03-17");  
              dates[3]=sdf.parse("2011-04-17");  
              dates[4]=sdf.parse("2012-05-17");  
              dates[5]=sdf.parse("2011-07-17"); 
              dates[6]=sdf.parse("2011-02-17");  
              dates[7]=sdf.parse("2012-03-17");  
              dates[8]=sdf.parse("2011-04-17");  
              dates[9]=sdf.parse("2012-05-17");  
              dates[10]=sdf.parse("2011-07-17");  
              dates[11]=sdf.parse("2011-02-17");  
              dates[12]=sdf.parse("2012-03-17");  
              dates[13]=sdf.parse("2011-04-17");  
              dates[14]=sdf.parse("2012-05-17");  
              dates[15]=sdf.parse("2011-07-17");  
              dates[16]=sdf.parse("2011-02-17");  
              dates[17]=sdf.parse("2012-03-17");  
              dates[18]=sdf.parse("2011-04-17");  
              dates[19]=sdf.parse("2012-05-17");  
              dates[20]=sdf.parse("2011-07-17");  
              dates[21]=sdf.parse("2011-02-17");  
              dates[22]=sdf.parse("2012-03-17");  
              dates[23]=sdf.parse("2011-04-17");  
              dates[24]=sdf.parse("2012-05-17");  
              dates[25]=sdf.parse("2011-07-17");  
              dates[26]=sdf.parse("2011-02-17");  
              dates[27]=sdf.parse("2012-03-17");  
              dates[28]=sdf.parse("2011-04-17");  
              dates[29]=sdf.parse("2012-05-17");  
              dates[30]=sdf.parse("2011-07-17");  
           } catch (Exception e) {  
              e.printStackTrace();  
           }
     }
     
     
     
     /**
      * ����/��������
      */
     public static void createIndex () {
         try {
              //��յ�ǰ��������
             indexWriter.deleteAll() ;
             for (int i = 0 ; i<=30; i++) {
                 Document d = new Document();
                 System.out.println(ids[i]+"-"+emails[i]+"-"+contents[i]+"-"+names[i]+"-"+attachs[i]+"-"+dates[i]);
                 d.add(new Field("id",ids[i],TextField.TYPE_STORED)) ;
                 d.add(new Field("email",emails[i],TextField.TYPE_STORED)) ;
                 d.add(new Field("content",contents[i],TextField.TYPE_NOT_STORED)) ;
                 d.add(new Field("name",names[i],TextField.TYPE_STORED)) ;
                 d.add(new IntField("attach",1,IntField.TYPE_STORED)) ;
                 d.add(new LongField("date",dates[i].getTime(),LongField.TYPE_STORED)) ;
                 indexWriter.addDocument(d) ;
             }
         } catch (IOException e) {
             e.printStackTrace();
         } finally {
             try {
                 //������writer����Ҫ�ص�����commit����Ȼ����д�벻��ȥ����
                 indexWriter.commit() ;
                 indexWriter.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
     }
     
     //�򵥵Ĳ�ѯ
     public static void search (){
         System.out.println(indexReader.maxDoc()); //������е�DOC��
         System.out.println(indexReader.numDocs()); //������õ�DOC��
         System.out.println(indexReader.numDeletedDocs()); //���ɾ����DOC��
         try {
             //��ȡ������
             QueryParser parser = new QueryParser("email",new StandardAnalyzer()) ;
             //���ò�ѯ����
             Query query = parser.parse("@dd.org~");
             //���ò��ҽ�� -���Ϊ10��
             TopDocs tocs =  search.search(query,10) ;
             //������ѯ���
             ScoreDoc[] docs = tocs.scoreDocs ;
             for (ScoreDoc sd : docs) {
                 Document doc = search.doc(sd.doc) ;
                 //�����content���Ϊnull����Ϊ����û�б���Ŷ~
                 System.out.println(doc.get("id")+":" + doc.get("email") + "-" + doc.get("content")+"-" + doc.get("name")+"-" + doc.get("attach")+"-" + doc.get("date"));
             }
         } catch (org.apache.lucene.queryparser.classic.ParseException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
     
     /**
      * ɾ������
      */
     public static void delete () {
         try {
             //ɾ���������ڻ���վ�У��ɻָ�
             indexWriter.deleteDocuments(new Term("shouzimu","xxxxlxw")) ;
             //ɾ�������ɻָ�
             indexWriter.forceMergeDeletes() ;
             indexWriter.commit() ;
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
     
     /**
      * ��ѯ
      * @throws Exception
      */
     public static void paserQuery () throws Exception {
         //�������Ƕ���һ�����ڱ�׼�ִ�����Query;������Ĭ��Ϊcontent
         QueryParser parser = new QueryParser("content",new StandardAnalyzer()) ;
         try {
             //��Ĭ�����в��� ��you��
             Query query = parser.parse("you");
             //��Ĭ�����в����� boy ����girl ��
             query = parser.parse("boy girl");
             query = parser.parse("boy OR girl");
             //��name���в�� name Ϊxiaoqinag��
             query = parser.parse("name:xiaoqinag");
             //���������βΪ@dd.org��   ͨ�����ѯ�����ã�������  �����
             query = parser.parse("email:a?@aa.org");  //�ᱨ�� ��ʾͨ������ܷ�����λ
             //��Ĭ�����в��� ��how ���� you��
             query = parser.parse("how AND you");
             //��Ĭ�����в��� ��hello ����û�� girl��   ǧ��Ҫע��ո�
             query = parser.parse("-girl +hello");
             //��ȫƥ��hello a boy
             query = parser.parse("\"hello a girl\"");
             //��ʾid 2-4 �ı����� TO��д
             query = parser.parse("id:[2 TO 4]");
             //��ʾid 2-4 �Ŀ����� TO��д
             query = parser.parse("id:{2 TO 4}");
             //��ʾid hello �� boy ֮����С��1��  
             query = parser.parse("\"hello boy\"~1");
             //ģ����ѯ
             query = parser.parse("name:xiaoqiang~");
             query = parser.parse("attach:{2 TO 4}");
             TopDocs tdocs = search.search(query, 10) ;
             ScoreDoc[] sdoc = tdocs.scoreDocs;
             for (ScoreDoc sd : sdoc) {
                 Document doc = search.doc(sd.doc) ;
                 System.out.println(doc.get("id")+":" + doc.get("email") + "-" + doc.get("content")+"-" + doc.get("name")+"-" + doc.get("attach")+"-" + doc.get("date"));
             }
         } catch (Exception e) {
        	 e.printStackTrace();
         }
     }
     
     
     /**
      * ��ҳ��ѯ1
      * @param page ��ʼҳ��
      * @param pageSize һҳ����ʾ����
      * @param str
      * @throws Exception
      */
     public static void searchBypage01 (int page,int pageSize,String str) throws Exception {
         int start = (page-1) * pageSize ;
         int end = start + pageSize ;
         QueryParser parser = new QueryParser("content",new StandardAnalyzer()) ;
         try {
             Query query = parser.parse(str) ;
             TopDocs tdos = search.search(query, 100) ;
             ScoreDoc[] sds = tdos.scoreDocs ;
             for (int i = start ;i <end;i++ ) {
                 Document doc = search.doc(sds[i].doc) ;
                 System.out.println(doc.get("id")+":" + doc.get("email") + "-" + doc.get("content")+"-" + doc.get("name")+"-" + doc.get("attach")+"-" + doc.get("date"));
             }
         } catch (Exception e) {
             e.printStackTrace();
         } 
     }
     
      /**
       * ��ҳ��ѯ2     
       * @param page ҳ��
       * @param pageSize һҳ��ʾ����������
       * @param str
       * @throws ParseException
       */
      public static void searchBypage02 (int page,int pageSize,String str) throws ParseException {
         int start = (page-1) * pageSize ;
         QueryParser parser = new QueryParser("content",new StandardAnalyzer()) ;
         try {
             Query query = parser.parse(str) ;
             TopDocs tdos = search.search(query, 100) ;
             ScoreDoc[] sds = tdos.scoreDocs ;
             tdos = search.searchAfter(sds[start], query, pageSize) ;
             sds = tdos.scoreDocs ;
             for (ScoreDoc sd : sds) {
                 Document doc = search.doc(sd.doc) ;
                 System.out.println(doc.get("id")+":" + doc.get("email") + "-" + doc.get("content")+"-" + doc.get("name")+"-" + doc.get("attach")+"-" + doc.get("date"));
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
  }