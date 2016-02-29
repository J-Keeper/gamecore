package com.yxy.web;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.FilterHolder;
import org.mortbay.jetty.servlet.HashSessionManager;
import org.mortbay.jetty.servlet.SessionHandler;
import org.mortbay.jetty.webapp.WebAppContext;
import org.nutz.http.Http;
import org.nutz.http.Response;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.NutFilter;

import com.yxy.core.framework.IServer;

public class WebServer implements IServer {
	private static final Log log = Logs.get();
	protected WebConfig dc;
	protected Server server;

	public WebServer(WebConfig config) {
		this.dc = config;
	}

	public void init() throws Exception {

		this.server = new Server();
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(dc.getWebPort());
		server.setConnectors(new Connector[] { connector });

		// web运行上下文
		WebAppContext ctx = new WebAppContext();
		ctx.setContextPath(dc.getWebContext());
		ctx.setResourceBase(dc.getWebRoot());
		ctx.setSessionHandler(new SessionHandler(new HashSessionManager()));
		// 过滤器使用NutzFilter
		FilterHolder fh = new FilterHolder(NutFilter.class);
		fh.setInitParameter("modules", dc.getMainModule());
		ctx.addFilter(fh, "/*", 15);

		this.server.setHandler(ctx);
	}

	public void start() throws Exception {
		this.server.start();
		Response resp = Http.get("http://127.0.0.1:" + dc.getWebPort());
		if ((resp == null) || (resp.getStatus() >= 500)) {
			log.error("Self-Testing fail !!Server start fail?!!");
			this.server.stop();
			return;
		}
		if (log.isInfoEnabled())
			log.info("Web server is up!");
	}

	public void shutdown() {
		if (null != this.server)
			try {
				server.stop();
			} catch (Throwable e) {
				if (log.isErrorEnabled())
					log.error("Fail to stop!", e);
			}
	}

	public Server getServer() {
		return server;
	}
}