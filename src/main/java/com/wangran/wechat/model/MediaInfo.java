package com.wangran.wechat.model;

import lombok.Data;

/**
 * @author ：Wang
 * @date ：Created in 2020/4/1 20:12
 * @description：
 */
@Data
public class MediaInfo {
    private String url;
    private String imageUrl;
    private int width;
    private int height;
    private long totalSize;
}
