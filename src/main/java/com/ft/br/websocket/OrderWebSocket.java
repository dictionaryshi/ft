package com.ft.br.websocket;

import com.ft.br.service.impl.GoodsServiceImpl;
import com.ft.util.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;

import static com.ft.web.util.WebSocketUtil.sendMessage;

/**
 * 订单业务socket
 *
 * @author shichunyang
 */
@Slf4j
@Component
@ServerEndpoint(value = "/order/socket/{oid}")
public class OrderWebSocket {
    public static final ConcurrentHashMap<Session, Integer> ORDER_WEB_SOCKET = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(@PathParam("oid") Integer oid, Session session) {
        ORDER_WEB_SOCKET.put(session, oid);

        log.info(LogUtil.build("socket add",
                "oid", oid + "",
                "total", ORDER_WEB_SOCKET.size() + ""));
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.warn(LogUtil.build("socket error", throwable,
                "oid", ORDER_WEB_SOCKET.get(session) + "",
                "total", ORDER_WEB_SOCKET.size() + ""));
    }

    @OnClose
    public void onClose(Session session) {
        Integer oid = ORDER_WEB_SOCKET.remove(session);

        log.info(LogUtil.build("socket close",
                "oid", oid + "",
                "total", ORDER_WEB_SOCKET.size() + ""));
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        MDC.put(ThreadLocalMap.REQUEST_ID, CommonUtil.get32Uuid());

        GoodsServiceImpl goodsService = SpringContextUtil.getBean(GoodsServiceImpl.class);
        sendMessage(session, JsonUtil.object2Json(goodsService.get(ORDER_WEB_SOCKET.get(session))) + "_" + message);

        log.info(LogUtil.build("socket message",
                "oid", ORDER_WEB_SOCKET.get(session) + "",
                "total", ORDER_WEB_SOCKET.size() + ""));
    }
}
