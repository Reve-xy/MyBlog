package cn.ly.blog.controller;

import cn.ly.framework.annotation.SystemLog;
import cn.ly.framework.constants.RedisKeyConstants;
import cn.ly.framework.domain.dto.SendEmailDto;
import cn.ly.framework.domain.dto.VerifyEmailCodeDto;
import cn.ly.framework.domain.entity.User;
import cn.ly.framework.enums.HttpCodeEnum;
import cn.ly.framework.exception.SystemException;
import cn.ly.framework.service.EmailService;
import cn.ly.framework.utils.RedisCache;
import cn.ly.framework.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/5/25 15:19
 */
@RestController
public class EmailController{
    
    @Resource
    EmailService emailService;
    @Resource
    RedisCache redisCache;
    /**
     * @param sendEmailDto
     * @return Result
     * @date 2023/5/25 12:51
     * @description 根据输入的验证码发送邮箱验证码到邮箱
     */
    @PostMapping("/sendEmailCode")
    @SystemLog("发送邮箱验证码")
    public Result sendEmailCode(@RequestBody @Valid SendEmailDto sendEmailDto){
        emailService.sendEmailCode(sendEmailDto);
        return Result.success();
    }


    @GetMapping("/verifyEmail")
    @SystemLog("验证邮箱是否存在")
    public Result verifyEmail(@RequestParam(value = "email") String email){
        List<User> users = emailService.verifyEmail(email);
        if(users.size()==0){
            throw new SystemException(HttpCodeEnum.NO_EXIST_EMAIL);
        }
        return Result.success();
    }

    @PostMapping("/verifyEmailCaptcha")
    @SystemLog("验证邮箱验证码是否正确")
    public Result verifyEmailCaptcha(@RequestBody @Valid VerifyEmailCodeDto verifyEmailCodeDto){
        String email = verifyEmailCodeDto.getEmail();
        String captcha = verifyEmailCodeDto.getCaptcha();
        emailService.verifyEmailCaptcha(email, captcha);
        //验证通过，存储当前邮箱到Redis重置密码凭证区中,凭证五分钟过期
        redisCache.setCacheObject(RedisKeyConstants.RESET_EMAIL_KEY+email,email,5, TimeUnit.MINUTES);
        return Result.success();
    }
}
