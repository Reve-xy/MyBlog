package cn.ly.framework.service.impl;


import cn.ly.framework.domain.entity.UserRole;
import cn.ly.framework.mapper.UserRoleMapper;
import cn.ly.framework.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户和角色关联表 服务实现类
 * </p>
 *
 * @author 刘易
 * @since 2023-04-22
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
