package com.xgame.web.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xgame.module.account.AccountSystem;
import com.xgame.module.account.dao.Account;
import com.xgame.module.avatar.AvatarSystem;
import com.xgame.module.avatar.dao.Avatar;
import com.xgame.web.filter.DESDecryptFilter;
import com.yxy.core.GameContext;
import com.yxy.core.Halt;
import com.yxy.core.framework.ISystem;

/**
 * 游戏控制web入口
 * 
 * @since 2015年3月17日 上午11:02:51
 * @author YXY
 */
@IocBean
@InjectName
@At("/opt")
@Fail("void")
@Filters(@By(type = DESDecryptFilter.class))
public class OperateModule {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Inject
	private GameContext gameContext;
	@Inject
	private AvatarSystem avatarSystem;
	@Inject
	private AccountSystem accountSystem;

	@At("/stop")
	public WebResult stop() {
		log.warn("收到来至web入口的停服请求,服务器即将关闭!");
		new Halt().start();
		return new WebResult(WebResult.SUC, "关闭服务器成功!");
	}

	@At
	@Ok("raw")
	public String kickAccount(@Param("acc") String accName,
			@Param("key") String token) {
		AccountSystem accountSystem = gameContext
				.getSystem(AccountSystem.class);
		Account account = accountSystem.getAccount(accName);
		if (account == null || !account.getToken().equals(token)) {
			return "fail";
		}
		accountSystem.kick(accName);
		return "ok";
	}

	/**
	 * 正式充值接口
	 * 
	 * @param param
	 * @return
	 * @since 2015年3月31日 下午4:45:33
	 * @author Yongxinyu
	 */
	@At("/recharge")
	@Ok("json")
	public WebResult recharge(@Param("acc") String accName,
			@Param("money") float money, @Param("monthCard") int type,
			@Param("orderId") String orderId, @Param("serverId") int sid) {
		return new WebResult(WebResult.SUC, "充值成功");
	}

	/**
	 * 分页查询所有在线玩家
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSzie
	 *            每页数据记录条数
	 * @return
	 */
	@At("/all")
	@Ok("json")
	public WebResult getOnlineAll(@Param("currentPage") int pageNumber,
			@Param("pageSize") int pageSize) {
		AvatarSystem avatarSystem = gameContext.getSystem(AvatarSystem.class);
		List<Avatar> list = avatarSystem.getAllAvatar();
		int maxPage = list.size() / pageSize + 1;

		// 参数校验
		if (maxPage < pageNumber || pageNumber < 1) {
			return new WebResult(WebResult.PARAMS_NULL, "参数错误，无效的页码或条数");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("amountSize", list.size());
		map.put("records", getPageAvatars(list, pageNumber, pageSize));
		return new WebResult(WebResult.SUC, map);
	}

	/**
	 * 玩家信息分页
	 * 
	 * @author Yongxinyu
	 * @param avas
	 *            角色对象
	 * @param pageNumber
	 *            页码
	 * @param pageSize
	 *            每页的记录数量
	 * @return
	 */
	public List<Avatar> getPageAvatars(List<Avatar> avas, int pageNumber,
			int pageSize) {
		List<Avatar> list = new ArrayList<>();
		if (avas.size() <= pageSize) {
			return avas;
		}
		int sumPage = avas.size() / pageSize;
		// 数据分页
		int begin = pageSize * (pageNumber - 1);
		int end = begin;
		if (pageNumber <= sumPage) {
			end = pageSize * pageNumber - 1;
		} else {
			end = avas.size() - 1;
		}
		for (int i = begin; i <= end; i++) {
			list.add(avas.get(i));
		}
		return list;
	}

	/**
	 * 分页查询所有玩家
	 * 
	 * @author yongxinyu
	 * @data 2014年12月1日 下午4:57:26
	 * @param name
	 * @return
	 */
	@At("/allAvatar")
	@Ok("json")
	public WebResult seachAvatar(@Param("aid") long aid,
			@Param("accName") String accName, @Param("sid") int sid,
			@Param("name") String name, @Param("currentPage") int pageNumber,
			@Param("pageSize") int pageSize,
			@Param("startDate") long startDate, @Param("endDate") long endDate) {
		AvatarSystem avatarSystem = gameContext.getSystem(AvatarSystem.class);
		List<Avatar> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		Avatar avatar = null;
		if (aid != 0) {
			avatar = avatarSystem.getAvatar(aid);
			if (avatar == null) {
				return new WebResult(WebResult.AVATAR_NULL, "玩家不存在");
			} else {
				list.add(avatar);
				map.put("amountSize", list.size());
				map.put("records", getPageAvatars(list, pageNumber, pageSize));
				return new WebResult(WebResult.SUC, map);
			}
		}
		if (!Strings.isBlank(accName)) {
			AccountSystem as = gameContext.getSystem(AccountSystem.class);
			Account account = as.queryAccount(accName, sid);
			if (account == null) {
				return new WebResult(WebResult.AVATAR_NULL, "不存在该玩家数据");
			} else {
				long avatarId = account.getAvatarId();
				avatar = avatarSystem.getAvatar(avatarId);
				if (avatar == null) {
					return new WebResult(WebResult.AVATAR_NULL, "不存在该玩家数据");
				} else {
					list.add(avatar);
					map.put("amountSize", list.size());
					map.put("records",
							getPageAvatars(list, pageNumber, pageSize));
					return new WebResult(WebResult.SUC, map);
				}
			}
		}
		list = avatarSystem.seachAvatar(name, startDate, endDate);
		int maxPage = list.size() / pageSize + 1;
		// 参数校验
		if (maxPage < pageNumber || pageNumber < 1) {
			return new WebResult(WebResult.PARAMS_NULL, "参数错误，无效的页码或条数");
		}
		map.put("amountSize", list.size());
		map.put("records", getPageAvatars(list, pageNumber, pageSize));
		return new WebResult(WebResult.SUC, map);
	}

	/**
	 * 全服角色数
	 * 
	 * @return
	 * @since 2015年5月30日 下午2:07:14
	 * @author LSQ
	 */
	@At("/avaCount")
	@Ok("json")
	public WebResult queryAvatarCount() {
		return new WebResult(WebResult.SUC, avatarSystem.getAllId().size());
	}

	@At("/query/*")
	@Ok("json")
	public WebResult queryAvatar(@Param("aid") long aid) {
		Avatar avatar = avatarSystem.getAvatar(aid);
		if (avatar == null) {
			return new WebResult(WebResult.AVATAR_NULL, "玩家不存在");
		}
		return new WebResult(WebResult.SUC, avatar);
	}

	/**
	 * 发送邮件
	 * 
	 * @param avatarId
	 *            逗号分隔的一个或多个玩家,all表示所有玩家
	 * @param title
	 * @param context
	 * @param awards
	 * @return
	 * @since 2015年3月25日 下午5:52:59
	 * @author YXY
	 */
	@At("/sendMail/*")
	@Ok("json")
	public WebResult sendMail(@Param("aid") String avatarId,
			@Param("title") String title, @Param("content") String content,
			@Param("awards") String awards) {
		return new WebResult(WebResult.SUC, "邮件发送成功！");
	}

	/***
	 * 重新加载数据配置
	 * 
	 * @param sysName
	 * @return WebResult
	 * @throws
	 * @author YXY
	 */
	@At("/reload/*")
	@Ok("json")
	public WebResult reload(@Param("sysName") String sysName) {
		ISystem sys = gameContext.getSystem(sysName);
		if (sys == null) {
			return new WebResult(WebResult.RELOAD_FAIL, "系统[" + sysName
					+ "]不存在");
		} else {
			try {
				sys.reload();
				log.info("系统[" + sysName + "]加载配表成功");
				return new WebResult(WebResult.SUC, "系统[" + sysName + "]加载配表成功");
			} catch (Exception e) {
				return new WebResult(WebResult.RELOAD_FAIL, "系统[" + sysName
						+ "]加载配表失败" + e.getMessage());
			}
		}
	}

}
