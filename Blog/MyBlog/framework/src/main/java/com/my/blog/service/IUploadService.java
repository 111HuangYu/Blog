package com.my.blog.service;

import com.my.blog.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadService {
    ResponseResult uploadImg(MultipartFile img);
}
