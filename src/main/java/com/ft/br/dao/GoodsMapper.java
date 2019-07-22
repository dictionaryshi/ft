package com.ft.br.dao;

import com.ft.br.model.ao.GoodsListAO;
import com.ft.dao.stock.mapper.GoodsDOMapper;
import com.ft.dao.stock.model.GoodsDO;
import com.ft.db.util.MybatisUtil;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * GoodsMapper
 *
 * @author shichunyang
 */
@Mapper
@Component
public interface GoodsMapper extends GoodsDOMapper {

	class SqlBuilder {
		private void pageWhere(SQL sql, GoodsListAO ao) {
			if (ao.getCategoryId() != null) {
				sql.WHERE("category = #{categoryId}");
			}
		}

		public String countPaging(GoodsListAO ao) {
			SQL sql = new SQL();

			sql.SELECT("count(1)");

			sql.FROM("goods");

			this.pageWhere(sql, ao);

			return sql.toString();
		}

		public String listPaging(GoodsListAO ao) {
			SQL sql = new SQL();

			sql.SELECT("*");

			sql.FROM("goods");

			this.pageWhere(sql, ao);

			sql.ORDER_BY("id desc");

			return MybatisUtil.limit(sql, ao);
		}
	}

	/**
	 * 根据分类查询所有商品信息
	 *
	 * @param category 分类
	 * @return 商品信息
	 */
	@MapKey("id")
	@Select("select * from `goods` where category = #{category} ")
	Map<Integer, GoodsDO> selectByCategory(int category);

	/**
	 * 根据id 修改商品库存数量
	 *
	 * @param id     商品主键
	 * @param number 变化的库存数量
	 * @return 1:修改成功
	 */
	@Update("update `goods` set `number` = `number` + #{number} where `id` = #{id}")
	int updateNumber(@Param("id") long id, @Param("number") int number);

	/**
	 * 查询符合条件的条数
	 *
	 * @param goodsListAO 条件
	 * @return 数量
	 */
	@SelectProvider(type = SqlBuilder.class, method = "countPaging")
	int countPaging(GoodsListAO goodsListAO);

	/**
	 * 分页查询商品信息
	 *
	 * @param goodsListAO 条件
	 * @return 商品信息
	 */
	@SelectProvider(type = SqlBuilder.class, method = "listPaging")
	List<GoodsDO> listPaging(GoodsListAO goodsListAO);

	/**
	 * 根据名称查询商品
	 *
	 * @param category 分类
	 * @param name     商品名称
	 * @return 商品信息
	 */
	@Select("select * from `goods` where category = #{category} and `name` = #{name} limit 1")
	GoodsDO getGoodsByName(@Param("category") int category, @Param("name") String name);
}
