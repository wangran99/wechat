package com.wangran.wechat.controller;

import com.google.gson.*;
import com.wangran.wechat.model.*;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author ：Wang
 * @date ：Created in 2020/3/29 10:09
 * @description：
 */


@RequestMapping("/wechat")
@Controller
@Slf4j
public class WechatController {

    private final String url = "";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    Gson gson;

    @GetMapping("/get")
    @CrossOrigin(allowCredentials = "true")
    public String main(Model model) {
        return "wechat";
    }

    @GetMapping("/qrcode")
    @ResponseBody
    public GetQrCodeResp getQrCode() {
        GetQrCode getQrCode = new GetQrCode();
        JsonObject qrCode = post("/Login/GetQrCode", getQrCode);
        GetQrCodeResp getQrCodeResp = gson.fromJson(qrCode.getAsJsonObject("Data"), GetQrCodeResp.class);
        return getQrCodeResp;
    }

    @PostMapping("/checkLogin")
    @ResponseBody
    public CheckLoginResp checkLogin(@RequestBody Uuid uuid) {
        JsonObject checkLoginObj = post("/Login/CheckLogin/" + uuid.getUuid(), null);
        if (checkLoginObj == null)
            return new CheckLoginResp();
        CheckLoginResp checkLoginResp = gson.fromJson(checkLoginObj.getAsJsonObject("Data"), CheckLoginResp.class);
        if (checkLoginResp.getWxId() != null) {
//            List<String> contactList = getContact(checkLoginResp.getWxId());
            return checkLoginResp;
        } else
            return new CheckLoginResp();
//        getContact(checkLoginResp.getWxId());
//        return null;
    }

    @PostMapping("/upload")
    @ResponseBody
    public Boolean upload(@RequestParam("file") List<MultipartFile> fileList, @RequestParam("wxId") String wxId,
                          @RequestParam("content") String content) throws IOException {
      log.info("enter upload,time:"+System.currentTimeMillis());
        Base64.Encoder encoder = Base64.getEncoder();
        List<UploadImageBaseRsp> uploadImageBaseRspList = new ArrayList<>();
        for (MultipartFile file : fileList) {
            long start= System.currentTimeMillis();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Thumbnails.of(file.getInputStream()).size(720, 1080).toOutputStream(byteArrayOutputStream);
            String base64 = encoder.encodeToString(byteArrayOutputStream.toByteArray());
            long encodeTime=System.currentTimeMillis();
            log.info("resize image and encode base64,time:"+(encodeTime-start));
            List<String> base64List = new ArrayList<>();
            base64List.add(base64);
            //一次多张图片上传会导致返回的数组除了第一个剩余的都是空值，所以必须一次上传一张图片
            SendFriendCircleImage sendFriendCircleImage = new SendFriendCircleImage();
            sendFriendCircleImage.setBase64s(base64List);
            sendFriendCircleImage.setWxId(wxId);
            JsonObject imageRspObj = post("/FriendCircle/SendFriendCircleImage", sendFriendCircleImage);
            long uploadImageTime=System.currentTimeMillis();
            log.info("upload a image,time:"+(uploadImageTime-encodeTime));
            JsonArray jsonArray = imageRspObj.getAsJsonArray("Data");
            //遍历JsonArray对象
            Iterator it = jsonArray.iterator();
            while (it.hasNext()) {
                JsonElement e = (JsonElement) it.next();
                //JsonElement转换为JavaBean对象
                UploadImageBaseRsp uploadImageBaseRsp = gson.fromJson(e, UploadImageBaseRsp.class);
                uploadImageBaseRspList.add(uploadImageBaseRsp);
            }
        }

        //发送朋友圈
        SendFriendCircle sendFriendCircle = new SendFriendCircle();
        List<String> list = new ArrayList<>();
        sendFriendCircle.setBlackList(list);
        sendFriendCircle.setWithUserList(list);
        List<MediaInfo> mediaInfoList = new ArrayList<>();
        for (UploadImageBaseRsp rsp : uploadImageBaseRspList) {
            if (rsp.getBufferUrl() == null || rsp.getBufferUrl().getUrl() == null
                    || "".equals(rsp.getBufferUrl().getUrl()))
                continue;
            MediaInfo mediaInfo = new MediaInfo();
//            mediaInfo.setWidth(720);
//            mediaInfo.setHeight(1080);
//            mediaInfo.setTotalSize(444);
            mediaInfo.setUrl(rsp.getBufferUrl().getUrl());
            mediaInfo.setImageUrl(rsp.getBufferUrl().getUrl());
            mediaInfoList.add(mediaInfo);
        }
        sendFriendCircle.setWxId(wxId);
        sendFriendCircle.setMediaInfos(mediaInfoList);
        //////////////////////0：纯文字 1：图片 2：视频
        sendFriendCircle.setType(1);
        sendFriendCircle.setTitle("这是title");
        sendFriendCircle.setDescription("这是desc");
        sendFriendCircle.setContent(content);
        sendFriendCircle.setContentUrl("zheshiurl");
        JsonObject friendCircleRspObj = post("/FriendCircle/SendFriendCircle", sendFriendCircle);
        boolean sendFriendCircleResult = friendCircleRspObj.getAsJsonPrimitive("Success").getAsBoolean();
        forkOfficialAccountsMessage("", wxId);
//        List<String> contactList = getContact(wxId);
        logout(wxId);
        return sendFriendCircleResult;
    }

    private Boolean forkOfficialAccountsMessage(String appId, String wxId) {
        //gh_7b66a38db0c8  今日元宝山
        String ybsApp = "gh_7b66a38db0c8";
        ForkOfficialAccountsMessage forkOfficialAccountsMessage = new ForkOfficialAccountsMessage();
        forkOfficialAccountsMessage.setAppId(ybsApp);
        forkOfficialAccountsMessage.setWxId(wxId);
        JsonObject forkOfficialAccountsMessageRspObj = post("/Gh/ForkOfficialAccountsMessage", forkOfficialAccountsMessage);
        return forkOfficialAccountsMessageRspObj.getAsJsonPrimitive("Success").getAsBoolean();
    }

    //    private Boolean addGroup(String groupId,String wxId){
//
//    }

    private boolean logout(String wxId){
        JsonObject logoutObj = post("/Login/LogOut/"+wxId, null);
        return logoutObj.getAsJsonPrimitive("Success").getAsBoolean();
    }
    private List<String> getContact(String wxId) {
        GetContractList getContractList = new GetContractList();
        getContractList.setWxId(wxId);

        JsonObject getContractListObj = post("/Friend/GetContractList", getContractList);
        JsonArray jsonArray = getContractListObj.getAsJsonObject("Data").getAsJsonArray("Contracts");
        List<String> contactList = new ArrayList<>();
        //遍历JsonArray对象
        Iterator it = jsonArray.iterator();
        while (it.hasNext()) {
            JsonElement e = (JsonElement) it.next();
            //JsonElement转换为JavaBean对象
            String contact = gson.fromJson(e, String.class);
            contactList.add(contact);
        }
//        获取公众号
        List<String> ghList = contactList.stream().filter(o -> o.startsWith("gh_")).collect(Collectors.toList());
        //        获取群组
        List<String> groupList = contactList.stream().filter(o -> o.endsWith("@chatroom")).collect(Collectors.toList());
        return contactList;
    }

    private JsonObject post(String url, Object requestBody) {
        // 请求头
        HttpHeaders headers = new HttpHeaders();
        MimeType mimeType = MimeTypeUtils.parseMimeType("application/json");
        MediaType mediaType = new MediaType(mimeType.getType(), mimeType.getSubtype(), Charset.forName("UTF-8"));
        // 请求体
        headers.setContentType(mediaType);
        // 发送请求
        HttpEntity<String> entity = new HttpEntity<>(gson.toJson(requestBody), headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        String responseBody = responseEntity.getBody();
        JsonObject obj = (JsonObject) new Gson().fromJson(responseBody, JsonObject.class);
        JsonPrimitive successObj = obj.getAsJsonPrimitive("Success");
        boolean result = successObj.getAsBoolean();
//        JsonObject dataObj = obj.get("Data");
        return obj;
    }


//    public String uuid(){
//
//    }
}
