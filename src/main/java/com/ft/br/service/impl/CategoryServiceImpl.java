package com.ft.br.service.impl;

import com.ft.br.dao.CategoryMapper;
import com.ft.br.model.bo.CategoryBO;
import com.ft.br.service.CategoryService;
import com.ft.dao.stock.model.CategoryDO;
import com.ft.db.annotation.UseMaster;
import com.ft.util.exception.FtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分类业务
 *
 * @author shichunyang
 */
@Service("com.ft.br.service.impl.CategoryServiceImpl")
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryMapper categoryMapper;

	@Override
	public List<CategoryBO> listAllCategories() {
		Collection<CategoryDO> categoryDOs = categoryMapper.selectAllCategories().values();
		return categoryDOs.stream().map(categoryDO -> {
			CategoryBO categoryBO = new CategoryBO();
			categoryBO.setId(categoryDO.getId());
			categoryBO.setName(categoryDO.getName());
			return categoryBO;
		}).collect(Collectors.toList());
	}

	/**
	 * 添加分类信息
	 *
	 * @param name 分类名称
	 * @return true:添加成功
	 */
	@UseMaster
	public boolean add(String name) {
		CategoryDO categoryDO = categoryMapper.getCategoryByName(name);
		if (categoryDO != null) {
			FtException.throwException("请不要重复添加分类信息");
		}

		categoryDO = new CategoryDO();
		categoryDO.setName(name);

		return categoryMapper.insertSelective(categoryDO) == 1;
	}

	/**
	 * 修改分类信息
	 *
	 * @param id   分类id
	 * @param name 分类名称
	 * @return true:修改成功
	 */
	@UseMaster
	public boolean update(int id, String name) {
		CategoryDO categoryDO = categoryMapper.selectByPrimaryKey(id);
		if (categoryDO == null) {
			FtException.throwException("分类信息不存在");
		}
		if (categoryMapper.getCategoryByName(name) != null) {
			FtException.throwException("分类已经存在");
		}
		categoryDO = new CategoryDO();
		categoryDO.setId(id);
		categoryDO.setName(name);
		return categoryMapper.updateByPrimaryKeySelective(categoryDO) == 1;
	}

	/**
	 * 根据主键查询分类信息
	 *
	 * @param id 分类主键
	 * @return 分类信息
	 */
	public CategoryDO get(int id) {
		return categoryMapper.selectByPrimaryKey(id);
	}
}
