package com.ft.br.service;

import java.util.List;
import java.util.Map;

/**
 * UserService
 *
 * @author shichunyang
 */
public interface UserService {

	/**
	 * 批量获取用户名
	 *
	 * @param ids 用户id集合
	 * @return 用户名集合
	 */
	Map<Integer, String> listUserNamesByIds(List<Integer> ids);
}
