package com.yxy.core;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.mvc.Mvcs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yxy.core.event.Event;
import com.yxy.core.event.EventManager;
import com.yxy.core.framework.IServer;
import com.yxy.core.framework.ISystem;
import com.yxy.core.msg.MessageDispatcher;
import com.yxy.core.msg.MessageHandler;
import com.yxy.core.net.NetServer;
import com.yxy.core.net.codec.Constants;
import com.yxy.core.net.codec.Message;
import com.yxy.core.sch.Job;
import com.yxy.core.sch.ScheduledManager;
import com.yxy.core.sch.Trigger;
import com.yxy.core.sch.impl.SimpleTrigger;
import com.yxy.core.util.ThreadPoolUtil;
import com.yxy.web.WebConfig;
import com.yxy.web.WebServer;

public final class GameContext implements IServer {
	private Logger log = LoggerFactory.getLogger(getClass());
	private final int FIVE_MIN = 300000;

	private ExecutorService exec = Executors.newCachedThreadPool();
	private NetServer netServer;
	private MessageDispatcher dispatcher;
	private MessageHandler msgHandler;
	private SystemHolder systemHolder;
	private PropertiesProxy config;
	private EventManager eventManager;
	private ScheduledManager schManager;
	private NutIoc ioc;

	public void init() throws Exception {
		this.config = ioc.get(PropertiesProxy.class, "config");
		Constants.IS_SHOW_LOG = Boolean.parseBoolean(config.get(
				"game.log.show", "false"));
		Constants.IS_HEART_BEAT = Boolean.parseBoolean(config.get(
				"game.start.heartbeat", "false"));
		this.schManager = ScheduledManager.getInstance();
		AutoAssemble.checkIocBean(this,
				config.get("game.sys.pkg", "com.xgame.module"));
		this.systemHolder.initAllSystem();
	}

	public void start() throws Exception {
		this.dispatcher.start();
		this.netServer.startup(msgHandler);
		boolean time2save = Boolean.parseBoolean(config.get("game.time.save",
				"true"));
		if (time2save) {
			schedule(new SimpleTrigger("time2saveJob", new Date(curr()
					+ FIVE_MIN), FIVE_MIN, new Job() {
				public void execute(Trigger trigger) {
					systemHolder.time2save();
				}
			}));
		}
		this.log.info("Game Server is up!");
		Runtime.getRuntime().addShutdownHook(new ExitHookThread(ioc));
	}

	public void startWithWeb() throws Exception {
		WebServer server = new WebServer(new WebConfig(getConfig()));
		server.init();
		server.start();
		Mvcs.setIoc(this.ioc);
		start();
	}

	public void shutdown() {
		this.log.info("NetServer shutdown ......");
		this.netServer.shutdown();
		this.log.info("Dispatcher shutdown ......");
		this.dispatcher.stop();
		this.log.info("SystemHolder shutdown ......");
		this.systemHolder.destroy();
		this.log.info("SchManager shutdown ......");
		this.schManager.shutDown();
		this.log.info("Executor shutdown ......");
		ThreadPoolUtil.safeShutdownPool(this.exec);
		this.log.info("GameContext shutdown success!");
	}

	public MessageDispatcher getDispatcher() {
		return this.dispatcher;
	}

	public SystemHolder getSystemContext() {
		return this.systemHolder;
	}

	public NetServer getNetServer() {
		return this.netServer;
	}

	public NutIoc getIoc() {
		return this.ioc;
	}

	public PropertiesProxy getConfig() {
		return this.config;
	}

	public String getConfig(String key) {
		return this.config.get(key);
	}

	protected void addSystem(ISystem sys) {
		this.systemHolder.add(sys);
	}

	public <T> T getSystem(Class<T> clazz) {
		return this.systemHolder.getSystem(clazz);
	}

	public ISystem getSystem(String className) {
		return this.systemHolder.getSystem(className);
	}

	public Future<?> submit(Runnable task) {
		return this.exec.submit(task);
	}

	public <R> Future<R> submit(Callable<R> task) {
		return this.exec.submit(task);
	}

	public <T> T getIoc(Class<T> type) {
		return this.ioc.get(type);
	}

	public <T> T getIoc(Class<T> type, String name) {
		return this.ioc.get(type, name);
	}

	public EventManager getEventManager() {
		return this.eventManager;
	}

	public boolean addMessage(Message msg) {
		msg.setInner(true);
		return dispatcher.addInner(msg);
	}

	public void addEvent(Event event) {
		this.eventManager.handleEvent(event);
	}

	/***
	 * 按顺序添加某个事件
	 * 
	 * @param event
	 * @param last
	 *            void
	 * @throws
	 * @author YXY
	 */
	public void addEventLast(Event event, Object last) {
		this.eventManager.fireEventLast(event, last);
	}

	public void schedule(Trigger trigger) {
		this.schManager.schedule(trigger);
	}

	public boolean schCancel(String name) {
		return this.schManager.cancel(name);
	}

	public Trigger getTrigger(String name) {
		return this.schManager.getTrigger(name);
	}

	public long curr() {
		return System.currentTimeMillis();
	}
}