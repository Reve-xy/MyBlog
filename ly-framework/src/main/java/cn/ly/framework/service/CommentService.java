package cn.ly.framework.service;

import cn.ly.framework.domain.entity.Comment;
import cn.ly.framework.domain.vo.PageVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author 刘易
 * @since 2023-04-09
 */
public interface CommentService extends IService<Comment> {

    /**
     * @param articleId
     * @param pageNum
     * @param pageSize
     * @return PageVo
     * @date 2023/4/10 21:50
     * @description 根据文章ID与分页情况进行显示评论列表
     */
    PageVo getCommentList(String commentType,Long articleId, Integer pageNum, Integer pageSize);

    /**
     * @param comment
     * @return void
     * @date 2023/4/10 21:51
     * @description 添加评论
     */
    void addComment(Comment comment);

}
