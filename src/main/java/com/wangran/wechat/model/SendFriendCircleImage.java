package com.wangran.wechat.model;

import lombok.Data;

import java.util.List;

/**
 * @author ：Wang
 * @date ：Created in 2020/4/1 19:33
 * @description：
 */
@Data
public class SendFriendCircleImage {
    private List<String> base64s;
    private String wxId;
}
