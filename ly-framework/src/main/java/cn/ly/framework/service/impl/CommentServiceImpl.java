package cn.ly.framework.service.impl;

import cn.ly.framework.constants.SystemConstants;
import cn.ly.framework.domain.entity.Article;
import cn.ly.framework.domain.entity.Comment;
import cn.ly.framework.domain.entity.User;
import cn.ly.framework.domain.vo.CommentVo;
import cn.ly.framework.domain.vo.PageVo;
import cn.ly.framework.enums.HttpCodeEnum;
import cn.ly.framework.exception.SystemException;
import cn.ly.framework.mapper.ArticleMapper;
import cn.ly.framework.mapper.CommentMapper;
import cn.ly.framework.service.CommentService;
import cn.ly.framework.service.UserService;
import cn.ly.framework.utils.BeanCopyUtils;
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

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author 刘易
 * @since 2023-04-09
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    UserService userService;

    @Override
    public PageVo getCommentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> lambdaQueryWrapper=new LambdaQueryWrapper();
        //对articleId进行判断,友联评论不需要查文章ID
        lambdaQueryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId,articleId);
        lambdaQueryWrapper.eq(Comment::getRootId, SystemConstants.COMMENT_ROOT_ID);
        lambdaQueryWrapper.eq(Comment::getType,commentType);

        Page<Comment> page = PageHelper.startPage(pageNum, pageSize);
        List<Comment> list = this.list(lambdaQueryWrapper);
        //封装分页查询结果
        PageInfo<Comment> commentPageInfo=new PageInfo<>(list);
        //comment转vo
        List<CommentVo> commentVos = toCommentList(commentPageInfo.getList());

        //查询所有根评论的子评论集合
        for (CommentVo commentVo :commentVos) {
            List<CommentVo> children = getChildren(commentVo.getId());
            commentVo.setChildren(children);
        }
        //返回vo与所有评论的条数
        return new PageVo(commentVos,commentPageInfo.getTotal());
    }


    @Resource
    ArticleMapper articleMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addComment(Comment comment) {
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(HttpCodeEnum.CONTENT_NOT_NULL);
        }
        Article article = articleMapper.selectById(comment.getArticleId());
        if(article.getIsComment().equals(SystemConstants.EXIT_COMMENT)){
            throw new SystemException(HttpCodeEnum.EXIT_COMMENT);
        }
        //保存
        this.save(comment);
    }

    private List<CommentVo> getChildren(Long id){
        List<Comment> list = list(new LambdaQueryWrapper<Comment>().eq(Comment::getRootId, id).orderByAsc(Comment::getCreateTime));
        List<CommentVo> commentVos = toCommentList(list);
        return commentVos;
    }

    /**
     * @param list
     * @return List<CommentVo>
     * @date 2023/4/9 22:37
     * @description 将comment转换为commentvo
     */
    private List<CommentVo> toCommentList(List<Comment> list){
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        for (CommentVo commentVo : commentVos) {
            User byId = userService.getById(commentVo.getCreateBy());
            //设置评论人的昵称
            commentVo.setUsername(byId.getNickName());
            //头像
            commentVo.setAvatar(byId.getAvatar());
            if(commentVo.getToCommentUserId()!=SystemConstants.COMMENT_ROOT_ID){
                String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toCommentUserName);
            }
        }
        return commentVos;
    }
}
