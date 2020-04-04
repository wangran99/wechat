package com.wangran.wechat.model;

import lombok.Data;

/**
 * @author ：Wang
 * @date ：Created in 2020/3/29 15:42
 * @description：
 */
@Data
public class CheckLoginResp {
    private String Uuid;
    private String WxId;
    private String NickName;
    private String State;
    private String Device;
    private String HeadUrl;
    private String Mobile;
    private String Email;
    private String Alias;
    private String Data62;
}
