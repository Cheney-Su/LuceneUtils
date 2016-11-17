package com.doubles.utils;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

import com.doubles.dao.UserDao;
import com.google.gson.Gson;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author suhuaqiang
 * @ClassName:TxtFileIndexer
 * @Description: ()
 * @Email: suhuaqiang@hyx.com
 * @date 2016/10/28 10:27
 */
public class TxtFileIndexer {

    @Autowired
    private UserDao userDao;

    public static void main(String[] args) throws Exception {
        FSDirectory fsDirectory = LuceneUtils.openFSDirectory("/tem/test");
//        IndexWriter indexWriter = LuceneUtils.getIndexWriter(fsDirectory, LuceneUtils.getIndexWriterConfig());
//        Document document = new Document();
//        String text = "text is text!!!";
//        document.add(new Field("name",text,TextField.TYPE_STORED));
//        LuceneUtils.addIndex(indexWriter,document);
//        LuceneUtils.closeIndexWriter(indexWriter);

        IndexReader indexReader = LuceneUtils.getIndexReader(fsDirectory);
        IndexSearcher indexSearcher = LuceneUtils.getIndexSearcher(indexReader);
        QueryParser queryParser = LuceneUtils.createQueryParser("name", LuceneUtils.analyzer);
        Query query = queryParser.parse("text");
        List<Document> list = LuceneUtils.query(indexSearcher, query);
        for (int i=0;i<list.size();i++){
            System.out.println(list.get(i).get("name"));
        }
        LuceneUtils.closeIndexReader(indexReader);
        LuceneUtils.closeDirectory(fsDirectory);
//        Analyzer analyzer = new StandardAnalyzer();
//
//        // Store the index in memory:
////        Directory directory = new RAMDirectory();
//        // To store an index on disk, use this instead:
//        String directoryName = "/tem/test";
//        File file = new File(directoryName);
//        if (file.exists()) {
//            String[] list = file.list();
//            for (int i = 0; i < list.length; i++) {
//                File del = new File("/tem/test/" + list[i]);
//                if (del.isFile()){
//                    del.delete();
//                }
//            }
//        }
//        Path path = file.toPath();
//        System.out.println(path);
//        Directory directory = FSDirectory.open(path);
//
//        IndexWriterConfig config = new IndexWriterConfig(analyzer);
//        IndexWriter writer = new IndexWriter(directory, config);
//
//        String[] texts = new String[]{"This is the text to be indexed",
//                "That is an cat", "he is shit", "stupid women,so bitch"};
//        for (String text : texts) {
//            Document doc = new Document();
//            doc.add(new Field("fieldname", text, TextField.TYPE_STORED));
//            writer.addDocument(doc);
//        }
//
//        writer.close();
//
//        // Now search the index:
//        DirectoryReader reader = DirectoryReader.open(directory);
//        IndexSearcher searcher = new IndexSearcher(reader);
//
//        QueryParser parser = new QueryParser("fieldname", analyzer);
//
//        Query query = parser.parse("the");
//        // if you parse("the")，then hits.length=0,because "the" is
//        // stopWord,others like "to" "be",also stopWord
//        ScoreDoc[] hits = searcher.search(query, 5).scoreDocs;
//        System.out.println(hits.length);
//
//        // Iterate through the results:
//        for (int i = 0; i < hits.length; i++) {
//            Document hitDoc = searcher.doc(hits[i].doc);
//            System.out.println(hitDoc.get("fieldname"));
//        }
//        reader.close();
//        directory.close();
    }
}

