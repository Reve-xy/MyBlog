package cn.ly.framework.service;

import cn.ly.framework.domain.entity.Link;
import cn.ly.framework.domain.vo.LinkVo;
import cn.ly.framework.domain.vo.PageVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 友链 服务类
 * </p>
 *
 * @author 刘易
 * @since 2023-04-06
 */
public interface LinkService extends IService<Link> {

    /**
     * @param
     * @return List<linkVo>
     * @date 2023/4/6 13:34
     * @description 获取所有通过审核的友链
     */
    List<LinkVo> getAllLink();

    PageVo selectLinkPage(Link link, Integer pageNum, Integer pageSize);
}
