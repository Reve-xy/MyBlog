package cn.ly.framework.utils;

import cn.ly.framework.domain.entity.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/11 14:29
 */
public class PathUtils {

    public static String generateFilePath(String fileName){
        //根据日期生成路径   2022/1/15/
        SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/");
        String datePath = sdf.format(new Date());
        //uuid作为文件名
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        //取文件后缀
        int index = fileName.lastIndexOf(".");
        String fileType = fileName.substring(index);
        User user = SecurityUtils.getLoginUser().getUser();

        return new StringBuilder()
                .append(user.getId()).append("_").append(user.getUserName()).append(datePath).append(uuid).append(fileType).toString();
    }
}