package cn.ly.framework.service.impl;

import cn.ly.framework.constants.RedisKeyConstants;
import cn.ly.framework.enums.HttpCodeEnum;
import cn.ly.framework.exception.SystemException;
import cn.ly.framework.service.CaptchaService;
import cn.ly.framework.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/5/25 15:25
 */
@Service
@Slf4j
public class CaptchaServiceImpl implements CaptchaService {
    @Resource
    RedisCache redisCache;
    /**
     * @param uuid
     * @param captcha
     * @return void
     * @date 2023/5/3 21:13
     * @description 校验验证码
     */
    @Override
    public void verifyCaptcha(String uuid, String captcha) {
        String captchaKey = RedisKeyConstants.CAPTCHA_KEY + uuid;
        String captchaCache = (String) redisCache.getCacheObject(captchaKey);
        if (!StringUtils.hasText(captchaCache)) {
            throw new SystemException(HttpCodeEnum.CAPTCHA_EXPIRE);
        } else if (!captcha.equals(captchaCache)) {
            // 一次性使用
            redisCache.deleteObject(captchaKey);
            throw new SystemException(HttpCodeEnum.CAPTCHA_ERR);
        }
    }
}
