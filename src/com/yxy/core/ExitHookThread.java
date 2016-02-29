package com.yxy.core;

import org.nutz.ioc.Ioc;

public final class ExitHookThread extends Thread {
	Ioc ioc = null;

	public ExitHookThread(Ioc ioc) {
		this.ioc = ioc;
	}

	public void run() {
		if (this.ioc != null) {
			GameContext context = (GameContext) ioc.get(GameContext.class);
			if (context != null)
				context.shutdown();
			this.ioc.depose();
		}
	}
}