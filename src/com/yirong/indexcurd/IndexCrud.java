package com.yirong.indexcurd;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
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

public class IndexCrud {
	private Directory directory;
	private Analyzer analyzer;
	private IndexWriterConfig config;
	private IndexWriter writer;
	private IndexReader reader;
	String[] ids = {"1","2","3","4","5","6"};  
	String[] names = {"zs","ls","ww","hl","wq","bb"};  
	String[] contents = {  
	   "She had been shopping with her Mom in Wal-Mart. She must have been 6 years old, this beautiful brown haired, freckle-faced image of innocence. It was pouring outside. The kind of rain that gushes over the top of rain gutters, so much in a hurry to hit the Earth, it has no time to flow down the spout."  
	   ,"We all stood there under the awning and just inside the door of the Wal-Mart. We all waited, some patiently, others irritated, because nature messed up their hurried day. I am always mesmerized by rainfall. I get lost in the sound and sight of the heavens washing away the dirt and dust of the world. Memories of running, splashing so carefree as a child come pouring in as a welcome reprieve from the worries of my day."  
	   ,"Her voice was so sweet as it broke the hypnotic trance we were all caught in, Mom, let's run through the rain. she said."  
	   ,"The entire crowd stopped dead silent. I swear you couldn't hear anything but the rain. We all stood silently. No one came or left in the next few minutes. Mom paused and thought for a moment about what she would say."  
	   ,"Now some would laugh it off and scold her for being silly. Some might even ignore what was said. But this was a moment of affirmation in a young child's life. Time when innocent trust can be nurtured so that it will bloom into faith."  
	   ,"To everything there is a season and a time to every purpose under heaven. I hope you still take the time to run through the rain."  
	}; 
	
	
	/**
	 * �ļ�������洢·��
	 */
	public IndexCrud() {
		try {
			directory = FSDirectory.open(Paths.get("F:/indexFile"));
			//analyzer = new StandardAnalyzer();
//			config = new IndexWriterConfig(analyzer);
//			writer = new IndexWriter(directory, config);
//			reader = DirectoryReader.open(directory);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * ��������
	 */
	public void updateIndex() {
		try {
			Term term = new Term("id", "2");
			Document doc = new Document();
			doc.add(new StringField("id", ids[1], Field.Store.YES));
			doc.add(new StringField("name", "lsup", Field.Store.YES));
			doc.add(new TextField("content", contents[1], Field.Store.NO));

			// ���µ�ʱ�򣬻��ԭ���Ǹ�����ɾ������������һ������
			writer.updateDocument(term, doc);

			writer.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * ɾ��ȫ������
	 */
	public void deleteAllIndex() {
		try {
			writer.deleteAll();
			writer.commit();
			System.out.println("����ȫ��ɾ���ɹ�");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����ɾ��
	 */
	public void deleteIndex() {
		try {
			Term[] terms = new Term[2];
			Term term = new Term("id", "1");
			terms[0] = term;
			term = new Term("id", "3");
			terms[1] = term;
		  //��idΪ 1��3������ɾ���� Ҳ���Դ�һ��Query������󣬽�Query���ҵĽ��ɾ����
			writer.deleteDocuments(terms);
			writer.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ����
	 */
	public void readIndex() {
		System.out.println("max num:" + reader.maxDoc());
		System.out.println("index num:" + reader.numDocs());
		System.out.println("delete index num:" + reader.numDeletedDocs());
	}
	  
	/**
	 * �õ���ѯ
	 * @return
	 */
	public IndexSearcher getSearcher() {
		try {
			IndexReader newReader = DirectoryReader.openIfChanged((DirectoryReader) reader);
			if (newReader != null) {
				reader.close();
				reader = newReader;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new IndexSearcher(reader);
	}

	/**
	 * ����������������
	 */
	public void queryIndex() {
		try {
			// ������
			IndexSearcher searcher = getSearcher();
			String queryStr ="make";  
	         //����һ���ʷ���������������ѯ������ص�һ������  
			// ��ѯ�ĸ��ֶ�
			QueryParser parse = new QueryParser("content", analyzer);
			// ��ѯ�ؼ���
			Query query = parse.parse(queryStr);
			TopDocs topDocs = searcher.search(query, 1000);
			// ��ײ���
			ScoreDoc[] hits = topDocs.scoreDocs;
			 for(ScoreDoc scoreDoc:hits){  
	             System.out.print("���Ϊ:"+scoreDoc.doc);  
	             System.out.print("����Ϊ:"+scoreDoc.score);  
	             Document document = searcher.doc(scoreDoc.doc);  
	             System.out.print("·��Ϊ:"+document.get("path"));  
	             System.out.print("�ļ���СΪ:"+document.get("fileSize"));  
	             System.out.print("�ļ���Ϊ:"+document.get("filename"));  
	             System.out.println("�����ɹ�����");  
	         }     
			 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args)  {
		IndexCrud index=new IndexCrud();
		index.updateIndex();
	    //index.readIndex();    //��ȡ����
     	//index.queryIndex();   //��ѯ����
		//index.deleteAllIndex(); //ɾ��ȫ������
	    //index.updateIndex();
	}
}