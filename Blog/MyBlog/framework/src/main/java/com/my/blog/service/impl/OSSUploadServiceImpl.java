package com.my.blog.service.impl;

import com.google.gson.Gson;
import com.my.blog.domain.ResponseResult;
import com.my.blog.enums.AppHttpCodeEnum;
import com.my.blog.exception.SystemException;
import com.my.blog.service.IUploadService;
import com.my.blog.utils.PathUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

@Service
@Data
@ConfigurationProperties(prefix = "oss")
public class OSSUploadServiceImpl implements IUploadService {
    private String accessKey ;
    private String secretKey ;
    private String bucket ;
    private String host;
    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        String filename = img.getOriginalFilename();
        if(!filename.endsWith(".png") && !filename.endsWith(".jpg")){
            throw new SystemException(AppHttpCodeEnum.IMG_TYPE_ERROR);
        }
        //生成一个文件路径
        String path = PathUtil.generateFilePath(filename);

        String url = ossUploadImg(img, path);
        return ResponseResult.okResult(url);
    }

    private String ossUploadImg(MultipartFile img, String path) {
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        // String accessKey = "your access key";
        // String secretKey = "your secret key";
        // String bucket = "your bucket name";
        String key = path;
        try {
            InputStream inputStream = img.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return host + path;
            } catch (QiniuException ex) {
               ex.printStackTrace();
               if(ex.response !=null){
                   System.err.println(ex.response);

                   try {
                       String body = ex.response.toString();
                       System.err.println(body);
                   }catch (Exception ignored){
                   }
               }
            }
        } catch (UnsupportedEncodingException ex) {
            //ignore
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
