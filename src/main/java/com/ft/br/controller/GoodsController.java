package com.ft.br.controller;

import com.ft.br.service.GoodsService;
import com.ft.db.model.PageParam;
import com.ft.util.JsonUtil;
import com.ft.util.model.RestResult;
import com.ft.web.annotation.LoginCheck;
import com.ft.web.constant.SwaggerConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商品API
 *
 * @author shichunyang
 */
@Api(tags = "商品API")
@RestController
@RequestMapping(RestResult.API + "/goods")
@CrossOrigin
@Slf4j
public class GoodsController {

	@Autowired
	private GoodsService goodsService;

	@ApiOperation("根据id查询商品信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "商品id", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
	})
	/**
	 * 根据id查询商品信息
	 *
	 * @param id 商品id
	 * @return 商品信息
	 */
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	@LoginCheck
	public String get(@RequestParam("id") long id) {
		return JsonUtil.object2Json(RestResult.success(goodsService.get(id)));
	}

	@ApiOperation("修改商品信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "商品id", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
			@ApiImplicitParam(name = "name", value = "商品名称", required = true, dataType = SwaggerConstant.DATA_TYPE_STRING, paramType = SwaggerConstant.PARAM_TYPE_QUERY),
			@ApiImplicitParam(name = "category_id", value = "分类id", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
	})
	/**
	 * 修改商品信息
	 *
	 * @param id         主键
	 * @param name       商品名称
	 * @param categoryId 分类id
	 * @return 修改结果
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@LoginCheck
	public String update(
			@RequestParam("id") long id,
			@RequestParam("name") String name,
			@RequestParam("category_id") short categoryId
	) {
		return JsonUtil.object2Json(RestResult.success(goodsService.update(id, name, categoryId)));
	}

	@ApiOperation("添加商品信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "name", value = "商品名称", required = true, dataType = SwaggerConstant.DATA_TYPE_STRING, paramType = SwaggerConstant.PARAM_TYPE_QUERY),
			@ApiImplicitParam(name = "category_id", value = "分类id", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
	})
	/**
	 * 添加商品信息
	 *
	 * @param name       商品名称
	 * @param categoryId 分类id
	 * @return 添加结果
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@LoginCheck
	public String add(
			@RequestParam("name") String name,
			@RequestParam("category_id") short categoryId
	) {
		return JsonUtil.object2Json(RestResult.success(goodsService.add(name, categoryId)));
	}

	@ApiOperation("分页查询商品")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "category_id", value = "分类id", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0", defaultValue = "0"),
			@ApiImplicitParam(name = "current_page", value = "查询页码", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
	})
	/**
	 * 分页查询商品
	 *
	 * @param categoryId  分类id
	 * @param currentPage 当前页
	 * @return 查询到的数据
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@LoginCheck
	public String list(
			@RequestParam(value = "category_id", required = false, defaultValue = "0") short categoryId,
			@RequestParam(value = "current_page") int currentPage
	) {
		return JsonUtil.object2Json(RestResult.success(goodsService.list(categoryId, new PageParam(currentPage, 10))));
	}

	@ApiOperation("根据分类查询所有商品")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "category_id", value = "分类id", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
	})
	/**
	 * 根据分类查询所有商品
	 *
	 * @param categoryId 分类id
	 * @return 该分类下所有商品
	 */
	@RequestMapping(value = "/list-by-category", method = RequestMethod.POST)
	@LoginCheck
	public String listByCategory(
			@RequestParam(value = "category_id") short categoryId
	) {
		return JsonUtil.object2Json(RestResult.success(goodsService.list(categoryId, new PageParam(1, 10_0000))));
	}
}
