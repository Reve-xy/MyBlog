package cn.ly.admin.controller;

import cn.ly.framework.annotation.SystemLog;
import cn.ly.framework.service.UploadService;
import cn.ly.framework.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/11 12:42
 */
@RestController
public class UploadController {
    @Resource(name="localUploadService")
    UploadService uploadService;

    @PostMapping("/upload")
    @SystemLog("上传图片")
    public Result uploadImg(@RequestParam("img") MultipartFile img){
        return uploadService.uploadImg(img);
    }


}

