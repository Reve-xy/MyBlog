package cn.ly.framework.service;

import cn.ly.framework.utils.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/11 12:47
 */
public interface UploadService {
    /**
     * @param img
     * @return Result
     * @date 2023/4/11 12:47
     * @description 上传图片
     */
    Result uploadImg(MultipartFile img);
    Result uploadAvatar(MultipartFile avatar);
}
