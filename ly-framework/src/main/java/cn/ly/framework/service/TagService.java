package cn.ly.framework.service;

import cn.ly.framework.domain.dto.TagListDto;
import cn.ly.framework.domain.entity.Tag;
import cn.ly.framework.utils.Result;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 标签 服务类
 * </p>
 *
 * @author 刘易
 * @since 2023-04-14
 */
public interface TagService extends IService<Tag> {

    Result pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    Result listAllTag();
}
