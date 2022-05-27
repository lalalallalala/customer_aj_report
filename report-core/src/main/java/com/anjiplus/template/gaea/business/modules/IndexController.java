package com.anjiplus.template.gaea.business.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 林波
 * @date 2022/5/26
 */
@Controller("reportIndexController")
@RequestMapping(IndexController.BASE_URL)
public class IndexController {

    public static final String BASE_URL = "/report/static";
    private static final String INDEX_URL = BASE_URL + "/index.html";

    @Resource
    HttpServletRequest request;

    @Resource
    HttpServletResponse response;

    @RequestMapping("/")
    public void index() throws IOException {
        response.sendRedirect(INDEX_URL);
    }
}
