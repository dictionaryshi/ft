package com.ft.br.service;

/**
 * SsoService
 *
 * @author shichunyang
 */
public interface SsoService {
    /**
     * 死锁测试
     *
     * @param lockId1 锁定id
     * @param lockId2 锁定id
     */
    void deadLock(int lockId1, int lockId2);
}
