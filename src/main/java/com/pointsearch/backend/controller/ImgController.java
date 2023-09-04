package com.pointsearch.backend.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.google.gson.Gson;
import com.pointsearch.backend.common.BaseResponse;
import com.pointsearch.backend.common.ImgKBResponseData;
import com.pointsearch.backend.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件接口
 */
@RestController
@RequestMapping("/img")
@Slf4j
public class ImgController {

    private final static String Token = "上传图片的token";

    private final static String UPLOAD_PATH = "上传图片的地址";

    @PostMapping("/upload")
    public BaseResponse<String> uploadImage(@RequestParam("avatar") MultipartFile imageFile) {
        try {
            // 检查文件是否为空
            if (imageFile.isEmpty()) {
                return ResultUtils.success("上传的文件为空");
            }

            // 执行文件保存操作
            // 这里可以将文件保存到指定的位置，例如本地磁盘或云存储服务
            File file = new File(imageFile.getOriginalFilename());
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            StreamUtils.copy(imageFile.getInputStream(), fileOutputStream);
            fileOutputStream.close();

            String imageUrl = uploadImageToImgKb(file);

            // 返回成功响应
            return ResultUtils.success(imageUrl);
        } catch (Exception e) {
            // 处理异常情况
            return ResultUtils.success("文件上传失败");
        }
    }

    private static String uploadImageToImgKb(File imageFile) throws Exception {

        Map<String, Object> params = new HashMap<>();
        params.put("image", imageFile);

        HttpResponse response = HttpRequest.post(UPLOAD_PATH)
                .header("Token", Token)
                .form(params)
                .execute();

        if (response.isOk()) {
            // 解析响应结果，获取上传后的图片 URL
            String json = response.body();
            // 这里根据实际的响应格式进行解析，示例中假设响应是 JSON 格式，包含一个 "url" 字段
            Gson gson = new Gson();
            ImgKBResponseData imgKBResponseData = gson.fromJson(json, ImgKBResponseData.class);
            System.out.println(imgKBResponseData.getData().getUrl());

            // 返回上传后的图片 URL
            return imgKBResponseData.getData().getUrl();
        } else {
            throw new Exception("Failed to upload image. Response: " + response);
        }
    }
}
