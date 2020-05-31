package com.ft.br.service.impl;

import com.ft.br.dao.UserMapper;
import com.ft.br.service.UserService;
import com.ft.dao.stock.model.UserDO;
import com.ft.util.ObjectUtil;
import com.ft.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UserServiceImpl
 *
 * @author shichunyang
 */
@Service("com.ft.br.service.impl.UserServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<Integer, String> listUserNamesByIds(List<Integer> ids) {
        Map<Integer, String> resultMap = new HashMap<>(16);

        if (ObjectUtil.isEmpty(ids)) {
            return resultMap;
        }

        String idStr = StringUtil.join(ids, ",");
        Map<Integer, UserDO> userDOMap = userMapper.selectByIds(idStr);
        userDOMap.forEach((id, userDO) -> resultMap.put(id, userDO.getUsername()));

        return resultMap;
    }
}
