package com.xbkj.zyhy.wx.utils;

import com.xbkj.zyhy.wx.http.MyOkHttpClient;
import com.xbkj.zyhy.wx.translate.TranslateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.TextUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

@Slf4j
@AllArgsConstructor
@Component
public class ImageDownloader {

    public final MyOkHttpClient okHttpCli;

    public String downloadImg(String imgUrl){
        byte[] binaryData = okHttpCli.get(imgUrl);
        String imgData = Base64.getEncoder().encodeToString(binaryData);
        saveFile(binaryData);
        return imgData;
    }

    public boolean saveFile(byte[] binaryData) {
        try {
            FileOutputStream mFileOutputStream = getFile();
            if (mFileOutputStream != null) {
                mFileOutputStream.write(binaryData);
                mFileOutputStream.flush();
                mFileOutputStream.close();
                log.info("saveFile-SaveFile");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public FileOutputStream getFile() throws IOException {
        String filePath = "temp.jpg";
        File mFile = new File(filePath);
        mFile.createNewFile();
        log.info("getFile-CreateFile:" + filePath);
        return new FileOutputStream(mFile);
    }
}
