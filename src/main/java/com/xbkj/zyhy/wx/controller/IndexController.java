package com.xbkj.zyhy.wx.controller;


import com.xbkj.zyhy.wx.http.MyOkHttpClient;
import com.xbkj.zyhy.wx.translate.TranslateUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
public class IndexController {

//    wxzyhy-1.0.1-SNAPSHOT.jar
//    demo-0.0.1-SNAPSHOT.jar
//    http://localhost:18081/wx/portal/wx418fe69af321e06f?echostr=1234567890&signature=sdfdsfsdfsd&timestamp=123432432423&nonce=sdfa
//    private final TranslateUtil mTranslateUtil;

    @RequestMapping("/")
    public Flux<String> index(){
        return Flux.just("Hello,it's a nice day.");
    }

}
