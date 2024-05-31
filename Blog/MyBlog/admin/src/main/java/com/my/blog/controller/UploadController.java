package com.my.blog.controller;

import com.my.blog.domain.ResponseResult;
import com.my.blog.service.IUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @Autowired
    IUploadService uploadService;
    @PostMapping("/upload")
    @ResponseBody
    public ResponseResult uploadImg(MultipartFile img){
        return uploadService.uploadImg(img);
    }
}

