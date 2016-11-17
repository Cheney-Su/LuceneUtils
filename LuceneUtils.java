package com.doubles.utils;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author suhuaqiang
 * @ClassName:LuceneUtils
 * @Description: (Lucene工具类(基于Lucene5.0封装) )
 * @Email: suhuaqiang@hyx.com
 * @date 2016/11/16 12:09
 */
public class LuceneUtils {
    private static LuceneManager luceneManager = LuceneManager.getSingleton();
    public static Analyzer analyzer = new StandardAnalyzer();

    /**
     * 打开索引目录
     *
     * @param luceneDir
     * @return
     * @throws IOException
     */
    public static FSDirectory openFSDirectory(String luceneDir) {
        FSDirectory fsDirectory = null;
        try {
            fsDirectory = FSDirectory.open(Paths.get(luceneDir));
            IndexWriter.isLocked(fsDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fsDirectory;
    }

    /**
     * 关闭索引目录并销毁
     *
     * @param directory
     * @throws IOException
     */
    public static void closeDirectory(Directory directory) throws IOException {
        if (directory != null) {
            directory.close();
            directory = null;
        }
    }

    /**
     * 获取IndexWriter
     *
     * @param dir
     * @param conf
     * @return
     */
    public static IndexWriter getIndexWriter(Directory dir, IndexWriterConfig conf) {
        return luceneManager.getIndexWriter(dir, conf);
    }

    /**
     * 获取IndexWriter
     *
     * @param directoryPath
     * @param conf
     * @return
     */
    public static IndexWriter getIndexWriter(String directoryPath, IndexWriterConfig conf) {
        FSDirectory directory = openFSDirectory(directoryPath);
        return luceneManager.getIndexWriter(directory, conf);
    }

    /**
     * 获取IndexWriterConfig
     *
     * @return
     */
    public static IndexWriterConfig getIndexWriterConfig() {
        return new IndexWriterConfig(analyzer);
    }

    /**
     * 获取IndexReader
     *
     * @param dir
     * @param enableNRTReader 是否开启NRTReader
     * @return
     */
    public static IndexReader getIndexReader(Directory dir, boolean enableNRTReader) {
        return luceneManager.getIndexReader(dir, enableNRTReader);
    }

    /**
     * 获取IndexReader(默认不启用NRTReader)
     *
     * @param dir
     * @return
     */
    public static IndexReader getIndexReader(Directory dir) {
        return luceneManager.getIndexReader(dir);
    }

    /**
     * 获取IndexSearcher
     *
     * @param reader   IndexReader对象
     * @param executor 如果你需要开启多线程查询，请提供ExecutorService对象参数
     * @return
     */
    public static IndexSearcher getIndexSearcher(IndexReader reader, ExecutorService executor) {
        return luceneManager.getIndexSearcher(reader, executor);
    }

    /**
     * 获取IndexSearcher(不支持多线程查询)
     *
     * @param reader IndexReader对象
     * @return
     */
    public static IndexSearcher getIndexSearcher(IndexReader reader) {
        return luceneManager.getIndexSearcher(reader);
    }

    /**
     * 创建QueryParser对象
     *
     * @param field
     * @param analyzer
     * @return
     */
    public static QueryParser createQueryParser(String field, Analyzer analyzer) {
        return new QueryParser(field, analyzer);
    }

    /**
     * 关闭IndexReader
     *
     * @param reader
     */
    public static void closeIndexReader(IndexReader reader) {
        if (null != reader) {
            try {
                reader.close();
                reader = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭IndexWriter
     *
     * @param writer
     */
    public static void closeIndexWriter(IndexWriter writer) {
        if (null != writer) {
            try {
                writer.close();
                writer = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭IndexReader和IndexWriter
     *
     * @param reader
     * @param writer
     */
    public static void closeAll(IndexReader reader, IndexWriter writer) {
        closeIndexReader(reader);
        closeIndexWriter(writer);
    }

    /**
     * 更新索引文档
     * @param writer
     * @param term
     * @param document
     */
    public static void updateIndex(IndexWriter writer, Term term, Document document) {
        try {
            writer.updateDocument(term, document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加索引文档
     * @param writer
     * @param document
     */
    public static void addIndex(IndexWriter writer, Document document) {
        updateIndex(writer, null, document);
    }

    /**
     * 索引文档查询
     * @param searcher
     * @param query
     * @return
     */
    public static List<Document> query(IndexSearcher searcher, Query query) {
        TopDocs topDocs = null;
        try {
            topDocs = searcher.search(query, Integer.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ScoreDoc[] scores = topDocs.scoreDocs;
        int length = scores.length;
        if (length <= 0) {
            return Collections.emptyList();
        }
        List<Document> docList = new ArrayList<Document>();
        try {
            for (int i = 0; i < length; i++) {
                Document doc = searcher.doc(scores[i].doc);
                docList.add(doc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return docList;
    }
}
