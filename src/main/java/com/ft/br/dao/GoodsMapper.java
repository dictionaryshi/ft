package com.ft.br.dao;

import com.ft.br.model.dto.GoodsDTO;
import com.ft.br.model.mdo.GoodsDO;
import com.ft.br.model.vo.GoodsVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * GoodsMapper
 *
 * @author shichunyang
 */
@Mapper
@Component
public interface GoodsMapper {

	class SqlBuilder {

		private String query(GoodsDTO goodsDTO) {
			StringBuilder sb = new StringBuilder();
			sb.append("where 1 = 1 ");
			if (goodsDTO.getCategory() != null) {
				sb.append("and `category` = ").append(goodsDTO.getCategory()).append(" ");
			}
			return sb.toString();
		}

		public String countPaging(GoodsDTO goodsDTO) {
			String sql;
			sql = "select count(1) from `goods` " + query(goodsDTO);
			return sql;
		}

		public String listPaging(GoodsDTO goodsDTO) {
			String sql;
			sql = "select * from `goods` " + query(goodsDTO)
					+ "order by id asc "
					+ "limit " + goodsDTO.getStartRow() + ", " + goodsDTO.getPageSize();
			return sql;
		}
	}

	/**
	 * 插入商品
	 *
	 * @param goodsDO 商品
	 * @return 1:插入成功
	 */
	@Insert("insert into `goods` (`name`, `category`) values (#{name}, #{category})")
	int insert(GoodsDO goodsDO);

	/**
	 * 修改商品
	 *
	 * @param goodsDO 商品信息
	 * @return 1:修改成功
	 */
	@Update("update `goods` set `name` = #{name}, `category` = #{category} where id = #{id}")
	int update(GoodsDO goodsDO);

	/**
	 * 根据主键查找商品信息
	 *
	 * @param id 主键
	 * @return 商品信息
	 */
	@Select("select * from `goods` where id = #{id}")
	GoodsDO getGoodsById(@Param("id") Long id);

	/**
	 * 根据分类查询所有商品信息
	 *
	 * @param category 分类
	 * @return 商品信息
	 */
	@Select("select * from `goods` where category = #{category} ")
	List<GoodsDO> selectByCategory(@Param("category") short category);

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
	 * @param goodsDTO 条件
	 * @return 数量
	 */
	@SelectProvider(type = SqlBuilder.class, method = "countPaging")
	int countPaging(GoodsDTO goodsDTO);

	/**
	 * 分页查询商品信息
	 *
	 * @param goodsDTO 条件
	 * @return 商品信息
	 */
	@SelectProvider(type = SqlBuilder.class, method = "listPaging")
	List<GoodsVO> listPaging(GoodsDTO goodsDTO);

	/**
	 * 根据名称查询商品
	 *
	 * @param name 商品名称
	 * @return 商品信息
	 */
	@Select("select * from `goods` where `name` = #{name} limit 1")
	GoodsDO getGoodsByName(@Param("name") String name);
}
