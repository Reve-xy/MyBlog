package cn.ly.framework.service;

import cn.ly.framework.domain.dto.AddArticleDto;
import cn.ly.framework.domain.dto.ArticleDto;
import cn.ly.framework.domain.vo.ArticleDetailVo;
import cn.ly.framework.domain.vo.HotArticleVo;
import cn.ly.framework.domain.entity.Article;
import cn.ly.framework.domain.vo.PageVo;
import cn.ly.framework.utils.Result;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * 文章表(Article)表服务接口
 *
 * @author Rêve
 * @since 2023-04-05 10:44:32
 */
public interface ArticleService extends IService<Article> {

    /**
     * @param 
     * @return List<HotArticleVo>
     * @date 2023/4/5 16:54
     * @description 查询热门文章
     */
    List<HotArticleVo> hotArticleList();

    /**
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return PageVo
     * @date 2023/4/5 16:54
     * @description 分页查询文章，还可根据分类查询
     */
    PageVo getArticleList(Integer pageNum, Integer pageSize, Long categoryId,String keyWords);

    /**
     * @param id
     * @return ArticleDetailVo
     * @date 2023/4/6 10:42
     * @description 根据文章ID搜寻
     */
    ArticleDetailVo getArticleDetail(Long id);

    /**
     * @param id
     * @return Result
     * @date 2023/4/12 23:27
     * @description 更新浏览量
     */
    Result updateViewCount(Long id);

    Result addArticle( AddArticleDto addArticleDto);

    Result getArticleInfo(Long id);

    Result editArticle(ArticleDto articleDto);

    PageVo selectArticlePage(Article article, Integer pageNum, Integer pageSize);
}
