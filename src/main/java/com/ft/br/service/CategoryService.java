package com.ft.br.service;

import com.ft.br.model.ao.category.CategoryAddAO;
import com.ft.br.model.ao.category.CategoryUpdateAO;
import com.ft.br.model.bo.CategoryBO;

import java.util.List;
import java.util.Map;

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

    /**
     * 根据主键查询分类信息
     *
     * @param id 主键
     * @return 分类信息
     */
    CategoryBO getById(int id);

    /**
     * 添加分类信息
     *
     * @param categoryAddAO 分类信息
     * @return true 添加成功
     */
    boolean add(CategoryAddAO categoryAddAO);

    /**
     * 修改分类信息
     *
     * @param categoryUpdateAO 分类信息
     * @return true 修改成功
     */
    boolean update(CategoryUpdateAO categoryUpdateAO);

    /**
     * 根据id批量获取分类名称
     *
     * @param categoryIds 分类id集合
     * @return 分类名称集合
     */
    Map<Integer, String> listCategoryNameByIds(List<Integer> categoryIds);
}
