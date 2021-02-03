package com.xbkj.zyhy.wx.handler;


import com.alibaba.fastjson.JSON;
import com.xbkj.zyhy.wx.bean.*;
import com.xbkj.zyhy.wx.builder.TextBuilder;
import com.xbkj.zyhy.wx.parse.TranParser;
import com.xbkj.zyhy.wx.translate.TranslateUtil;
import com.xbkj.zyhy.wx.utils.ImageDownloader;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.http.util.TextUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@AllArgsConstructor
@Component
public class MsgHandler extends AbstractHandler {

    private TranslateUtil mTranslateUtil;
    private ImageDownloader mImageDownloader;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {
        String content = "";
        if (wxMessage.getMsgType().equals(XmlMsgType.TEXT)) {
            content = translate(wxMessage.getContent());
        }else if (wxMessage.getMsgType().equals(XmlMsgType.VOICE)) {
            content = translate(wxMessage.getRecognition());
        }else if (wxMessage.getMsgType().equals(XmlMsgType.IMAGE)) {
            String imgData = mImageDownloader.downloadImg(wxMessage.getPicUrl());
            String jsData = mTranslateUtil.postQQOCRAPi(imgData);
//            QQIMGTranRoot qroot = JSON.parseObject(jsData,QQIMGTranRoot.class);//img translate
            QQIMGORCRoot qroot = JSON.parseObject(jsData,QQIMGORCRoot.class);//img translate
            String ocrResult = getQQImgORCResult(qroot);
            content = ocrResult;
//            content += "\n";
//            content += translate(ocrResult);
        }else {
            content = "sorry,暂不支持该类型的消息!" + wxMessage.getMsgType();
        }
//        if (!wxMessage.getMsgType().equals(XmlMsgType.EVENT)) {
//            //TODO 可以选择将消息保存到本地
//        }
        return new TextBuilder().build(content, wxMessage, weixinService);
    }

    public String translate(String q){
        String tResult = q;
        System.out.println("translate:"+q);
        String result = mTranslateUtil.postIcibaNew(q);
        try {
            TranResultRoot mIciba = JSON.parseObject(result, TranResultRoot.class);
            if(mIciba != null && mIciba.getResult() != null){
                tResult = mIciba.getResult().getResult();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("translate-result:"+result);
        return tResult;
    }

    public String getQQImgTranResult(QQIMGTranRoot qroot){
        StringBuilder sb = new StringBuilder();
        if(qroot != null && qroot.getRet() == 0){
            QQIMGTranData data = qroot.getData();
            if(data != null){
                List<QQIMGTranRecords> list = data.getImage_records();
                if(list != null && !list.isEmpty()){
                    for (QQIMGTranRecords qrecords : list){
                        sb.append(qrecords.getSource_text());
                        sb.append("\n");
                        sb.append(qrecords.getTarget_text());
                        sb.append("\n\n");
                    }
                }
            }
        }
        return sb.toString().trim();
    }

    public String getQQImgORCResult(QQIMGORCRoot qroot){
        StringBuilder sb = new StringBuilder();
        if(qroot != null && qroot.getRet() == 0){
            QQIMGORCData data = qroot.getData();
            if(data != null){
                List<QQIMGORCItemList> list = data.getItem_list();
                if(list != null && !list.isEmpty()){
                    for (QQIMGORCItemList qrecords : list){
                        sb.append(qrecords.getItemstring());
                    }
                }
            }
        }
        return sb.toString().trim();
    }

}
