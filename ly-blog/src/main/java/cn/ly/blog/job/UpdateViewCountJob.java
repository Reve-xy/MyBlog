package cn.ly.blog.job;

import cn.ly.framework.constants.RedisKeyConstants;
import cn.ly.framework.domain.entity.Article;
import cn.ly.framework.mapper.ArticleMapper;
import cn.ly.framework.service.ArticleService;
import cn.ly.framework.utils.RedisCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 简要描述
 * 定时十分钟将redis的浏览量更新到数据库
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/12 23:30
 */
@Component
public class UpdateViewCountJob {
    @Resource
    RedisCache redisCache;
    @Resource
    ArticleService articleService;

    //每2分钟同步到数据库
    @Scheduled(cron = "0 0/2 * * * ?")
    public void updateViewCount(){
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(RedisKeyConstants.ARTICLE_VIEW_COUNT);
        List<Article> articleList = viewCountMap.entrySet().stream().
                map(entry -> new Article(Long.parseLong(entry.getKey()), entry.getValue().longValue())).collect(Collectors.toList());
        //通过主键批量更新
        articleService.updateBatchById(articleList);
    }
}
