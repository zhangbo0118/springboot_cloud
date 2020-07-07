package ltd.newbee.mall.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/admin")
public class GoSpiderController {
    /**
     * 点击跳转爬虫页面
     */
    @GetMapping("/gospider")
    public String goSpider(HttpServletRequest request) {
        request.setAttribute("path", "spider");
        return "admin/spider";
    }
}
