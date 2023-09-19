package cn.ly.blog.runner;

import cn.ly.framework.constants.RedisKeyConstants;
import cn.ly.framework.domain.entity.Article;
import cn.ly.framework.mapper.ArticleMapper;
import cn.ly.framework.utils.RedisCache;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 简要描述
 * spring boot启动后最先执行此类，将博客的浏览量存入redis，防止数据库的高并发
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/12 23:11
 */
@Component
public class ViewCountRunner implements CommandLineRunner {
    @Resource
    ArticleMapper articleMapper;

    @Resource
    RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        //查询出所有博客信息
        List<Article> articles = articleMapper.selectList(null);
        Map<String, Integer> viewCountMap = articles.stream().collect(Collectors.toMap(article -> article.getId().toString(), article -> {
            return article.getViewCount().intValue();
        }));
        redisCache.setCacheMap(RedisKeyConstants.ARTICLE_VIEW_COUNT,viewCountMap);
    }
}
