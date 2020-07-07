package ltd.newbee.mall.controller.admin;

import ltd.newbee.mall.controller.vo.GoodInfoVIO;
import ltd.newbee.mall.service.GoodsInfoService;
import ltd.newbee.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/product")
public class GoodInfoController {
    @Resource
    private GoodsInfoService goodsInfoService;

    /**
     * 点击相机按钮，上传被搜索图片，保存到服务器
     */
    @PostMapping("/uploadSearchPic")
    public String uploadSearchPic(@RequestParam("personfile") MultipartFile file, HttpServletRequest request) throws Exception {
        if (null == file || file.isEmpty()) {
            return "err";
        }
        /**
         * 提取被搜索的图片流特征值并跟索引库对比
         */
        List<GoodInfoVIO> list = goodsInfoService.search(file);
        PageResult page = new PageResult(list, 100, 10, 1);
        request.setAttribute("pageResult", page);
        return "mall/search";
    }
}
