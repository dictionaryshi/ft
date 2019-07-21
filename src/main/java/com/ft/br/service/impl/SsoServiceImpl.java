package com.ft.br.service.impl;

import com.ft.br.model.bo.CodeBO;
import com.ft.br.service.SsoService;
import com.ft.redis.base.ValueOperationsCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * SsoServiceImpl
 *
 * @author shichunyang
 */
@Service(value = "com.ft.br.service.impl.SsoServiceImpl")
public class SsoServiceImpl implements SsoService {

	@Autowired
	private ValueOperationsCache<String, String> valueOperationsCache;

	@Value("${cookieDomain}")
	private String cookieDomain;

	@Override
	public CodeBO getCode() {
		return null;
	}
}
