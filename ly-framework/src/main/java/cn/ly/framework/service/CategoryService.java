package cn.ly.framework.service;

import cn.ly.framework.domain.entity.Category;
import cn.ly.framework.domain.vo.CategoryVo;
import cn.ly.framework.enums.HttpCodeEnum;
import cn.ly.framework.utils.Result;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 分类表 服务类
 * </p>
 *
 * @author 刘易
 * @since 2023-04-05
 */
public interface CategoryService extends IService<Category> {

    List<CategoryVo> getCategoryList();

    Result selectCategoryPage(String name, String status, Integer pageNum, Integer pageSize);
}
