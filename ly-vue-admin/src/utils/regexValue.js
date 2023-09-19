/**
 * @author Rêve
 * @date 2023/6/4 19:41
 * @description
 */

export default {
  email_regex: {
    regex: /^([A-Za-z0-9_\-\.\u4e00-\u9fa5])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,8})$$/,
    message: '请输入正确的邮箱'
  },
  pwd_regex: {
    regex: /^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z0-9]{8,18}$/,
    message: '请输入包括大小写字符与数字的密码'
  },
  userName_regex: {
    regex: /^[a-zA-Z0-9_-]{5,19}$/,
    message: '用户名只能包括英文字母、数字、-、_'
  }
}
