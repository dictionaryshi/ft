package com.ft.br.dao;

import com.ft.dao.stock.mapper.CategoryDOMapper;
import com.ft.dao.stock.model.CategoryDO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 分类Mapper
 *
 * @author shichunyang
 */
@Mapper
@Component
public interface CategoryMapper extends CategoryDOMapper {

    /**
     * 查询所有分类数据
     *
     * @return 所有分类数据
     */
    @Select("select * from `category`")
    @MapKey("id")
    Map<Integer, CategoryDO> selectAllCategories();

    /**
     * 根据名称查询分类信息
     *
     * @param name 分类名称
     * @return 分类信息
     */
    @Select("select * from `category` where `name` = #{name} limit 1")
    CategoryDO getCategoryByName(String name);

    @MapKey("id")
    @Select({
            "select ",
            "* ",
            "from category ",
            "where id in (${ids}) "
    })
    Map<Integer, CategoryDO> listByIds(@Param("ids") String ids);
}
