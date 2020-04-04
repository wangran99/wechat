package com.wangran.wechat.model;

import lombok.Data;

/**
 * @author ：Wang
 * @date ：Created in 2020/3/29 18:03
 * @description：
 */
@Data
public class GetContractList {
    private int currentWxcontactSeq;
    private int currentChatRoomContactSeq;
    private String wxId;
}
