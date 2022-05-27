package com.anjiplus.template.gaea.business;

import cn.hutool.core.net.URLDecoder;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author 林波
 * @date 2022/5/26
 */
public class Demo {
    public static void main( String[] args ) throws URISyntaxException, IOException {
//        SpringApplication.run(ReportApplication.class);
//        MybatisPlusInterceptor bean = ApplicationContextUtils.getBean(MybatisPlusInterceptor.class);
//        System.out.println("done");
        String url = "https://www.ruijie.com.cn/application/Article/GetArticleFile?id=82073&attachmentNo=1&download=true";
        String savePath = "/Users/linbo/Downloads/";

        for (int i = 0; i < 100; i++) {
            download(url, savePath);
        }

    }

    public static void download(String url, String savePath) throws IOException {
        HttpRequest httpRequest = getHttpRequest(url);
        HttpResponse httpResponse = httpRequest.executeAsync();
        if(httpResponse.getStatus() == 302){
            String schema = url.substring(0, url.indexOf("://"));
            url = httpResponse.header("Location");
            url = schema + ":" + url;
            download(url, savePath);
        }else if(httpResponse.getStatus() == 200){
            String filename = URLDecoder.decode(url.substring(url.lastIndexOf("/"), url.lastIndexOf('?')), StandardCharsets.UTF_8);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(savePath + "/" + filename)));
            bufferedWriter.write(httpResponse.body());
            bufferedWriter.flush();
            bufferedWriter.close();
        }else {
            System.err.println(httpResponse.getStatus());
        }
    }

    public static HttpRequest getHttpRequest(String url){
        String cookie = "gr_user_id=50d440d3-0626-4fb6-bf44-e47a8e10f71d; _gscu_1878775803=45759074mdlc1h17; AGL_USER_ID=620aff03-f998-46d4-ad65-09579f837839; __jsluid_s=18fad24d6b3af7b244a30013758b1354; Hm_lvt_608ef9e4b8cd3b7746392c721f2ef555=1647770628,1649601844; c__utma=1557273461.188390773.7340176657450576825.1647770628.1649601844.4; __jsluid_h=c178b3e975f421c736314a6993e05f24; ASP.NET_SessionId=1wwqr045ih2qnnvbbgegiihm; r_f_d_c_k=1504815826; __jsl_clearance_s=1653568355.25|0|jr%2FJqTm6kBkp8SLFfC5EEiWXuAY%3D";
        HttpRequest httpRequest = HttpRequest.get(url);
        httpRequest.header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpRequest.header("accept-encoding", "gzip, deflate, br");
        httpRequest.header("accept-language", "zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7");
        httpRequest.header("cache-control", "no-cache");
        httpRequest.header("cookie", cookie);
        httpRequest.header("dnt", "1");
        httpRequest.header("pragma", "no-cache");
        httpRequest.header("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"101\", \"Google Chrome\";v=\"101\"");
        httpRequest.header("sec-ch-ua-mobile", "?0");
        httpRequest.header("sec-ch-ua-platform", "\"macOS\"");
        httpRequest.header("sec-fetch-dest", "document");
        httpRequest.header("sec-fetch-mode", "navigate");
        httpRequest.header("sec-fetch-site", "none");
        httpRequest.header("sec-fetch-user", "?1");
        httpRequest.header("upgrade-insecure-requests", "1");
        httpRequest.header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.64 Safari/537.36");
        return httpRequest;
    }
}
