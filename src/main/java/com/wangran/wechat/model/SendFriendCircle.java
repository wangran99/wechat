package com.wangran.wechat.model;

import lombok.Data;

import java.util.List;

/**
 * @author ：Wang
 * @date ：Created in 2020/4/1 20:13
 * @description：
 */
@Data
public class SendFriendCircle {
    private int type;
    private List<String> blackList;
    private List<String> withUserList;
    private List<MediaInfo> mediaInfos;
    private String title;
    private String contentUrl;
    private String description;
    private String content;
    private String wxId;
}
