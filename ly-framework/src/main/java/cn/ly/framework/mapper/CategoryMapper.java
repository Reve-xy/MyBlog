package cn.ly.framework.mapper;

import cn.ly.framework.domain.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 分类表 Mapper 接口
 * </p>
 *
 * @author 刘易
 * @since 2023-04-05
 */
public interface CategoryMapper extends BaseMapper<Category> {
    /**
     * @param 
     * @return List<Category>
     * @date 2023/4/5 14:55
     * @description 获取文章分类列表，要求只展示有正式内容的文章，且分类状态是正常
     */
    public List<Category> getCategoryList(String status);
}
