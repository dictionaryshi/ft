package com.ft.br.controller;

import com.ft.br.model.ao.goods.GoodsAddAO;
import com.ft.br.model.ao.goods.GoodsGetAO;
import com.ft.br.model.ao.goods.GoodsListByCategoryAO;
import com.ft.br.model.ao.goods.GoodsUpdateAO;
import com.ft.br.model.bo.GoodsBO;
import com.ft.br.service.GoodsService;
import com.ft.br.service.impl.GoodsServiceImpl;
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

import javax.validation.Valid;
import java.util.List;

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

	@Autowired
	private GoodsServiceImpl goodsServiceImpl;

	/**
	 * 添加商品信息
	 */
	@ApiOperation("添加商品信息")
	@LoginCheck
	@PostMapping("/add")
	public RestResult<Boolean> add(
			@RequestBody @Valid GoodsAddAO goodsAddAO
	) {
		boolean result = goodsService.add(goodsAddAO);
		return RestResult.success(result);
	}

	/**
	 * 根据id查询商品信息
	 */
	@ApiOperation("根据id查询商品信息")
	@LoginCheck
	@GetMapping("/get")
	public RestResult<GoodsBO> get(
			@Valid GoodsGetAO goodsGetAO
	) {
		GoodsBO goodsBO = goodsService.get(goodsGetAO);
		return RestResult.success(goodsBO);
	}

	/**
	 * 根据分类查询所有商品
	 */
	@ApiOperation("根据分类查询所有商品")
	@LoginCheck
	@GetMapping("/list-by-category")
	public RestResult<List<GoodsBO>> listByCategory(
			@Valid GoodsListByCategoryAO goodsListByCategoryAO
	) {
		int categoryId = goodsListByCategoryAO.getCategoryId();
		List<GoodsBO> goodsBOs = goodsService.listByCategoryId(categoryId);
		return RestResult.success(goodsBOs);
	}

	/**
	 * 修改商品信息
	 */
	@ApiOperation("修改商品信息")
	@LoginCheck
	@PostMapping("/update")
	public RestResult<Boolean> update(
			@RequestBody @Valid GoodsUpdateAO goodsUpdateAO
	) {
		boolean result = goodsService.update(goodsUpdateAO);
		return RestResult.success(result);
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
		return JsonUtil.object2Json(RestResult.success(goodsServiceImpl.list(categoryId, new PageParam(currentPage, 10))));
	}
}
