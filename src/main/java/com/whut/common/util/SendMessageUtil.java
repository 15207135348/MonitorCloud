package com.whut.common.util;

import com.whut.common.wesocket.PollWebSocketServlet;

import java.text.ParseException;

/**
 * @author yy
 * 将信息封装为Json格式传给前台
 */
public class SendMessageUtil {

	public static void sendMessage(String id, //websocket id
								   String msg, //msg
								   PollWebSocketServlet webSocketServlet)
			throws ParseException {
		webSocketServlet.sendText(id, msg);
	}
}
