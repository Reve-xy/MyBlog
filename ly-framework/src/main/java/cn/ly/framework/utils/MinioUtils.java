package cn.ly.framework.utils;


import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.ly.framework.config.MinioConfig;
import cn.ly.framework.enums.HttpCodeEnum;
import com.google.common.collect.HashMultimap;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

@Slf4j
@Component
public class MinioUtils {
    
    @Resource
    MinioConfig minioConfig;
    @Resource
    MinioClient client;

    private final String defaultContentType="application/octet-stream";



    /**
     * 获取文件下载地址
     *
     * @param fileName   文件名
     * @return
     */
    public String getFileUrl(String fileName) {
        return StrUtil.format("{}/{}/{}", minioConfig.getEndpoint(), minioConfig.getBucketName(), fileName);//文件访问路径
    }

    /**
     * 单文件直传
     *
     * @param file 文件
     * @return Boolean
     */
    public String upload(MultipartFile file,String fileName) throws Exception{
        if (!StringUtils.hasText(fileName)) {
            throw new RuntimeException();
        }
        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(minioConfig.getBucketName()).object(fileName)
                    .stream(file.getInputStream(), file.getSize(), -1).contentType(file.getContentType()).build();
            //文件名称相同会覆盖
            client.putObject(objectArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        // 查看文件地址
        GetPresignedObjectUrlArgs build = new GetPresignedObjectUrlArgs().builder()
                .bucket(minioConfig.getBucketName()).object(fileName).method(Method.GET).build();
        String url = null;
        url = client.getPresignedObjectUrl(build);
    
        return url;
    }

    /**
     * 缩略图上传
     * @param inputStream
     * @param fileName
     * @throws Exception
     */
    public String uploadFile(InputStream inputStream, String fileName) throws Exception {
        // 开始上传
        PutObjectArgs args = PutObjectArgs.builder().bucket(minioConfig.getBucketName()).object(fileName)
                .stream(inputStream,inputStream.available(),-1).build();
        client.putObject(args);
        GetPresignedObjectUrlArgs build = new GetPresignedObjectUrlArgs().builder()
                .bucket(minioConfig.getBucketName()).object(fileName).method(Method.GET).build();
        String url = null;
        url = client.getPresignedObjectUrl(build);
        return url;
    }


    /**
     * @param fileName
     * @param response
     * @return void
     * @date 2023/7/5 16:58
     * @description 下载文件
     */
    public void downloadFile(String fileName,
                             HttpServletResponse response) {

        InputStream inputStream   = null;
        OutputStream outputStream = null;

        try {
            outputStream = response.getOutputStream();
            // 获取文件对象
            inputStream =client.getObject(GetObjectArgs.builder().bucket(minioConfig.getBucketName()).object(fileName).build());
            if(inputStream==null){
                WebUtils.renderString(response,"文件下载失败，文件不存在");
            }
            byte buf[] = new byte[1024];
            int length = 0;
            response.reset();
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    URLEncoder.encode(fileName.substring(fileName.lastIndexOf("/") + 1), "UTF-8"));
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            // 输出文件
            while ((length = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, length);
            }
            log.info("下载文件{}成功",fileName);
            inputStream.close();
        } catch (Throwable ex) {
           WebUtils.renderString(response,"下载出现异常");
        } finally {
            try {
                outputStream.close();
                if (inputStream != null) {
                    inputStream.close();
                }}catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取文件流
     * @param fileName
     * @return
     * @throws Exception
     */
    public InputStream getInputStreamFromMinio(String fileName) throws Exception {
        return client.getObject(GetObjectArgs.builder().bucket(minioConfig.getBucketName()).object(fileName).build());
    }

    /**
     * 获取文件的 MimeType
     * @param fileUrl
     * @return
     */
    public  String getMimeType(String fileUrl)
    {
        return HttpUtil.getMimeType(fileUrl,defaultContentType);
    }
}