package com.ft.br.controller;

import com.ft.br.feign.RemoteService;
import com.ft.br.model.dto.ValidParent;
import com.ft.br.websocket.OrderWebSocket;
import com.ft.rpc.api.model.RpcParam;
import com.ft.rpc.api.model.RpcResult;
import com.ft.util.ExcelUtil;
import com.ft.util.JsonUtil;
import com.ft.util.exception.FtException;
import com.ft.util.model.RestResult;
import com.ft.web.annotation.SignCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.InputStream;
import java.util.Date;

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

	@GetMapping("/feign")
	public String feign() {
		RpcParam rpcParam = new RpcParam();
		rpcParam.setUsername("春阳");
		rpcParam.setAge(29);
		rpcParam.setBirth(new Date());
		RestResult<RpcResult> putResult = remoteService.put(rpcParam);
		RestResult<RpcResult> getResult = remoteService.get("xt", 25);
		log.info("putResult==>{}, getResult==>{}", JsonUtil.object2Json(putResult), JsonUtil.object2Json(getResult));
		return JsonUtil.object2Json(putResult) + "_" + JsonUtil.object2Json(getResult);
	}

	@PutMapping("/valid-model")
	public String valid(@RequestBody @Valid ValidParent validParent) {
		return JsonUtil.object2Json(validParent);
	}

	@SignCheck
	@PostMapping("/socket/push")
	public String push(
			@RequestParam Integer oid,
			@RequestParam String msg
	) {
		OrderWebSocket.ORDER_WEB_SOCKET.entrySet().stream().filter(entry -> entry.getValue().equals(oid)).forEach(entry -> OrderWebSocket.sendMessage(entry.getKey(), msg));
		return "success";
	}

	@PostMapping("/upload")
	public String upload(
			@RequestParam String username,
			@RequestParam MultipartFile excel
	) {
		try (InputStream in = excel.getInputStream()) {
			return JsonUtil.object2Json(ExcelUtil.readExcel(in, false));
		} catch (Exception e) {
			log.error("upload exception==>{}", FtException.getExceptionStack(e));
			return "error";
		}
	}
}
