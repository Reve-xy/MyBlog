package cn.ly.framework.service.impl;

import cn.ly.framework.annotation.CacheFind;
import cn.ly.framework.constants.RedisKeyConstants;
import cn.ly.framework.constants.SystemConstants;
import cn.ly.framework.domain.dto.AddArticleDto;
import cn.ly.framework.domain.dto.ArticleDto;
import cn.ly.framework.domain.entity.ArticleTag;
import cn.ly.framework.domain.entity.Category;
import cn.ly.framework.domain.entity.Article;
import cn.ly.framework.domain.vo.*;
import cn.ly.framework.enums.HttpCodeEnum;
import cn.ly.framework.exception.SystemException;
import cn.ly.framework.service.ArticleTagService;
import cn.ly.framework.service.TagService;
import cn.ly.framework.utils.RedisCache;
import cn.ly.framework.mapper.ArticleMapper;
import cn.ly.framework.service.ArticleService;
import cn.ly.framework.service.CategoryService;
import cn.ly.framework.utils.BeanCopyUtils;
import cn.ly.framework.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文章表(Article)表服务实现类
 *
 * @author Rêve
 * @since 2023-04-05 10:44:32
 */
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Resource
    CategoryService categoryService;

    @Resource
    ArticleTagService articleTagService;

    @Resource
    TagService tagService;
    @Override
    public List<HotArticleVo> hotArticleList() {
        // 热度最高的十个帖子,且文章必须是已发布的
        Page<Article> page = PageHelper.startPage(1, 10);

        List<Article> list = this.list(new LambdaQueryWrapper<Article>().eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL).orderByDesc(Article::getViewCount));
        // 将分页结果打包
        PageInfo<Article> articlePageInfo = new PageInfo<>(list);
        List<Article> articlePageInfoList = articlePageInfo.getList();

        // bean拷贝
        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(articlePageInfoList, HotArticleVo.class);
        return hotArticleVos;
    }


    @Override
    public PageVo getArticleList(Integer pageNum, Integer pageSize, Long categoryId,String keyWords) {

        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 若传入了分类ID，则进行分类统计，且要按照isTop进行降序排列
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0, Article::getCategoryId, categoryId)
                .eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL)
                .like(StringUtils.hasText(keyWords),Article::getTitle,keyWords)
                .orderByDesc(Article::getIsTop);

        Page<Article> page = PageHelper.startPage(pageNum, pageSize);
        List<Article> list = this.list(lambdaQueryWrapper);
        PageInfo<Article> articlePageInfo = new PageInfo<>(list);

        //取分页结果list
        List<Article> articlePageInfoList = articlePageInfo.getList();
        // 将categoryId转换为categoryName，方便后面的bean拷贝
        articlePageInfoList.stream()
                .map(article -> article.setCategoryName(getCategoryName(article)))
                .collect(Collectors.toList());

        // bean拷贝
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articlePageInfoList, ArticleListVo.class);
        // 包装成pageVo并返回给前端
        PageVo pageVo = new PageVo(articleListVos, articlePageInfo.getTotal());
        return pageVo;
    }

    //获取分类ID
    String getCategoryName(Article article){
        Category one = categoryService.getOne(Wrappers.<Category>lambdaQuery().
                eq(Category::getStatus, SystemConstants.CATEGORY_STATUS_NORMAL)
                .eq(Objects.nonNull(article.getCategoryId()), Category::getId, article.getCategoryId()));
        if(Objects.nonNull(one)){
            return one.getName();
        }
        return null;
    }
    String getTagName(ArticleTag articleTag){
        String name = tagService.getById(articleTag.getTagId()).getName();
        if(Objects.nonNull(name)){
            return name;
        }
        return null;
    }

    @Override
    // @CacheFind(key = RedisKeyConstants.ARTICLE_DETAIL_KEY)
    public ArticleDetailVo getArticleDetail(Long id) {
        Article article = this.getById(id);
        if(Objects.isNull(article)||!article.getStatus().equals(SystemConstants.ARTICLE_STATUS_NORMAL)){
            throw new SystemException(HttpCodeEnum.ARTICLE_ERROR);
        }
        Integer viewCount = redisCache.getCacheMapValue(RedisKeyConstants.ARTICLE_VIEW_COUNT, id.toString());
        if(Objects.isNull(viewCount)){
            article.setViewCount(0L);
        }else{
            article.setViewCount(viewCount.longValue());
        }

        //获取分类名
        Category category = categoryService.getById(article.getCategoryId());
        if(category!=null&&category.getStatus().equals(SystemConstants.CATEGORY_STATUS_NORMAL)){
            article.setCategoryName(category.getName());
        }
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //获取标签tag
        List<ArticleTag> articleTags = articleTagService.getBaseMapper().selectList(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId,id));
        List<String> tags = articleTags.stream().map(articleTag ->getTagName(articleTag)).collect(Collectors.toList());
        articleDetailVo.setTagNames(tags);
        return articleDetailVo;
    }

    @Resource
    RedisCache redisCache;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateViewCount(Long id) {
        redisCache.incrementCacheMapValue(RedisKeyConstants.ARTICLE_VIEW_COUNT,id.toString(),1);
        return Result.success();
    }

    @Override
    @CacheFind(key = RedisKeyConstants.ARTICLE_INFO_KEY)
    public Result getArticleInfo(Long id) {
        Article article =getById(id);
        ArticleVo articleVo = BeanCopyUtils.copyBean(article, ArticleVo.class);
        List<ArticleTag> articleTags = articleTagService.getBaseMapper().selectList(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, article.getId()));
        List<Long> tags = articleTags.stream().map(articleTag -> articleTag.getTagId()).collect(Collectors.toList());
        articleVo.setTags(tags);
        return Result.success(articleVo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result editArticle(ArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        updateById(article);
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,article.getId());
        articleTagService.remove(articleTagLambdaQueryWrapper);
        //添加新的博客和标签的关联信息
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(articleDto.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return Result.success();
    }

    @Override
    public PageVo selectArticlePage(Article article, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.hasText(article.getTitle()),Article::getTitle, article.getTitle());
        queryWrapper.eq(Objects.nonNull(article.getCategoryId()),Article::getCategoryId, article.getCategoryId());

        PageHelper.startPage(pageNum,pageSize);
        List<Article> list = list(queryWrapper);
        //转换成VO
        PageInfo<Article> articlePageInfo = new PageInfo<>(list);

        List<ArticlePageInfoVo> articlePageInfoVos = BeanCopyUtils.copyBeanList(articlePageInfo.getList(), ArticlePageInfoVo.class);
        PageVo pageVo = new PageVo();
        pageVo.setTotal(articlePageInfo.getTotal());
        pageVo.setRows(articlePageInfoVos);
        return pageVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addArticle( AddArticleDto addArticleDto) {
        Article article = BeanCopyUtils.copyBean(addArticleDto, Article.class);
        save(article);
        List<ArticleTag> articleTags = addArticleDto.getTags().stream().map(tag -> new ArticleTag(article.getId(), tag)).collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return Result.success();
    }
}

