package com.ft.br.service.impl;

import com.ft.br.dao.UserMapper;
import com.ft.br.service.SsoService;
import com.ft.db.constant.DbConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * SsoServiceImpl
 *
 * @author shichunyang
 */
@Slf4j
@Service
public class SsoServiceImpl implements SsoService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(value = DbConstant.DB_CONSIGN + DbConstant.TRANSACTION_MANAGER, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
    public void deadLock(int lockId1, int lockId2) {
        userMapper.deadLock(lockId1);
        userMapper.deadLock(lockId2);
    }
}
