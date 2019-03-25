package com.ft.br.controller;

import com.ft.br.service.CategoryService;
import com.ft.util.JsonUtil;
import com.ft.util.model.RestResult;
import com.ft.web.annotation.LoginCheck;
import com.ft.web.constant.SwaggerConstant;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
	private CategoryService categoryService;

	@ApiOperation("查询所有分类信息")
	/**
	 * 查询所有分类信息
	 *
	 * @return 所有分类信息
	 */
	@RequestMapping(value = "/all", method = RequestMethod.POST)
	@LoginCheck
	public String all() {
		return JsonUtil.object2Json(RestResult.getSuccessRestResult(categoryService.listAll()));
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

		String flag = categoryService.add(name) ? "添加成功" : "添加失败";

		Map<String, Object> result = new HashMap<>(16);
		result.put("flag", flag);
		return JsonUtil.object2Json(RestResult.getSuccessRestResult(result));
	}

	@ApiOperation("根据id修改分类信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "分类id", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
			@ApiImplicitParam(name = "name", value = "分类名称", required = true, dataType = SwaggerConstant.DATA_TYPE_STRING, defaultValue = "默认分类名称", paramType = SwaggerConstant.PARAM_TYPE_QUERY)
	})
	@ApiResponses({
			@ApiResponse(code = RestResult.SUCCESS_CODE, message = SwaggerConstant.MESSAGE_SUCCESS, response = RestResult.class),
			@ApiResponse(code = RestResult.ERROR_CODE, message = SwaggerConstant.MESSAGE_ERROR)
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
		String flag = categoryService.update(id, name) ? "修改成功" : "修改失败";
		Map<String, Object> result = new HashMap<>(16);
		result.put("flag", flag);
		return JsonUtil.object2Json(RestResult.getSuccessRestResult(result));
	}

	@ApiOperation("根据id查询分类信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "分类id", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
	})
	/**
	 * 根据主键查询分类信息
	 *
	 * @param id 分类主键
	 * @return 分类信息
	 */
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	@LoginCheck
	public String get(@RequestParam(value = "id") short id) {
		return JsonUtil.object2Json(RestResult.getSuccessRestResult(categoryService.get(id)));
	}
}
