package com.ft.br.service.impl;

import com.ft.br.service.IdService;
import com.ft.util.plugin.Snowflake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * IdServiceImpl
 *
 * @author shichunyang
 */
@Service("com.ft.br.service.impl.IdServiceImpl")
public class IdServiceImpl implements IdService {

    @Autowired
    private Snowflake snowflake;

    @Override
    public Long getId() {
        return snowflake.get();
    }
}
