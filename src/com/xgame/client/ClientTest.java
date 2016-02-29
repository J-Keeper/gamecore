package com.xgame.client;

import java.util.Scanner;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.nutz.lang.Strings;

import com.xgame.packet.MessageType;
import com.yxy.core.net.Client;
import com.yxy.core.net.codec.DemuxCodecFactory;
import com.yxy.core.net.codec.Message;

/**
 * 测试用客户端
 * 
 */
public class ClientTest extends IoHandlerAdapter {
	private Client client = null;

	public void start() {
		DemuxCodecFactory codec = new DemuxCodecFactory();
		client = new Client("127.0.0.1", 1051, codec);
		client.startup(new ClientHandler());
	}

	public void sendMessage(Message message) {
		client.sendMessage(message);
	}

	public void shutdown() {
		client.shutdown();
	}

	
	public static void main(String[] args) throws Exception {
		ClientTest test = new ClientTest();
		test.start();
		Message login = new Message(MessageType.LOGIN_REQ);
		login.put("name", "test2");
		login.put("pass", "2.2");
		login.put("platform", "wabao");
		test.sendMessage(login);
		Thread.sleep(1000L);

		Scanner scanner = new Scanner(System.in);
		short type = 0;
		System.err
				.println("退出:exit;参数格式: 协议号?参数=值&参数=值(如:501?accperId=-1&content=你好!);");
		do {
			System.out.println("@输入请求参数:");
			String requestParams = scanner.nextLine();
			if (Strings.isBlank(requestParams)) {
				continue;
			}
			if (requestParams.equals("exit")) {
				break;
			}
			String params[] = requestParams.split("\\?");
			try {
				Message message = new Message();
				type = Short.parseShort(params[0]);
				message.setType(type);
				if (params.length > 1) {
					for (String paramPart : params[1].split("&")) {
						String[] parts = paramPart.split("=");
						message.put(parts[0], parts[1]);
					}
				}
				test.sendMessage(message);
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("请求参数错误: " + e.getMessage());
			}
		} while (true);
		scanner.close();
		test.shutdown();
	}
}