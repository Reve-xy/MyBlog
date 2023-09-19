import request from '@/utils/request'

// 登录
export function userLogin(data) {
    return request({
        url: '/login',
        method: 'post',
        headers: {
            isToken: false
          },
        data
    })
}

export function userRegister(data) {
    return request({
        url: '/user/register',
        method: 'post',
        headers: {
            isToken :false
        },
        data
    })
}

// 获取验证码
export function getCaptcha() {
  return request({
    url: '/captchaImage',
    headers: {
      isToken: false
    },
    method: 'get',
    timeout: 20000
  })
}

export function sendEmailCode(data){
  return request({
    url:'/sendEmailCode',
    headers:{
      isToken:false
    },
    method:'post',
    data
  })
}
export function checkEmailCaptcha(data){
  return request({
    url:'/verifyEmailCaptcha',
    headers:{
      isToken:false
    },
    method:'post',
    data
  })
}
export function resetPwd(data){
  return request({
    url:'/user/resetPwd',
    headers:{
      isToken:false
    },
    method:'post',
    data
  })
}
export function logout() {
    return request({
        url: '/logout',
        method: 'post'
    })
}

export function getUserInfo(userId) {
    return request ({
        url: '/user/userInfo',
        method: 'get',
        params: {"userId":userId}
    })
}


export function savaUserInfo(userinfo) {
    return request({
        url: '/user/userInfo',
        method: 'put',
        data: userinfo
    })
}

export function checkEmail(email){
  return request ({
    url: '/user/checkEmail',
    method: 'post',
    params: {email}
  })
}

export function checkUserName(userName){
  return request ({
    url: '/user/checkUserName',
    method: 'post',
    params: {userName}
  })
}

