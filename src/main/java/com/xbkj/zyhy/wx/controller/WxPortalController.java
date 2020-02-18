package com.xbkj.zyhy.wx.controller;

import com.xbkj.zyhy.wx.translate.TranslateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * @author Binary Wang(https://github.com/binarywang)
 * kill -9 $(netstat -nlp | grep :18081 | awk '{print $7}' | awk -F"/" '{ print $1 }')
 * kill -9 $(netstat -nlp | grep :18080 | awk '{print $7}' | awk -F"/" '{ print $1 }')
 * http://wxgzh.mzxbkj.com/wx/portal/wxe4faa8e49af11911
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/wx/portal/{appid}")
public class WxPortalController {
    private final WxMpService wxService;
    private final WxMpMessageRouter messageRouter;

    @GetMapping(produces = "text/plain;charset=utf-8")
    public String authGet(@PathVariable String appid,
                          @RequestParam(name = "signature", required = false) String signature,
                          @RequestParam(name = "timestamp", required = false) String timestamp,
                          @RequestParam(name = "nonce", required = false) String nonce,
                          @RequestParam(name = "echostr", required = false) String echostr) {
        log.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature,
            timestamp, nonce, echostr);
        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }
        if (!this.wxService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }
        if (wxService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }
        return "非法请求";
    }

    @PostMapping(produces = "application/xml; charset=UTF-8")
    public Flux<String> post(@PathVariable String appid,
                             @RequestBody String requestBody,
                             @RequestParam("signature") String signature,
                             @RequestParam("timestamp") String timestamp,
                             @RequestParam("nonce") String nonce,
                             @RequestParam("openid") String openid,
                             @RequestParam(name = "encrypt_type", required = false) String encType,
                             @RequestParam(name = "msg_signature", required = false) String msgSignature) {
//        log.info("\nreceive message:[openid=[{}], [signature=[{}], encType=[{}], msgSignature=[{}],"
//                + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
//            openid, signature, encType, msgSignature, timestamp, nonce, requestBody);
        if (!this.wxService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }
        if (!wxService.checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }
        return Flux.just(executeMesg(encType,requestBody,timestamp,nonce,msgSignature));
    }

    private String executeMesg(String encType,String requestBody,String timestamp,
                               String nonce,String msgSignature){
        String q = "";
        if (encType == null) {
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                q = "";
            }
            q = outMessage.toXml();
        } else if ("aes".equalsIgnoreCase(encType)) {
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxService.getWxMpConfigStorage(),
                    timestamp, nonce, msgSignature);
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                q = "";
            }
            q = outMessage.toEncryptedXml(wxService.getWxMpConfigStorage());
        }
        return q;
    }

    private WxMpXmlOutMessage route(WxMpXmlMessage message) {
        try {
            return this.messageRouter.route(message);
        } catch (Exception e) {
            log.error("路由消息时出现异常！", e);
        }
        return null;
    }
}
