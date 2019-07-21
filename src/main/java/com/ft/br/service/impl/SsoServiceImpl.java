package com.ft.br.service.impl;

import com.ft.br.constant.LoginConstant;
import com.ft.br.model.bo.CodeBO;
import com.ft.br.service.SsoService;
import com.ft.redis.base.ValueOperationsCache;
import com.ft.util.CommonUtil;
import com.ft.util.ImageUtil;
import com.ft.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

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
		CodeBO codeBO = new CodeBO();

		String codeId = CommonUtil.get32UUID();
		codeBO.setCodeId(codeId);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		String code = ImageUtil.getImage(byteArrayOutputStream);
		String img = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
		codeBO.setImg(img);

		// code codeId做关系绑定
		String codeIdKey = StringUtil.append(StringUtil.REDIS_SPLIT, LoginConstant.REDIS_VERIFICATION_CODE, codeId);
		valueOperationsCache.setIfAbsent(codeIdKey, code, 300_000L, TimeUnit.MILLISECONDS);

		return codeBO;
	}
}
