package com.xgame.module.blog;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xgame.module.account.AccountSystem;
import com.xgame.module.avatar.AvatarSystem;
import com.xgame.module.blog.dao.ActivityLog;
import com.xgame.module.blog.dao.Blog;
import com.xgame.module.blog.dao.LogDao;
import com.xgame.module.blog.dao.StartStop;
import com.xgame.module.blog.dao.StatOnline;
import com.xgame.module.blog.dao.TableMark;
import com.yxy.core.GameContext;
import com.yxy.core.framework.SystemAdapter;
import com.yxy.core.sch.Job;
import com.yxy.core.sch.Trigger;
import com.yxy.core.sch.impl.SimpleTrigger;
import com.yxy.core.util.ThreadPoolUtil;

/**
 * 玩家行为日志系统
 * 
 * @author YXY
 * @date 2014年10月9日 下午1:54:47
 */
@IocBean(create = "load")
public class LogSystem extends SystemAdapter {
	private Logger log = LoggerFactory.getLogger(getClass());
	private final int STAT_MIN = 2 * 60 * 1000;
	@Inject
	private LogDao logDao;
	@Inject("refer:gameContext")
	private GameContext context;
	@Inject
	private AccountSystem accountSystem;
	@Inject
	private AvatarSystem avatarSystem;

	/** 服务器启动时间 */
	private StartStop startTime;
	/** 服务器第一次启动的时间 */
	private StartStop firstStartTime;
	/** 日志队列 **/
	private LinkedBlockingQueue<Blog> blogQueue = new LinkedBlockingQueue<>();
	ExecutorService exec = Executors.newSingleThreadExecutor();
	/** 是否关闭 **/
	private boolean isShutDown = false;
	/** 循环表标志 **/
	private TableMark tableMark;

	@Override
	public void load() throws Exception {
		this.firstStartTime = logDao.getFirstStartTime();
		this.startTime = logDao.onStart(new StartStop(new Date(), "running"));
		blogQueuSave();// 启动保存日志
	}

	@Override
	public void init() {
		context.schedule(new SimpleTrigger("onlineCount", new Date(context
				.curr() + STAT_MIN), STAT_MIN, new Job() {
			@Override
			public void execute(Trigger trigger) {
				StatOnline sol = accountSystem.countAll();
				logDao.addOnlineStat(sol);
				log.info("当前在线人数:" + sol.getOnlineNum() + "|"
						+ sol.getSessionNum());
			}
		}));
	}

	@Override
	public void destroy() {
		this.startTime.setStopTime(new Date());
		this.startTime.setNote("shutdown [normal]");
		/** 保证关服也保存 **/
		while (!blogQueue.isEmpty()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.isShutDown = true;
		// 结束日志线程
		ThreadPoolUtil.safeShutdownPool(exec);
		// 保存当前切换的表标识
		saveTableCheck();
		logDao.onShutdown(this.startTime);
	}

	/**
	 * 生产日志方法(这里是放入队列)
	 * 
	 * @author YXY
	 * @data 2014年10月21日 上午10:02:03
	 * @param blog
	 */
	public void save(final Blog blog) {
		if (blog == null)
			return;
		try {
			blogQueue.put(blog);
		} catch (Exception e) {
			log.error("save error[" + blog + "]", e);
		}
	}

	/**
	 * 消费队列日志
	 * 
	 * @author YXY
	 * @data 2014年10月21日 上午10:13:03
	 */
	private void blogQueuSave() {
		// 读取数据库保存的切换表标识
		tableMark = logDao.getTableMark();
		if (tableMark == null) {
			tableMark = new TableMark(0);
		}

		exec.submit(new Runnable() {
			@Override
			public void run() {
				// 循环保存
				while (!isShutDown) {
					Blog blog = null;
					try {
						blog = blogQueue.take();
						int tName = blog.getTableCid();

						boolean isClear = isClear(tName);
						if (isClear) {
							// 切换表标识
							tableMark.setTableCheke(tName);
							// 保存当前切换的表标识
							saveTableCheck();
						}
						logDao.addLog(blog, isClear);
					} catch (Exception e) {
						if (!isShutDown)
							log.error("save error", e);
					}
				}

			}
		});
	}

	public StartStop getStartTime() {
		return startTime;
	}

	public StartStop getFirstStartTime() {
		return firstStartTime;
	}

	/**
	 * 是否清空日志表
	 * 
	 * @author YXY
	 * @data 2014年10月21日 下午8:32:29
	 * @param tName
	 * @return
	 */
	private boolean isClear(int tName) {
		if (tableMark.getTableCheke() != tName) {
			return true;
		}
		return false;
	}

	/**
	 * 保存当前切换的表标识
	 * 
	 * @author YXY
	 * @data 2014年10月21日 下午8:56:46
	 */
	private void saveTableCheck() {
		logDao.saveTableMark(tableMark);
	}

	/**
	 * 保存活动数据
	 * 
	 * @param YXY
	 * @since 2015年5月14日 下午2:16:41
	 * @author LSQ
	 */
	public void saveActivityLog(ActivityLog value) {
		logDao.saveActivityLog(value);
	}
}