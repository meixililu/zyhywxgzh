package com.xbkj.zyhy.wx.bean;

import lombok.Data;

import java.util.List;

@Data
public class QQIMGORCItemList {

    private String item;
    private String itemstring;
    private List<QQIMGORCItemCoord> itemcoord;
    private List<QQIMGORCWords> words;

}