package com.xbkj.zyhy.wx;

import com.alibaba.fastjson.JSON;
import com.xbkj.zyhy.wx.bean.IcibaNew;
import com.xbkj.zyhy.wx.bean.TranDictResult;
import com.xbkj.zyhy.wx.bean.TranResultRoot;
import com.xbkj.zyhy.wx.http.MyOkHttpClient;
import com.xbkj.zyhy.wx.translate.TranslateUtil;
import lombok.AllArgsConstructor;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@SpringBootTest
class WxzyhyApplicationTests {

	@Test
	void contextLoads() {
//		TranslateUtil mTranslateUtil = new TranslateUtil(new MyOkHttpClient(new OkHttpClient()));
//		String result = mTranslateUtil.postIcibaNew("你好");
//		TranResultRoot mIciba = JSON.parseObject(result, TranResultRoot.class);
//		System.out.println("translate:"+result);
//		System.out.println("translate:"+mIciba.getResult().getResult());
	}

}
