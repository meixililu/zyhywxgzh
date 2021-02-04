package com.xbkj.zyhy.wx.translate;

import com.xbkj.zyhy.wx.http.MyOkHttpClient;
import com.xbkj.zyhy.wx.utils.MD5;
import com.xbkj.zyhy.wx.utils.Setings;
import com.xbkj.zyhy.wx.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@Slf4j
@Component
@AllArgsConstructor
public class TranslateUtil {

    public static final String Header = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36";


    private MyOkHttpClient okHttpCli;
//    http://wxgzh.mzxbkj.com/wx/portal/wxe4faa8e49af11911
//    http://wechat.xxmoviexx.com/

    public String postIcibaNew(String question) {
        //zh  en  ja  ko  fr  de  es
        String timestamp = "12345678902";
        String platform = "wechat";
        String network = "wifi";
        String from = "";
        String to = "";
        if (StringUtils.isEnglish(question)) {
            from = "en";
            to = "zh";
        } else {
            from = "zh";
            to = "en";
        }
        String sign = getMd5Sign(PVideoKey, timestamp, question,
                platform, network, from, to);
//        System.out.println("sign:"+sign+"--from:"+from+"--to:"+to+"--question:"+question);
        String url = Setings.IcibaTranslateNewUrl + "q=" + question + "&network=" + network +
                "&platform=" + platform + "&sign=" + sign + "&type=" + "0" + "&fr=" + from +
                "&to=" + to + "&timestamp=" + timestamp;
//        System.out.println("url:"+url);
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", Header)
                .build();
        return okHttpCli.execute(request);
    }

    public String postTranQQIMGAPi(String image) {
        String time_stamp = String.valueOf(System.currentTimeMillis()/1000);
        String session_id = String.valueOf(System.currentTimeMillis());
        String nonce_str = StringUtils.getRandomString(16);
        String source = "auto";
        String target = "auto";
        String scene = "doc";
        Map<String, String> map = null;
        try {
            map = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String str1, String str2) {
                    return str1.compareTo(str2);
                }
            });
            map.put("app_id", URLEncoder.encode(Setings.QQAPPID,"UTF-8"));
            map.put("time_stamp",URLEncoder.encode(String.valueOf(time_stamp),"UTF-8"));
            map.put("nonce_str",URLEncoder.encode(nonce_str,"UTF-8"));
            map.put("image",URLEncoder.encode(image,"UTF-8"));
            map.put("scene",URLEncoder.encode(scene,"UTF-8"));
            map.put("session_id",URLEncoder.encode(session_id,"UTF-8"));
            map.put("source",URLEncoder.encode(source,"UTF-8"));
            map.put("target",URLEncoder.encode(target,"UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sign = getSortData(map);
        log.info("sign:"+sign);
        FormBody formBody = new FormBody.Builder()
                .add("app_id", Setings.QQAPPID)
                .add("time_stamp", time_stamp)
                .add("nonce_str", nonce_str)
                .add("image", image)
                .add("session_id", session_id)
                .add("scene", scene)
                .add("sign", sign)
                .add("source", source)
                .add("target", target)
                .build();
        return okHttpCli.post(Setings.QQTranIMGApi,  formBody);
    }

    public String postQQOCRAPi(String image) {
        String time_stamp = String.valueOf(System.currentTimeMillis()/1000);
        String nonce_str = StringUtils.getRandomString(16);
        Map<String, String> map = null;
        try {
            map = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String str1, String str2) {
                    return str1.compareTo(str2);
                }
            });
            map.put("app_id", URLEncoder.encode(Setings.QQAPPID,"UTF-8"));
            map.put("time_stamp",URLEncoder.encode(String.valueOf(time_stamp),"UTF-8"));
            map.put("nonce_str",URLEncoder.encode(nonce_str,"UTF-8"));
            map.put("image",URLEncoder.encode(image,"UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sign = getSortData(map);
        log.info("sign:"+sign);
        FormBody formBody = new FormBody.Builder()
                .add("app_id", Setings.QQAPPID)
                .add("time_stamp", time_stamp)
                .add("nonce_str", nonce_str)
                .add("image", image)
                .add("sign", sign)
                .build();
        return okHttpCli.post(Setings.QQTranIMGOCRApi,  formBody);
    }

    public String getSortData(Map<String, String> map){
        String result = "";
        if(map != null){
            for(Map.Entry<String,String> entry : map.entrySet()){
                result += entry.getKey() + "=" + entry.getValue() + "&";
            }
            result += "app_key="+ Setings.QQAPPKEY;
            result = MD5.encode(result).toUpperCase();
        }
        return result;
    }

    public static String getMd5Sign(String key, String... strs){
        String tempStr = "";
        String sign = "";
        List<String> list = new ArrayList<>();
        for(String item : strs){
            list.add(item);
        }
        Collections.sort(list);
        for(String item : list){
            tempStr = tempStr + key + item;
        }
        sign = MD5.encode(tempStr);
        return sign;
    }




    public static final String PVideoKey = "sfjsdlf342ds4rbmd24fsvh5fg56d";

}
