package com.yxy.core.event;

import com.yxy.core.GameContext;

/**
 * 事件的使用举例
 * 
 * @author YXY
 * @date 2016年2月18日 下午5:59:41
 */
public class EventUseExample {
	private GameContext context;

	public void createOnLineEvent() {
		context.addEvent(new Event(EventTypeTest.ONLINE, "玩家上线了"));
	}

	@Listener(EventTypeTest.ONLINE)
	public void onLine(Event event) {
		Object source = event.getSource();
		System.out.print(source);
	}
}
