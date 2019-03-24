package com.ft.br.dao;

import com.ft.br.model.mdo.CategoryDO;
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
public interface CategoryMapper {

	/**
	 * 根据主键查询分类信息
	 *
	 * @param id 主键
	 * @return 分类信息
	 */
	@Select("select * from `category` where id = #{id}")
	CategoryDO getCategoryById(@Param("id") short id);

	/**
	 * 根据主键修改分类信息
	 *
	 * @param categoryDO 修改分类
	 * @return 1:修改成功
	 */
	@Update("update `category` set `name` = #{name} where id = #{id}")
	int update(CategoryDO categoryDO);

	/**
	 * 添加分类
	 *
	 * @param categoryDO 分类DO
	 * @return 1:添加成功
	 */
	@Insert("insert into `category` (`name`) values (#{name})")
	int insert(CategoryDO categoryDO);

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
