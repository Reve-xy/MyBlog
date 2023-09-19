package cn.ly.framework.service.impl;


import cn.ly.framework.domain.entity.User;
import cn.ly.framework.enums.HttpCodeEnum;
import cn.ly.framework.exception.SystemException;
import cn.ly.framework.mapper.UserMapper;
import cn.ly.framework.service.UploadService;
import cn.ly.framework.utils.PathUtils;
import cn.ly.framework.utils.Result;
import cn.ly.framework.utils.SecurityUtils;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.Arrays;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/11 12:47
 */
@Service("cloudUploadService")
@Data
@Slf4j
@ConfigurationProperties(prefix = "oss")
public class CloudUploadServiceImpl implements UploadService {
    private String accessKey;
    private String secretKey;
    private String bucket;

    private String cdnHttp;
    private  final  String[] IMG_TYPE={".jpg",".png"};
    @Resource
    UserMapper userMapper;
    @Override
    public Result uploadImg(MultipartFile img) {
        //获取原始文件名
        String originalFilename = img.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        //进行筛选
        if(Arrays.binarySearch(IMG_TYPE,substring)<0){
            throw new SystemException(HttpCodeEnum.IMG_TYPE_ERROR);
        }
        //如果通过上传文件到OSS
        String filePath = PathUtils.generateFilePath(originalFilename);
        String url = uploadOss(img,filePath);//  2099/2/3/wqeqeqe.png
        return Result.success(url);
    }

    @Override
    public Result uploadAvatar(MultipartFile avatar) {
        //获取原始文件名
        String originalFilename = avatar.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        //进行筛选
        if(Arrays.binarySearch(IMG_TYPE,substring)<0){
            throw new SystemException(HttpCodeEnum.IMG_TYPE_ERROR);
        }
        //如果通过上传文件到OSS
        String filePath = PathUtils.generateFilePath(originalFilename);
        String url = uploadOss(avatar,filePath);//  2099/2/3/wqeqeqe.png
        User user = new User();
        user.setId(SecurityUtils.getUserId());
        user.setAvatar(url);
        userMapper.updateById(user);
        return Result.success(url);
    }
    private String uploadOss(MultipartFile imgFile, String filePath){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;

        try {
            InputStream inputStream = imgFile.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return cdnHttp+key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
                throw new SystemException(HttpCodeEnum.FAILURE_UPLOAD);
            }
        } catch (Exception ex) {
            //ignore
            throw new SystemException(HttpCodeEnum.FAILURE_UPLOAD);
        }
    }
}
