package com.ft.br.service;

import com.ft.br.model.bo.CategoryBO;

import java.util.List;

/**
 * CategoryService
 *
 * @author shichunyang
 */
public interface CategoryService {
	/**
	 * 查询所有分类信息
	 *
	 * @return 所有分类信息
	 */
	List<CategoryBO> listAllCategories();
}
