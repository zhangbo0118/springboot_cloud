package ltd.newbee.mall.service.impl;

import ltd.newbee.mall.controller.vo.GoodInfoVIO;
import ltd.newbee.mall.dao.NewBeeMallGoodsMapper;
import ltd.newbee.mall.service.GoodsInfoService;
import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.ImageSearchHits;
import net.semanticmetadata.lire.ImageSearcher;
import net.semanticmetadata.lire.ImageSearcherFactory;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoodInfoServiceImpl implements GoodsInfoService {
    @Value("${INDEX_PATH}")
    private String INDEX_PATH;// 索引文件存放路径
    @Value("${INDEX_FILE_PATH}")
    private String INDEX_FILE_PATH; //要索引的图片文件目录

    @Resource
    private NewBeeMallGoodsMapper newBeeMallGoodsMapper;

    @Override
    public List<GoodInfoVIO> search(MultipartFile file) throws IOException {
        StringBuilder sb = new StringBuilder();
        List<GoodInfoVIO> productList = new ArrayList<>();
        //获取图片名称集合
        List<String> imgNameList = this.searchSimilar(file);
        System.out.println("**********************************************************8");
        System.out.println(imgNameList);
        if (!CollectionUtils.isEmpty(imgNameList)) {
            for (int i = 0; i < imgNameList.size(); i++) {
                String s = imgNameList.get(i);
                String b = "http://localhost:8080/upload/";
                b += s;
                imgNameList.set(i, b);
            }
        } else {
            return productList;
        }
        System.out.println(imgNameList);
        //查询数据库
        productList = newBeeMallGoodsMapper.searchProduct(imgNameList);

        return productList;
    }

    //获取图片集合
    private List<String> searchSimilar(MultipartFile file) throws IOException {
        List<String> imgNameList = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        try {
            IndexReader ir = IndexReader.open(FSDirectory.open(new File(INDEX_PATH)));//打开索引
            //IndexReader ir = IndexReader.open(FSDirectory.open(new File(searchFile)));//打开索引
            ImageSearcher is = ImageSearcherFactory.createDefaultSearcher();//创建一个图片搜索器(默认的图片搜索器是CEDD)
            //ImageSearcher fcthImageSearcher = ImageSearcherFactory.createFCTHImageSearcher(100);//测试用fcth搜索器
            //FileInputStream fis = new FileInputStream(searchFile);//搜索图片源
            //BufferedImage bi = ImageIO.read(fis);
            BufferedImage bi = ImageIO.read(inputStream);
            ImageSearchHits ish = is.search(bi, ir);//根据上面提供的图片搜索相似的图片
            //ImageSearchHits search = fcthImageSearcher.search(bi, ir);//用fcth搜索器查相似图片

            for (int i = 0; i < 50; i++) {//显示前50条记录（根据匹配度排序）
                System.out.println(ish.score(i) + ": " + ish.doc(i).getFieldable(DocumentBuilder.FIELD_NAME_IDENTIFIER).stringValue());
                if (ish.score(i) >= 0.65000000) {
                    Fieldable fieldable = ish.doc(i).getFieldable(DocumentBuilder.FIELD_NAME_IDENTIFIER);
                    imgNameList.add(ish.doc(i).getFieldable(DocumentBuilder.FIELD_NAME_IDENTIFIER).stringValue());
                }
            }
        } catch (CorruptIndexException corruptIndexException) {
            throw corruptIndexException;
        } catch (IOException e) {
            throw e;
        } finally {
            inputStream.close();
        }
        return imgNameList;
    }
    //获取图片集合
   /*private List<String> searchSimilar(MultipartFile file) throws IOException {
       List<String> imgNameList = new ArrayList<>();
       InputStream inputStream = file.getInputStream();
       try {
           IndexReader ir = DirectoryReader.open(FSDirectory.open(new File(INDEX_PATH).toPath()));//打开索引
           ImageSearcher is = ImageSearcherFactory.createDefaultSearcher();//创建一个图片搜索器(默认的图片搜索器是CEDD)
           BufferedImage bi = ImageIO.read(inputStream);
           ImageSearchHits ish = is.search(bi, ir);//根据上面提供的图片搜索相似的图片
           for (int i = 0; i < 50; i++) {//显示前50条记录（根据匹配度排序）
               System.out.println(ish.score(i) + ": " + ish.doc(i).getField(DocumentBuilder.FIELD_NAME_IDENTIFIER).stringValue());
               if (ish.score(i) >= 0.65000000) {
                   Document doc = ish.doc(i);
                   IndexableField field = ish.doc(i).getField(DocumentBuilder.FIELD_NAME_IDENTIFIER);
                   imgNameList.add(field.stringValue());
               }
           }
       }catch (CorruptIndexException corruptIndexException) {
           throw corruptIndexException;
       } catch (IOException e) {
           throw e;
       } finally {
           inputStream.close();
       }
       return imgNameList;
   }*/
}
