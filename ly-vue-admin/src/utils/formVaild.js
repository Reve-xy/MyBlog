/**
 * @author Rêve
 * @date 2023/4/20 10:30
 * @description
 */
// 用户名匹配
export function loginNameRule(rule, value, callback) {
  // 5到19位(字母，数字，下划线，减号)，字符开头
  const regName = /^[a-zA-Z][-_a-zA-Z0-9]{5,19}$/
  if (value === '') {
    callback(new Error('请输入用户名'))
  } else if (!regName.test(value)) {
    callback(new Error('请输入正确的用户名'))
  } else {
    callback()
  }
}
// 密码匹配
export function loginPassRule(rule, value, callback) {
  // 8-18位密码，数字加英文
  const regPass = /^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z0-9]{8,18}$/
  if (value === '') {
    callback(new Error('请输入密码'))
  } else if (!regPass.test(value)) {
    callback(new Error('请输入正确的密码'))
  } else {
    callback()
  }
}

