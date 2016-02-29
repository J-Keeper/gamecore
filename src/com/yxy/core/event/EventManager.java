package com.yxy.core.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 事件管理对象
 * @author YongXinYu
 * @date 2015年8月5日 下午10:05:03
 */
public class EventManager {
	private Logger log = LoggerFactory.getLogger(EventManager.class);
	private Map<Integer, List<Subscriber>> subscriberMap = new HashMap<>();

	public void registerEvent(Object target) {
		Class<?> clazz = target.getClass();
		Method[] methods = clazz.getDeclaredMethods();
		for (Method m : methods) {
			m.setAccessible(true);
			if (m.isAnnotationPresent(Listener.class)) {
				Listener lser = (Listener) m.getAnnotation(Listener.class);
				List<Subscriber> subList = this.subscriberMap.get(Integer
						.valueOf(lser.value()));
				if (subList == null) {
					subList = new ArrayList<>();
					this.subscriberMap.put(Integer.valueOf(lser.value()),
							subList);
				}
				subList.add(new Subscriber(target, m));
			}
		}
	}

	public void handleEvent(Event event) {
		try {
			fireEvent(event);
		} catch (Exception e) {
			this.log.error("addLoaclEvent[" + event + "] Error", e);
		}
	}

	private void fireEvent(Event event) {
		List<Subscriber> listenerList = this.subscriberMap.get(Integer
				.valueOf(event.getType()));
		if (listenerList == null) {
			this.log.info("No Subscriber for " + event);
			return;
		}
		for (Subscriber sub : listenerList)
			try {
				sub.handle(new Object[] { event });
			} catch (Exception e) {
				this.log.error("fireEvent[" + event + "] error", e.getCause());
			}
	}

	public void fireEventLast(Event event, Object last) {
		List<Subscriber> subscriberList = this.subscriberMap.get(Integer
				.valueOf(event.getType()));
		if (subscriberList == null) {
			this.log.info("No Subscriber for " + event);
			return;
		}
		Subscriber lastsub = null;
		for (Subscriber sub : subscriberList) {
			try {
				if (sub.getTarget() == last) {
					lastsub = sub;
				} else
					sub.handle(new Object[] { event });
			} catch (Exception e) {
				this.log.error("fireEvent[" + event + "] error", e.getCause());
			}
			if (lastsub != null)
				try {
					lastsub.handle(new Object[] { event });
				} catch (Exception e) {
					this.log.error("fireEvent[" + event + "] error",
							e.getCause());
				}
		}
	}

}