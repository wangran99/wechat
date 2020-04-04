package com.wangran.wechat.model;

import lombok.Data;

/**
 * @author ：Wang
 * @date ：Created in 2020/3/29 11:46
 * @description：
 */
@Data
public class GetQrCode {
    private String proxyIp;
    private String proxyUserName;
    private String proxyPassword;
    private String deviceId;
}
