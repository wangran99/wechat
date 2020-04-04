package com.wangran.wechat.model;

import lombok.Data;

/**
 * @author ：Wang
 * @date ：Created in 2020/3/29 11:55
 * @description：
 */
@Data
public class GetQrCodeResp {

    private String QrBase64;
    private String Uuid;
    private String ExpiredTime;
}
