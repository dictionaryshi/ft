package com.ft.br.controller;

import com.ft.br.model.ao.GoodsListAO;
import com.ft.br.model.ao.goods.GoodsAddAO;
import com.ft.br.model.ao.goods.GoodsGetAO;
import com.ft.br.model.ao.goods.GoodsListByCategoryAO;
import com.ft.br.model.ao.goods.GoodsUpdateAO;
import com.ft.br.model.bo.GoodsBO;
import com.ft.br.service.GoodsService;
import com.ft.db.annotation.PageParamCheck;
import com.ft.db.model.PageResult;
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

	/**
	 * 分页查询商品
	 */
	@ApiOperation("分页查询商品")
	@LoginCheck
	@PageParamCheck
	@GetMapping("/list")
	public RestResult<PageResult<GoodsBO>> list(
			@Valid GoodsListAO goodsListAO
	) {
		PageResult<GoodsBO> pageResult = goodsService.listByPage(goodsListAO);
		return RestResult.success(pageResult);
	}
}
