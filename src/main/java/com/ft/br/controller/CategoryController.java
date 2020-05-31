package com.ft.br.controller;

import com.ft.br.model.ao.category.CategoryAddAO;
import com.ft.br.model.ao.category.CategoryGetAO;
import com.ft.br.model.ao.category.CategoryUpdateAO;
import com.ft.br.model.bo.CategoryBO;
import com.ft.br.service.CategoryService;
import com.ft.util.model.RestResult;
import com.ft.web.annotation.LoginCheck;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 分类api接口
 *
 * @author shichunyang
 */
@RestController
@RequestMapping(RestResult.API + "/category")
@CrossOrigin
@Slf4j
@Api(tags = "分类API")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查询所有分类信息
     */
    @ApiOperation("查询所有分类信息")
    @LoginCheck
    @GetMapping("/all")
    public RestResult<List<CategoryBO>> all() {
        return RestResult.success(categoryService.listAllCategories());
    }

    /**
     * 根据主键查询分类信息
     */
    @ApiOperation("根据id查询分类信息")
    @LoginCheck
    @GetMapping("/get")
    public RestResult<CategoryBO> get(
            @Valid CategoryGetAO categoryGetAO
    ) {
        CategoryBO categoryBO = categoryService.getById(categoryGetAO.getId());
        return RestResult.success(categoryBO);
    }

    /**
     * 添加分类信息
     */
    @ApiOperation("添加分类信息")
    @LoginCheck
    @PostMapping("/add")
    public RestResult<Boolean> add(
            @RequestBody @Valid CategoryAddAO categoryAddAO
    ) {
        boolean result = categoryService.add(categoryAddAO);
        return RestResult.success(result);
    }

    /**
     * 根据id修改分类信息
     */
    @ApiOperation("根据id修改分类信息")
    @LoginCheck
    @PostMapping("/update")
    public RestResult<Boolean> update(
            @RequestBody @Valid CategoryUpdateAO categoryUpdateAO
    ) {
        boolean result = categoryService.update(categoryUpdateAO);
        return RestResult.success(result);
    }
}
