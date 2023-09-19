package cn.ly.framework.service.impl;

import cn.ly.framework.annotation.CacheFind;
import cn.ly.framework.constants.RedisKeyConstants;
import cn.ly.framework.constants.SystemConstants;
import cn.ly.framework.domain.entity.Article;
import cn.ly.framework.domain.entity.Category;
import cn.ly.framework.domain.vo.CategoryPageInfoVO;
import cn.ly.framework.domain.vo.CategoryVo;
import cn.ly.framework.domain.vo.PageVo;
import cn.ly.framework.mapper.ArticleMapper;
import cn.ly.framework.utils.BeanCopyUtils;
import cn.ly.framework.mapper.CategoryMapper;
import cn.ly.framework.service.CategoryService;
import cn.ly.framework.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 分类表 服务实现类
 * </p>
 *
 * @author 刘易
 * @since 2023-04-05
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    CategoryMapper categoryMapper;
    @Resource
    ArticleMapper articleMapper;

    /**
     * @param
     * @return List<CategoryVo>
     * @date 2023/6/8 23:15
     * @description 获取分类列表
     */
    @Override
    public  List<CategoryVo> getCategoryList() {
        //通过mapper检索分类列表
        List<Category> categoryList = categoryMapper.getCategoryList(SystemConstants.ARTICLE_STATUS_NORMAL);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categoryList, CategoryVo.class);
        return categoryVos;
    }

    /**
     * @param name
     * @param status
     * @param pageNum
     * @param pageSize
     * @return Result
     * @date 2023/6/8 23:25
     * @description 分页获取分类
     */
    @Override
    public Result selectCategoryPage(String name, String status, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(StringUtils.hasText(name),Category::getName,name);
        queryWrapper.eq(Objects.nonNull(status),Category::getStatus, status);

        PageHelper.startPage(pageNum,pageSize);
        List<Category> list = list(queryWrapper);

        PageInfo<Category> categoryPageInfo = new PageInfo<>(list);
        List<Category> categories = categoryPageInfo.getList().stream().map(m ->
                m.setArticleCount(articleMapper.selectCount(
                        Wrappers.<Article>lambdaQuery().eq(Article::getCategoryId, m.getId())).longValue())).collect(Collectors.toList());
        List<CategoryPageInfoVO> categoryPageInfoVOS = BeanCopyUtils.copyBeanList(categories, CategoryPageInfoVO.class);
        return Result.success(new PageVo<>(categoryPageInfoVOS,categoryPageInfo.getTotal()));
    }

}
