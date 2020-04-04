<html>
<head>
    <title>微信推广</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link href="http://apps.bdimg.com/libs/bootstrap/3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-fileinput/5.0.6/css/fileinput.min.css" type="text/css"
          rel="stylesheet">
    <script type="text/javascript" src="https://cdn.bootcss.com/jquery/3.4.1/jquery.js"></script>
    <script type="text/javascript"
            src="https://cdn.bootcss.com/bootstrap-fileinput/5.0.8/js/plugins/piexif.min.js"></script>

    <script type="text/javascript"
            src="https://cdn.bootcss.com/bootstrap-fileinput/5.0.8/js/plugins/purify.js"></script>
    <script type="text/javascript" src="https://cdn.bootcss.com/popper.js/1.16.1/esm/popper.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap/4.1.0/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="https://cdn.bootcss.com/bootstrap-fileinput/5.0.8/js/fileinput.js" type="text/javascript"></script>
    <script src="https://cdn.bootcss.com/bootstrap-fileinput/5.0.7/themes/fa/theme.js" type="text/javascript"></script>
    <script src="https://cdn.bootcss.com/bootstrap-fileinput/5.0.6/js/locales/zh.js" type="text/javascript"></script>

    <script src="https://cdn.bootcss.com/axios/0.19.2/axios.min.js"></script>
    <script>

    </script>
</head>
<body>
<div class="container">
    <div class="text-center ">
        <span>uuid: <p id="uuid"> </p></span>
        <#--        <p>User: ${qr.getQrBase64()}</p>-->
        <#--        <p>expiredTime: ${qr.getExpiredTime()}</p>-->
    </div>
    <img id="qrCode" src="" class="img-responsive center-block">
    <div style="text-align:center">
        <h3 id="name">微信名</h3>
    </div>
    <div style="text-align:center">
        <button style="margin-bottom: 15px" onclick="checkLogin()" class="btn btn-primary btn-lg">扫描完毕</button>
    </div>
<#--    src引用的本地静态文件一定要加..否则放到tomcat找不到文件，会404-->
    <audio id="audio" src="../static/dingdong.wav" controls="controls" hidden>
        Your browser does not support the audio element.
    </audio>
</div>
<form role="form" style="margin-left: 15px;margin-right: 15px;margin-bottom: 10px">
    <div class="form-group">
        <textarea id="content" class="form-control" rows="5">这是我的朋友圈</textarea>
    </div>
</form>
<div style="margin-left: 15px;margin-right: 15px">
    <input id="fileInput" name="files" multiple type="file" class="file"/>
</div>
<div style="text-align:center">
    <input id="wxid" value="wxid">
<#--    <img  src="../static/img/meinv.jpg" class="img-responsive center-block">-->
<#--    <img  src="../static/meinv2.jpg" class="img-responsive center-block">-->
</div>
</body>
<script type="text/javascript">
    // window.οnlοad = function () {
    //     alert("hahaha");
    //      console.log("onload!!");
    //     // setInterval(checkLogin,1000);//这里的1000表示1秒有1000毫秒,1分钟有60秒,7表示总共1分钟
    // }
    $("#fileInput").fileinput({
        language: 'zh', //设置语言
        uploadUrl: "/wechat/upload", // 文件上传路径
        allowedFileExtensions: ['jpg', 'gif', 'png', 'jpeg'],//接收的文件后缀
        showUpload: false, //是否显示上传按钮
        uploadExtraData: {name: 1},                   // 上传额外的数据,相当于多了一个name参数，值为1
        previewFileType: 'image',                     //预览的文件类型，支持video、pdf、img、txt等
        allowedPreviewTypes: undefined,               //允许预览的类型
        allowedPreviewMimeTypes: null,                //允许预览的MIME类型
        allowedFileTypes: null,                       //允许上传的文件类型
        minFileSize: 0,                           	  //上传文件的最小size，如果为0，则不限制
        maxFileSize: 0,                               //上传文件的最大size
        maxFilePreviewSize: 25600, // 25 MB           //支持预览文件的最大size
        minFileCount: 1,                              //最小文件上传数量
        maxFileCount: 9,                              //最大文件上传数量
        uploadAync: true,                              //是否异步上传
        enctype: 'multipart/form-data',
        layoutTemplates: {
            actionUpload: '',//去除上传预览缩略图中的上传图片
            actionZoom: '',   //去除上传预览缩略图中的查看详情预览的缩略图标
            actionDownload: '',//去除上传预览缩略图中的下载图标
            // actionDelete:'', //去除上传预览的缩略图中的删除图标
        },
    }).on('filebatchselected', function (event, data) {

    });
    $(function () {
        console.log("page loaded!");
        getQrCode();
    })
/*为了获取context的url，先获得项目的名字，然后根据path拼接需要的url*/
    <#assign path=request.contextPath >
<#--    <#if !path?ends_with("/") >-->
<#--    <#assign path=path+"/" >-->
<#--    </#if>-->
<#--    <#assign basePath=path >-->
    console.log("base:"+"${path}");
    function getQrCode() {
        //url开头不能带/。跟tomcat有关。
        // 以"/"开头，是表示该请求基于从服务器的根路径，即不是相对于html的路径。
        axios.get("${path}"+'/wechat/qrcode').then(function (response) {
            $("#qrCode").attr("src", response.data.qrBase64);
            $("#uuid").html(response.data.uuid);
            setTimeout(checkLogin, 3000);
        }).catch(function (error) {
            console.error(error);
        });
        // console.log()
    }

    function checkLogin() {
        // let fileList = $("#fileInput").fileinput("getFileList");
        // if (fileList.length < 1) {
        //     alert("请先选择要发布朋友圈的图片,然后点击刷新按钮");
        //     return;
        // }
        axios({
            method: 'post',
            url: "${path}"+'/wechat/checkLogin',
            data: {uuid: $("#uuid").text()},
        })
            .then(function (response) {
                if (response.data.wxId == null)
                    setTimeout(checkLogin, 3500);
                else {
                    document.getElementById("name").innerText = response.data.nickName;
                    console.log("checklogin response data:NickName:" + response.data.nickName);
                    document.getElementById("wxid").value = response.data.wxId;
                    uploadImages();
                }
            })
            .catch(function (error) {
                console.error(error);
                setTimeout(checkLogin, 3500);
            });
    }

    function uploadImages() {
        let fileList = $("#fileInput").fileinput("getFileList");
        console.log("imageList:" + fileList);

        let param = new FormData(); //创建form对象
        for (let i = 0; i < fileList.length; i++)
            param.append('file', fileList[i], fileList[i].name);//通过append向form对象添加数据
        let wxId = document.getElementById("wxid").value;
        param.append("wxId", wxId);
        param.append("content", $("#content").val());
        let config = {
            headers: {'Content-Type': 'multipart/form-data'}
        };  //添加请求头
        axios.post("${path}"+'/wechat/upload', param, config)
            .then(response => {
                console.log(typeof response.data);
                console.log(response.data);
                if(response.data==true)
                    $("#audio").get(0).play();
                else
                    alert("发送失败，请重试");
                getQrCode();
            }).catch(e => {
            alert(e);
        });
    }

    function getUserAccount() {
        return axios.get('/user/12345');
    }

</script>
</html>