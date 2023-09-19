package cn.ly.framework.service.impl;

import cn.ly.framework.domain.entity.User;
import cn.ly.framework.enums.HttpCodeEnum;
import cn.ly.framework.exception.SystemException;
import cn.ly.framework.mapper.UserMapper;
import cn.ly.framework.service.UploadService;
import cn.ly.framework.utils.MinioUtils;
import cn.ly.framework.utils.PathUtils;
import cn.ly.framework.utils.Result;
import cn.ly.framework.utils.SecurityUtils;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/7/14 11:09
 */
@Service("localUploadService")
public class LocalUploadServiceImpl implements UploadService {
    @Resource
    MinioUtils minioUtils;
    @Resource
    UserMapper userMapper;
    private  final  String[] IMG_TYPE={".jpg",".png"};

    @Override
    public Result uploadImg(MultipartFile img) {
        String filePath = checkFileType(img);
        try {
           minioUtils.upload(img,filePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Result.success(minioUtils.getFileUrl(filePath));
    }

    @Override
    public Result uploadAvatar(MultipartFile avatar) {
        String filePath = checkFileType(avatar);
        try {
            minioUtils.upload(avatar,filePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String url = minioUtils.getFileUrl(filePath);
        User user = new User();
        user.setId(SecurityUtils.getUserId());
        user.setAvatar(url);
        userMapper.updateById(user);
        return Result.success(url);
    }

    String checkFileType(MultipartFile avatar){
        //获取原始文件名
        String originalFilename = avatar.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        //进行筛选
        if(Arrays.binarySearch(IMG_TYPE,substring)<0){
            throw new SystemException(HttpCodeEnum.IMG_TYPE_ERROR);
        }
        //如果通过上传文件到OSS
        String filePath = PathUtils.generateFilePath(originalFilename);
        return filePath;
    }
}
