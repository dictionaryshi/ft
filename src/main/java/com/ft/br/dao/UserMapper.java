package com.ft.br.dao;

import com.ft.dao.stock.mapper.UserDOMapper;
import com.ft.dao.stock.model.UserDO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 登录用户操作类
 *
 * @author shichunyang
 */
@Mapper
@Component
public interface UserMapper extends UserDOMapper {

	/**
	 * 根据用户名查询用户信息
	 *
	 * @param username 用户名
	 * @return 用户信息
	 */
	@Select("select * from `user` where username = #{username} limit 1")
	UserDO getUserByUserName(String username);

	/**
	 * 死锁测试
	 *
	 * @param id 主键
	 * @return 用户信息
	 */
	@Select("select * from `user` where id = #{id} for update")
	UserDO deadLock(int id);

	@MapKey("id")
	@Select({
			"select ",
			"* ",
			"from user ",
			"where id in (${idStr}) "
	})
	Map<Integer, UserDO> selectByIds(@Param("idStr") String idStr);
}
