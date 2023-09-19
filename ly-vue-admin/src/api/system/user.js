import request from '@/utils/request'
import { praseStrEmpty } from '@/utils/sg'

// 查询用户列表
export function listUser(query) {
  return request({
    url: '/system/user/list',
    method: 'get',
    params: query
  })
}
// 查询用户详细
export function getUser(userId) {
  return request({
    url: `/system/user/${praseStrEmpty(userId)}`,
    method: 'get'
  })
}

export function getProfile() {
  return request({
    url: '/system/user/profile',
    method: 'get'
  })
}

// 新增用户
export function addUser(data) {
  return request({
    url: '/system/user',
    method: 'post',
    data: data
  })
}
// 删除用户
export function delUser(ids) {
  return request({
    url: `/system/user`,
    method: 'delete',
    data: ids
  })
}
// 用户密码重置
export function resetUserPwd(userId, password) {
  const data = {
    userId,
    password
  }
  return request({
    url: '/system/user/resetPwd',
    method: 'put',
    data: data
  })
}

export function updateUserPwd(data) {
  return request({
    url: '/system/user/updateUserPwd',
    method: 'put',
    data
  })
}
// 上传头像
export function uploadAvatar(formData) {
  return request({
    url: '/system/user/uploadAvatar',
    method: 'post',
    data: formData
  })
}

/**
 * 修改自己的信息
 * @param data
 * @returns {*}
 */
export function updateLoginUser(data) {
  return request({
    url: '/system/user/updateLoginUser',
    method: 'put',
    data
  })
}

// 修改用户
export function updateUser(data) {
  console.log(data)
  return request({
    url: '/system/user',
    method: 'put',
    data: data
  })
}
// 用户状态修改
export function changeUserStatus(id, status) {
  const data = {
    id,
    status
  }
  return request({
    url: '/system/user/changeStatus',
    method: 'put',
    data
  })
}
