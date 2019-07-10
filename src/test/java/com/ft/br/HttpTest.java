package com.ft.br;

import com.ft.web.model.HttpParam;
import com.ft.web.model.HttpUploadBO;
import com.ft.web.util.HttpUtil;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import static com.ft.web.util.HttpUtil.*;

public class HttpTest {
	@Test
	public void upload() throws Exception {
		String url = "http://localhost:9001/upload";
		HttpParam httpParam = new HttpParam();
		httpParam.setUrl(url);
		httpParam.setResponseCharset(DEFAULT_RESPONSE_CHARSET);
		httpParam.setConnectTimeout(CONNECT_TIMEOUT);
		httpParam.setReadTimeout(READ_TIMEOUT);

		Map<String, String> textMap = new HashMap<String, String>(16) {
			{
				put("username", "史春阳");
				put("age", "30");
			}
		};
		httpParam.setTextMap(textMap);

		Map<String, HttpUploadBO> fileMap = new HashMap<String, HttpUploadBO>(16) {
			{
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				IOUtils.copy(new FileInputStream("/Users/shichunyang/person.xls"), byteArrayOutputStream);
				HttpUploadBO httpUploadBO = new HttpUploadBO();
				httpUploadBO.setFileName("史春阳.xls");
				httpUploadBO.setContentType("");
				httpUploadBO.setFileInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
				put("excel", httpUploadBO);
			}
		};
		httpParam.setFileMap(fileMap);

		System.out.println(HttpUtil.upload(httpParam));
	}
}
