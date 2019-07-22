package com.ft.br.controller;

import com.ft.br.model.ao.CategoryGetAO;
import com.ft.br.model.bo.CategoryBO;
import com.ft.br.service.CategoryService;
import com.ft.br.service.impl.CategoryServiceImpl;
import com.ft.util.JsonUtil;
import com.ft.util.model.RestResult;
import com.ft.web.annotation.LoginCheck;
import com.ft.web.constant.SwaggerConstant;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private CategoryServiceImpl categoryServiceImpl;

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

	@ApiOperation("添加分类信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "name", value = "分类名称", required = true, dataType = SwaggerConstant.DATA_TYPE_STRING, paramType = SwaggerConstant.PARAM_TYPE_QUERY)
	})
	/**
	 * 添加分类信息
	 *
	 * @param name 分类名称
	 * @return 添加结果
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@LoginCheck
	public String add(@RequestParam(value = "name") String name) {

		name = name.trim();

		String flag = categoryServiceImpl.add(name) ? "添加成功" : "添加失败";

		Map<String, Object> result = new HashMap<>(16);
		result.put("flag", flag);
		return JsonUtil.object2Json(RestResult.success(result));
	}

	@ApiOperation("根据id修改分类信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "分类id", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
			@ApiImplicitParam(name = "name", value = "分类名称", required = true, dataType = SwaggerConstant.DATA_TYPE_STRING, defaultValue = "默认分类名称", paramType = SwaggerConstant.PARAM_TYPE_QUERY)
	})
	@ApiResponses({
			@ApiResponse(code = 0, message = SwaggerConstant.MESSAGE_SUCCESS, response = RestResult.class),
			@ApiResponse(code = 500, message = SwaggerConstant.MESSAGE_ERROR)
	})
	/**
	 * 根据id修改分类信息
	 *
	 * @param id   分类id
	 * @param name 分类新名称
	 * @return 修改结果
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@LoginCheck
	public String update(
			@RequestParam(value = "id") short id,
			@RequestParam(value = "name") String name
	) {
		name = name.trim();
		String flag = categoryServiceImpl.update(id, name) ? "修改成功" : "修改失败";
		Map<String, Object> result = new HashMap<>(16);
		result.put("flag", flag);
		return JsonUtil.object2Json(RestResult.success(result));
	}
}
