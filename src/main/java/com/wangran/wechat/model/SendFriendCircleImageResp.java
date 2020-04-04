package com.wangran.wechat.model;

import lombok.Data;

/**
 * @author ：Wang
 * @date ：Created in 2020/3/29 16:57
 * @description：
 */
@Data
public class SendFriendCircleImageResp {
    private String StartPos;
    private String TotalLen;
    private String ClientId;

    private BufferUrl BufferUrl;
    private String ThumbUrlCount;
    private BufferUrl ThumbUrls;

    private String Id;
    private String Type;
}
