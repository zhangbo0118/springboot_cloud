package ltd.newbee.mall.service;

import ltd.newbee.mall.controller.vo.GoodInfoVIO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GoodsInfoService {
    List<GoodInfoVIO> search(MultipartFile file) throws Exception;


}
