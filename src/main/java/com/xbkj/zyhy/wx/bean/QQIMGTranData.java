package com.xbkj.zyhy.wx.bean;

import lombok.Data;

import java.util.List;

@Data
public class QQIMGTranData {

    private String session_id;
    private List<QQIMGTranRecords> image_records;


}