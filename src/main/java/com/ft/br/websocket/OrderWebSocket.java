package com.ft.br.websocket;

import com.ft.br.service.GoodsService;
import com.ft.br.util.JsonUtil;
import com.ft.br.util.SpringContextUtil;
import com.ft.util.JsonUtil;
import com.ft.util.SpringContextUtil;
import com.ft.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;

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

		log.info("ORDER_WEB_SOCKET add, session==>{}, oid==>{}, count==>{}", session, oid, ORDER_WEB_SOCKET.size());
		// 发送消息socket连接成功
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		log.error("session==>{}, oid==>{}, exception==>{}", session, ORDER_WEB_SOCKET.get(session), JsonUtil.object2Json(throwable), throwable);
	}

	@OnClose
	public void onClose(Session session) {
		Integer removeOid = ORDER_WEB_SOCKET.remove(session);
		log.info("ORDER_WEB_SOCKET close, session==>{}, oid==>{}, count==>{}", session, removeOid, ORDER_WEB_SOCKET.size());
	}

	@OnMessage
	public void onMessage(Session session, String message) {
		log.info("client==>{}, oid==>{}, msg==>{}", session, ORDER_WEB_SOCKET.get(session), message);
		GoodsService goodsService = SpringContextUtil.getBean(GoodsService.class);
		sendMessage(session, JsonUtil.object2Json(goodsService.get(ORDER_WEB_SOCKET.get(session))));
	}

	public static void sendMessage(Session session, String message) {
		try {
			session.getBasicRemote().sendText(message);
		} catch (Exception e) {
			log.error("session==>{}, oid==>{}, message==>{}, exception==>{}",
					session, ORDER_WEB_SOCKET.get(session), message, JsonUtil.object2Json(e), e);
		}
	}
}
