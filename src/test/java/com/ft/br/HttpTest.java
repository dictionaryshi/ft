package com.ft.br;

import com.ft.util.thread.ThreadPoolUtil;
import com.ft.web.model.HttpParam;
import com.ft.web.model.HttpUploadBO;
import com.ft.web.util.HttpThreadUtil;
import com.ft.web.util.HttpUtil;
import com.ft.web.util.RsaCheckUtil;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

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

	@Test
	public void batchHttp() {
		String url = "http://localhost:9001/socket/push";

		String taskName = "批量push订单消息";

		List<HttpParam> httpParams = new ArrayList<>();
		for (int i = 1; i <= 10; i++) {
			Map<String, String> params = new TreeMap<>();
			params.put("oid", i + "");
			params.put("msg", "签名测试+=%" + i);
			RsaCheckUtil.initParam(params, "scy");

			HttpParam httpParam = new HttpParam();
			httpParam.setUrl(url);
			httpParam.setConnectTimeout(10_000);
			httpParam.setReadTimeout(10_000);
			httpParam.setTextMap(params);
			httpParam.setBatchKey(i + "");
			httpParams.add(httpParam);
		}

		String poolName = "http批量线程池";
		ExecutorService threadPool = ThreadPoolUtil.getThreadPool(poolName, 10, 20, 300, TimeUnit.SECONDS, 50, null);

		Map<String, String> resultMap = HttpThreadUtil.batchPostHttp(taskName, httpParams, threadPool);
		System.out.println(resultMap);
	}
}
