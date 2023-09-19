package cn.ly.framework.service.impl;

import cn.ly.framework.annotation.CacheFind;
import cn.ly.framework.constants.RedisKeyConstants;
import cn.ly.framework.constants.SystemConstants;
import cn.ly.framework.domain.entity.Link;
import cn.ly.framework.domain.vo.LinkVo;
import cn.ly.framework.domain.vo.PageVo;
import cn.ly.framework.mapper.LinkMapper;
import cn.ly.framework.service.LinkService;
import cn.ly.framework.utils.BeanCopyUtils;
import cn.ly.framework.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 友链 服务实现类
 * </p>
 *
 * @author 刘易
 * @since 2023-04-06
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {



    @Override
    public List<LinkVo> getAllLink() {
        //查询审核通过后的友链
        LambdaQueryWrapper<Link> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);

        List<Link> linkList = this.list(lambdaQueryWrapper);
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(linkList, LinkVo.class);

        return linkVos;
    }

    @Override
    public PageVo selectLinkPage(Link link, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(StringUtils.hasText(link.getName()),Link::getName,link.getName());
        queryWrapper.eq(Objects.nonNull(link.getStatus()),Link::getStatus, link.getStatus());

        PageHelper.startPage(pageNum,pageSize);
        List<Link> list = list(queryWrapper);
        PageInfo<Link> categoryPageInfo = new PageInfo<>(list);

        return new PageVo<>(categoryPageInfo.getList(),categoryPageInfo.getTotal());
    }
}
