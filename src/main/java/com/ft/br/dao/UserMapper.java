package com.ft.br.dao;

import com.ft.dao.stock.model.UserDO;
import com.ft.db.constant.DbConstant;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 登录用户操作类
 *
 * @author shichunyang
 */
@Mapper
@Component
public interface UserMapper {

	/**
	 * 根据用户名和密码查找用户
	 *
	 * @param username 用户名
	 * @param password 密码(md5)
	 * @return 用户
	 */
	@Select("select * from `user` where username = #{username} and password = #{password}")
	UserDO getUserByUserNameAndPassword(@Param("username") String username, @Param("password") String password);

	/**
	 * 根据主键查询用户
	 *
	 * @param id 主键
	 * @return 用户信息
	 */
	@Select("select * from `user` where id = #{id}")
	UserDO getUserById(@Param("id") long id);

	/**
	 * 根据用户名查询用户信息
	 *
	 * @param username 用户名
	 * @return 用户信息
	 */
	@Select("select * from `user` where username = #{username} limit 1")
	UserDO getUserByUserName(@Param("username") String username);

	/**
	 * 修改用户密码
	 *
	 * @param id       用户ID
	 * @param password 用户密码(md5)
	 * @return 1: 修改成功
	 */
	@Update("update `user` set password = #{password} where id = #{id}")
	int update(@Param("id") long id, @Param("password") String password);

	/**
	 * 插入用户信息
	 *
	 * @param userDO 用户对象
	 * @return 1: 插入成功
	 */
	@Insert("insert into `user` (`username`, `password`) values (#{username}, #{password})")
	@SelectKey(statement = DbConstant.SELECT_LAST_INSERT_ID, keyColumn = "id", keyProperty = "id", resultType = Long.class, before = false)
	int insert(UserDO userDO);

	/**
	 * 查询所有用户信息
	 *
	 * @return 所有用户信息
	 */
	@Select("select * from `user`")
	List<UserDO> selectAllUsers();

	@Select("select * from `user` where id = #{id} for update")
	UserDO deadLock(@Param("id") long id);
}
