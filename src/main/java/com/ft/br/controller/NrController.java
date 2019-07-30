package com.ft.br.controller;

import com.ft.br.feign.RemoteService;
import com.ft.br.model.dto.ValidParent;
import com.ft.br.service.SsoService;
import com.ft.br.websocket.OrderWebSocket;
import com.ft.dao.stock.model.UserDO;
import com.ft.rpc.api.model.RpcParam;
import com.ft.rpc.api.model.RpcResult;
import com.ft.util.DateUtil;
import com.ft.util.ExcelUtil;
import com.ft.util.JsonUtil;
import com.ft.util.LogUtil;
import com.ft.util.model.LogAO;
import com.ft.util.model.LogBO;
import com.ft.util.model.RestResult;
import com.ft.web.annotation.SignCheck;
import com.ft.web.util.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.InputStream;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * NrController
 *
 * @author shichunyang
 */
@RestController
@CrossOrigin
@Slf4j
public class NrController {

	@Autowired
	private RemoteService remoteService;

	@Autowired
	private SsoService ssoService;

	@Autowired
	private ExecutorService executorService;

	@GetMapping("/feign")
	public String feign() {
		RpcParam rpcParam = new RpcParam();
		rpcParam.setUsername("春阳put");
		rpcParam.setAge(29);
		rpcParam.setBirth(DateUtil.getCurrentDateStr());
		RestResult<RpcResult> putResult = remoteService.put(rpcParam);

		rpcParam = new RpcParam();
		rpcParam.setUsername("春阳get");
		rpcParam.setAge(30);
		rpcParam.setBirth("2019-11-11 12:21:20");
		RestResult<RpcResult> getResult = remoteService.get(rpcParam);

		LogBO logBO = LogUtil.log("feign test",
				LogAO.build("put result", JsonUtil.object2Json(putResult)),
				LogAO.build("get result", JsonUtil.object2Json(getResult)));
		log.info(logBO.getLogPattern(), logBO.getParams());

		return JsonUtil.object2Json(putResult) + "_" + JsonUtil.object2Json(getResult);
	}

	@PutMapping("/valid-model")
	public String valid(@RequestBody @Valid ValidParent validParent) {
		return JsonUtil.object2Json(validParent);
	}

	@SignCheck
	@PostMapping("/socket/push")
	public RestResult<UserDO> push(
			@RequestParam Integer oid,
			@RequestParam String msg
	) {
		OrderWebSocket.ORDER_WEB_SOCKET.entrySet().stream().filter(entry -> entry.getValue().equals(oid)).forEach(entry -> WebSocketUtil.sendMessage(entry.getKey(), msg));

		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		UserDO userDO = new UserDO();
		userDO.setId(oid);
		userDO.setUsername(msg);
		userDO.setCreatedAt(new Date());
		return RestResult.success(userDO);
	}

	@PostMapping("/upload")
	public String upload(
			@RequestParam String username,
			@RequestParam Integer age,
			@RequestParam MultipartFile excel
	) throws Throwable {
		try (InputStream in = excel.getInputStream()) {
			return JsonUtil.object2Json(ExcelUtil.readExcel(in, false));
		}
	}

	/**
	 * mysql死锁:使用in (1, 2)解决。
	 */
	@GetMapping("/deadLock")
	public RestResult<Boolean> deadLock() {
		executorService.submit(() -> ssoService.deadLock(1, 2));
		executorService.submit(() -> ssoService.deadLock(2, 1));
		return RestResult.success(Boolean.TRUE);
	}
}
