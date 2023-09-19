package cn.ly.framework.service.impl;

import cn.ly.framework.domain.entity.ArticleTag;
import cn.ly.framework.mapper.ArticleTagMapper;
import cn.ly.framework.service.ArticleTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章标签关联表 服务实现类
 * </p>
 *
 * @author 刘易
 * @since 2023-04-16
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
