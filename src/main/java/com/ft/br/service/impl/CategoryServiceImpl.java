package com.ft.br.service.impl;

import com.ft.br.dao.CategoryMapper;
import com.ft.br.model.ao.category.CategoryAddAO;
import com.ft.br.model.ao.category.CategoryUpdateAO;
import com.ft.br.model.bo.CategoryBO;
import com.ft.br.service.CategoryService;
import com.ft.dao.stock.model.CategoryDO;
import com.ft.util.ObjectUtil;
import com.ft.util.StringUtil;
import com.ft.util.exception.FtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    public CategoryBO getById(int id) {
        CategoryDO categoryDO = categoryMapper.selectByPrimaryKey(id);
        if (categoryDO == null) {
            return null;
        }

        CategoryBO categoryBO = new CategoryBO();
        categoryBO.setId(categoryDO.getId());
        categoryBO.setName(categoryDO.getName());

        return categoryBO;
    }

    @Override
    public boolean add(CategoryAddAO categoryAddAO) {
        String name = categoryAddAO.getName();

        CategoryDO categoryDO = categoryMapper.getCategoryByName(name);
        if (categoryDO != null) {
            FtException.throwException("分类信息已存在");
        }

        categoryDO = new CategoryDO();
        categoryDO.setName(name);

        return categoryMapper.insertSelective(categoryDO) == 1;
    }

    @Override
    public boolean update(CategoryUpdateAO categoryUpdateAO) {
        int id = categoryUpdateAO.getId();
        String name = categoryUpdateAO.getName();

        CategoryDO categoryDO = categoryMapper.selectByPrimaryKey(id);
        if (categoryDO == null) {
            FtException.throwException("分类信息不存在");
        }

        if (categoryMapper.getCategoryByName(name) != null) {
            FtException.throwException("分类已经存在");
        }

        CategoryDO update = new CategoryDO();
        update.setId(id);
        update.setName(name);
        return categoryMapper.updateByPrimaryKeySelective(update) == 1;
    }

    @Override
    public Map<Integer, String> listCategoryNameByIds(List<Integer> categoryIds) {
        Map<Integer, String> resultMap = new HashMap<>(16);
        if (ObjectUtil.isEmpty(categoryIds)) {
            return resultMap;
        }

        String categoryIdStr = StringUtil.join(categoryIds, ",");
        Map<Integer, CategoryDO> categoryMap = categoryMapper.listByIds(categoryIdStr);
        categoryMap.forEach((id, categoryDO) -> resultMap.put(id, categoryDO.getName()));

        return resultMap;
    }
}
