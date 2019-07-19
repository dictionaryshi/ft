package com.ft.br.dao;

import com.ft.dao.stock.mapper.CategoryDOMapper;
import com.ft.dao.stock.model.CategoryDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

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
	List<CategoryDO> selectAllCategories();

	/**
	 * 根据名称查询分类信息
	 *
	 * @param name 分类名称
	 * @return 分类信息
	 */
	@Select("select * from `category` where `name` = #{name} limit 1")
	CategoryDO getCategoryByName(@Param("name") String name);
}
