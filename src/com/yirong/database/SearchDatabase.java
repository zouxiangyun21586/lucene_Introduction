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
   * ���ݿ⽨�������Ͳ�ѯ����
   * @author luolinghong
   *
   */
public class SearchDatabase {  
	/**
	 * ���ݿ�����
	 */
    private static Connection conn = null;  
    private static Statement stmt = null; 
    /**
     * �������ݿⷵ�ص�ResultSet 
     */
    private static ResultSet rs = null; 
    /**
     * ���������ļ���д��
     */
    private static IndexWriter writer=null;
   /**
    * ���������Ŀ¼
    */
    private static IndexSearcher searcher = null;  
    /**
     * �����ִ���
     */
    private static Analyzer analyzer = new IKAnalyzer(true);  
  
    /** 
     * ��ȡ���ݿ����� 
     * @param queryStr ��Ҫ�����Ĺؼ��� 
     * @return 
     * @throws Exception 
     */  
    public List<User> getResult(String queryStr) throws Exception {  
        List<User> result = null;  
        conn = JdbcUtil.getConnection();  
        if (conn == null) {  
            throw new Exception("���ݿ�����ʧ�ܣ�");  
        }  
        String sql = "select id,account from users";  
        try {  
            stmt = conn.createStatement();  
            rs = stmt.executeQuery(sql);  
            // �����ݿⴴ������,�˴�ִ��һ�Σ���Ҫÿ�����ж���������  
            // �Ժ������и��¿��Ժ�̨���ø�������  
            this.createIndex(rs);  
            TopDocs topDocs = this.search(queryStr);  
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;  
            result = this.addHits2List(scoreDocs);  
        } catch (Exception e) {  
            e.printStackTrace();  
            throw new Exception("���ݿ��ѯsql���� sql : " + sql);  
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
     * Ϊ���ݿ�������ݴ������� 
     * @param �������ݿⷵ�ص�ResultSet 
     * @throws Exception 
     */  
    private void createIndex(ResultSet rs) throws Exception {  
        // �����������  
        Directory directory = FSDirectory.open(Paths.get("F:/indexFile"));
        // ����IndexWriter  
        IndexWriterConfig conf = new IndexWriterConfig(analyzer);  
        IndexWriter indexWriter = new IndexWriter(directory, conf);  
        // ����ResultSet��������  
        while (rs.next()) {  
            // ����document�����field  
            Document doc = new Document();  
            doc.add(new TextField("id", rs.getString("id"), Field.Store.YES));  
            doc.add(new TextField("account", rs.getString("account"),  
                    Field.Store.YES));  
            // ��doc��ӵ�������  
            indexWriter.addDocument(doc);  
        }  
        indexWriter.commit();  //indexWriter����Ҫ�ύ���߹رգ�����д�벻��ȥ
        indexWriter.close();  
        directory.close();  
    }  
  
    
    /** 
     * �������� 
     * @param queryStr ��Ҫ�����Ĺؼ��� 
     * @return 
     * @throws Exception 
     */  
    private TopDocs search(String queryStr) throws Exception {  
        //�����������Ŀ¼  
        Directory directory = FSDirectory.open(Paths.get("F:/indexFile")); 
        DirectoryReader reader = DirectoryReader.open(directory);  
        if (searcher == null) {  
            searcher = new IndexSearcher(reader);  
        }  
        //ʹ�ò�ѯ����������Query  
        QueryParser parser = new QueryParser( "account", analyzer);  
        Query query = parser.parse(queryStr);  
        //�������������õ�����ǰ10���ĵ�  
        TopDocs topDocs = searcher.search(query, 10);  
        return topDocs;  
    }  
  
    /** 
     * �����������ӵ�List�� 
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
            System.out.println("��ѯ�����ѵ�ʱ��Ϊ��" + (endTime - startTime) / 1000); 
            
            //��ѯ��ɺ�,������ȫ��ɾ��������ÿ�εĴ����Ͳ�ѯ
            try {
    			Directory directory = FSDirectory.open(Paths.get("F:/indexFile"));
    			Analyzer analyzer = new StandardAnalyzer();
    			IndexWriterConfig config = new IndexWriterConfig(analyzer);
    			writer = new IndexWriter(directory, config);
    			writer.deleteAll(); //ɾ��ȫ��������
    			writer.commit(); 
    			System.out.println("����ɾ���ɹ�");
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
        } catch (Exception e) {  
            e.printStackTrace();  
            System.out.println(e.getMessage());  
        }  
    }  
}  