package com.xbkj.zyhy.wx.parse;

import com.alibaba.fastjson.JSON;
import com.xbkj.zyhy.wx.bean.IcibaNew;
import org.apache.http.util.TextUtils;
import org.springframework.boot.configurationprocessor.json.JSONObject;

public class TranParser {

    public static String tran_js_newapi(String mResult){
        String resutlStr = "";
        if(!TextUtils.isEmpty(mResult)) {
            if (isJson(mResult)) {
                IcibaNew mIciba = JSON.parseObject(mResult, IcibaNew.class);
                if (mIciba != null && mIciba.getContent() != null) {
                    if (mIciba.getStatus().equals("0")) {
                        StringBuilder sb = new StringBuilder();
                        if (!TextUtils.isEmpty(mIciba.getContent().getPh_en())) {
                            sb.append("英[");
                            sb.append(mIciba.getContent().getPh_en());
                            sb.append("]    ");
                        }
                        if (!TextUtils.isEmpty(mIciba.getContent().getPh_am())) {
                            sb.append("美[");
                            sb.append(mIciba.getContent().getPh_am());
                            sb.append("]");
                        }
                        if (sb.length() > 0) {
                            sb.append("\n");
                        }
                        if (mIciba.getContent().getWord_mean() != null) {
                            for (String item : mIciba.getContent().getWord_mean()) {
                                sb.append(item.trim());
                                sb.append("\n");
                            }
                        }
                        if(sb.lastIndexOf("\n") > 0){
                            resutlStr = sb.substring(0, sb.lastIndexOf("\n"));
                        }else {
                            resutlStr = sb.toString();
                        }
                    } else if (mIciba.getStatus().equals("1")) {
                        resutlStr = mIciba.getContent().getOut().replaceAll("<br/>", "").trim();
                    }
                }
            }
        }
        return resutlStr;
    }



    public static boolean isJson(String value) {
        try {
            if (TextUtils.isEmpty(value)) {
                return false;
            }else {
                new JSONObject(value);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
