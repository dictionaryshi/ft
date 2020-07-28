package com.ft.br.controller;

import com.ft.br.model.dto.ValidParent;
import com.ft.br.service.SsoService;
import com.ft.br.websocket.OrderWebSocket;
import com.ft.dao.stock.model.UserDO;
import com.ft.util.ExcelUtil;
import com.ft.util.JsonUtil;
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
    private SsoService ssoService;

    @Autowired
    private ExecutorService executorService;

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
            return JsonUtil.object2Json(ExcelUtil.readExcel(in, false, "#"));
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
