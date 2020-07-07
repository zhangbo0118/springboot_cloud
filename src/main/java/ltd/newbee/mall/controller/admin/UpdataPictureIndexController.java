package ltd.newbee.mall.controller.admin;

import lombok.SneakyThrows;
import net.semanticmetadata.lire.*;
import net.semanticmetadata.lire.impl.ChainedDocumentBuilder;


//import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class UpdataPictureIndexController {
    @Value("${INDEX_PATH}")
    private String INDEX_PATH;
    @Value("${INDEX_FILE_PATH}")
    private String INDEX_FILE_PATH;
    ChainedDocumentBuilder builder = (ChainedDocumentBuilder) getDocumentBuilder();

    private DocumentBuilder getDocumentBuilder() {
        ChainedDocumentBuilder result = new ChainedDocumentBuilder();
        result.addBuilder(DocumentBuilderFactory.getTamuraDocumentBuilder());
        result.addBuilder(DocumentBuilderFactory.getFCTHDocumentBuilder());
        result.addBuilder(DocumentBuilderFactory.getFCTHDocumentBuilder());
        result.addBuilder(DocumentBuilderFactory.getCEDDDocumentBuilder());
        result.addBuilder(DocumentBuilderFactory.getColorHistogramDocumentBuilder());

        return result;
    }

    /*@GetMapping("/createindex")
    public String createIndex (ModelMap modelMap){
       // UpdataPictureIndexController updataPictureIndexController = new UpdataPictureIndexController();
       // updataPictureIndexController.start();

            new Thread(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    //创建一个合适的文件生成器，Lire针对图像的多种属性有不同的生成器
                    ChainedDocumentBuilder chainedDocumentBuilder = new ChainedDocumentBuilder();
                    IndexWriter iw = null;
                    try {
                        DocumentBuilder db = DocumentBuilderFactory.getCEDDDocumentBuilder();
                        // DocumentBuilder fc = DocumentBuilderFactory.getFCTHDocumentBuilder();
                        chainedDocumentBuilder.addBuilder(db);
                        IndexWriterConfig iwc = new IndexWriterConfig(Version., new SimpleAnalyzer(Version.LUCENE_33));
                        iw = new IndexWriter(FSDirectory.open(new File(INDEX_PATH)), iwc);
                        File parent = new File(INDEX_FILE_PATH);
                        for (File f : parent.listFiles()) {
                            // 创建Lucene索引
                            Document doc = chainedDocumentBuilder.createDocument(new FileInputStream(f), f.getName());
                            // 将文件加入索引
                            iw.addDocument(doc);
                        }
                    } catch (Exception e) {
                    } finally {
                        iw.optimize();
                        iw.close();
                    }
                }
            }).start();
        modelMap.addAttribute("msg","正在更新索引");
        return "admin/newbee_mall_goods_edit";
    }*/
    public void creatlire(File file) throws IOException {
        //创建一个合适的文件生成器，Lire针对图像的多种属性有不同的生成器
        ChainedDocumentBuilder chainedDocumentBuilder = new ChainedDocumentBuilder();
        IndexWriter iw = null;
        try {
            DocumentBuilder db = DocumentBuilderFactory.getCEDDDocumentBuilder();
            // DocumentBuilder fc = DocumentBuilderFactory.getFCTHDocumentBuilder();
            chainedDocumentBuilder.addBuilder(db);
            IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_33, new SimpleAnalyzer(Version.LUCENE_33));
            iw = new IndexWriter(FSDirectory.open(new File(INDEX_PATH)), iwc);
            File parent = new File(INDEX_FILE_PATH);

            // 创建Lucene索引
            Document doc = chainedDocumentBuilder.createDocument(new FileInputStream(file), file.getName());
            // 将文件加入索引
            iw.addDocument(doc);
        } catch (Exception e) {
        } finally {
            iw.optimize();
            iw.close();
        }
    }
}


