package com.xgame.bootstrap;

import org.apache.log4j.PropertyConfigurator;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.IocException;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.combo.ComboIocLoader;
import org.nutz.log.Logs;

import com.yxy.core.GameContext;

/**
 * 游戏服务器本地启动
 * 
 * @author YXY
 * @date 2016年2月19日 下午2:27:04
 */
public final class GameMain {

	public static void main(String[] args) {
		try {
			PropertyConfigurator.configure(GameMain.class.getClassLoader()
					.getResource("log4j.properties"));
			Ioc ioc = new NutIoc(new ComboIocLoader(
					"*org.nutz.ioc.loader.json.JsonLoader", "server.js",
					"*org.nutz.ioc.loader.annotation.AnnotationIocLoader",
					"com.xgame.module"));
			// 启动游戏服务器
			GameContext context = ioc.get(GameContext.class);
			context.startWithWeb();
		} catch (Exception e) {
			Throwable t = e.getCause();
			while (t instanceof IocException) {
				t = t.getCause();
			}
			Logs.get().error("Game Server start fail!", e);
			System.exit(1);
		}
	}
}
