package com.ft.br.websocket;

import com.ft.br.service.impl.GoodsServiceImpl;
import com.ft.util.JsonUtil;
import com.ft.util.LogUtil;
import com.ft.util.SpringContextUtil;
import com.ft.util.model.LogAO;
import com.ft.util.model.LogBO;
import lombok.extern.slf4j.Slf4j;
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

		LogBO logBO = LogUtil.log("socket add",
				LogAO.build("oid", oid + ""),
				LogAO.build("total", ORDER_WEB_SOCKET.size() + ""));
		log.info(logBO.getLogPattern(), logBO.getParams());
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		LogBO logBO = LogUtil.log("socket error", throwable,
				LogAO.build("oid", ORDER_WEB_SOCKET.get(session) + ""),
				LogAO.build("total", ORDER_WEB_SOCKET.size() + ""));
		log.warn(logBO.getLogPattern(), logBO.getParams());
	}

	@OnClose
	public void onClose(Session session) {
		Integer oid = ORDER_WEB_SOCKET.remove(session);

		LogBO logBO = LogUtil.log("socket close",
				LogAO.build("oid", oid + ""),
				LogAO.build("total", ORDER_WEB_SOCKET.size() + ""));
		log.info(logBO.getLogPattern(), logBO.getParams());
	}

	@OnMessage
	public void onMessage(Session session, String message) {
		GoodsServiceImpl goodsService = SpringContextUtil.getBean(GoodsServiceImpl.class);
		sendMessage(session, JsonUtil.object2Json(goodsService.get(ORDER_WEB_SOCKET.get(session))) + "_" + message);

		LogBO logBO = LogUtil.log("socket message",
				LogAO.build("oid", ORDER_WEB_SOCKET.get(session) + ""),
				LogAO.build("total", ORDER_WEB_SOCKET.size() + ""));
		log.info(logBO.getLogPattern(), logBO.getParams());
	}
}
