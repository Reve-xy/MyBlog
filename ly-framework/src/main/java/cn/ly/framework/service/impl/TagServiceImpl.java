package cn.ly.framework.service.impl;

import cn.ly.framework.annotation.CacheFind;
import cn.ly.framework.constants.RedisKeyConstants;
import cn.ly.framework.domain.dto.TagListDto;
import cn.ly.framework.domain.entity.Tag;
import cn.ly.framework.domain.vo.PageVo;
import cn.ly.framework.domain.vo.TagVo;
import cn.ly.framework.mapper.TagMapper;
import cn.ly.framework.service.TagService;
import cn.ly.framework.utils.BeanCopyUtils;
import cn.ly.framework.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 标签 服务实现类
 * </p>
 *
 * @author 刘易
 * @since 2023-04-14
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public Result pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        LambdaQueryWrapper<Tag> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName()).like(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
        PageHelper.startPage(pageNum,pageSize);
        List<Tag> tagList = list(lambdaQueryWrapper);
        PageInfo<Tag> tagPageInfo = new PageInfo<>(tagList);
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(tagPageInfo.getList(), TagVo.class);
        PageVo pageVo=new PageVo(tagVos,tagPageInfo.getTotal());
        return Result.success(pageVo);
    }

    @Override
    public Result listAllTag() {
        LambdaQueryWrapper<Tag> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(Tag::getId,Tag::getName);
        List<Tag> list = list(lambdaQueryWrapper);
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(list, TagVo.class);
        return Result.success(tagVos);
    }
}
