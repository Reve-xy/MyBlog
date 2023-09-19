package cn.ly.framework.service.impl;

import cn.ly.framework.config.ReveConfig;
import cn.ly.framework.constants.MqConstants;
import cn.ly.framework.constants.RedisKeyConstants;
import cn.ly.framework.constants.SystemConstants;
import cn.ly.framework.domain.dto.SendEmailDto;
import cn.ly.framework.domain.entity.User;
import cn.ly.framework.enums.HttpCodeEnum;
import cn.ly.framework.exception.SystemException;
import cn.ly.framework.mapper.UserMapper;
import cn.ly.framework.service.CaptchaService;
import cn.ly.framework.service.EmailService;
import cn.ly.framework.utils.RandomCodeGenerator;
import cn.ly.framework.utils.RedisCache;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/5/25 15:21
 */
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
   @Resource
    RocketMQTemplate rocketMQTemplate;

    @Resource
    RedisCache redisCache;

    @Resource
    CaptchaService captchaService;

    @Override
    public void sendEmailCode(SendEmailDto sendEmailDto) {
        String captcha = sendEmailDto.getCaptcha();
        String email = sendEmailDto.getEmail().trim();
        String uuid = sendEmailDto.getUuid();
        if (StringUtils.hasText(captcha)) {
            captcha = captcha.trim();
            captchaService.verifyCaptcha(uuid, captcha);
        }
        if (StringUtils.hasText(email)) {
            sendEmailDto.setEmail(email.trim());
        }
        rocketMQTemplate.asyncSend(MqConstants.MAIL_TOPIC, sendEmailDto, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info(MqConstants.MAIL_TOPIC+"--->SUCCESS");
            }
            @Override
            public void onException(Throwable throwable) {
                log.error(MqConstants.MAIL_TOPIC+"--->FAIL");
            }
        });
    }

    /**
     * @param email
     * @param captcha
     * @return void
     * @date 2023/5/25 13:08
     * @description 邮箱验证码不能一次性使用，只能等待他过期
     */
    @Override
    public void verifyEmailCaptcha(String email, String captcha) {
        String captchaKey = RedisKeyConstants.EMAIL_CAPTCHA_KEY + email;
        String captchaCache = (String) redisCache.getCacheObject(captchaKey);
        if (!StringUtils.hasText(captchaCache)) {
            throw new SystemException(HttpCodeEnum.CAPTCHA_EXPIRE);
        } else if (!captcha.equals(captchaCache)) {
            throw new SystemException(HttpCodeEnum.CAPTCHA_ERR);
        }
    }

    @Resource
    UserMapper userMapper;

    @Override
    public List<User> verifyEmail(String email) {
        return userMapper.selectList(Wrappers.<User>lambdaQuery().eq(User::getEmail, email).select(User::getEmail));
    }
}
