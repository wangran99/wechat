package com.wangran.wechat.model;

import lombok.Data;

import java.util.List;

/**
 * @author ：Wang
 * @date ：Created in 2020/4/1 19:39
 * @description：
 */
@Data
public class UploadImageBaseRsp {
    private Integer Ret;
    private String ClientId;
    private BufferUrl BufferUrl;
    private Integer ThumbUrlCount;
    private List<BufferUrl> ThumbUrls;
    private Integer Id;
    private Integer Type;

}
