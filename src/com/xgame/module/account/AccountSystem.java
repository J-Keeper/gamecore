package com.xgame.module.account;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.mina.core.session.IoSession;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xgame.event.EventType;
import com.xgame.module.account.dao.Account;
import com.xgame.module.account.dao.AccountManager;
import com.xgame.module.avatar.AvatarSystem;
import com.xgame.module.avatar.dao.Avatar;
import com.xgame.module.blog.dao.StatOnline;
import com.xgame.packet.MessageState;
import com.xgame.packet.MessageType;
import com.yxy.core.GameContext;
import com.yxy.core.event.Event;
import com.yxy.core.event.Listener;
import com.yxy.core.framework.SystemAdapter;
import com.yxy.core.msg.Attribute;
import com.yxy.core.msg.MessageConstant;
import com.yxy.core.msg.Optional;
import com.yxy.core.msg.Packet;
import com.yxy.core.msg.Required;
import com.yxy.core.net.codec.Message;

/**
 * 账号系统,包含账号的创建,登陆,下线等逻辑
 * 
 * @author YXY
 * @date 2016年2月19日 下午3:11:16
 */
@IocBean(create = "load")
public class AccountSystem extends SystemAdapter {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Inject
	private AccountManager accountManager;
	@Inject("refer:gameContext")
	private GameContext context;
	@Inject
	private AvatarSystem avatarSystem;

	@Override
	public void time2save() {
		accountManager.time2save();
	}

	// 上线处理
	@Listener(EventType.ONLINE)
	public void online(Event event) {
		Avatar avatar = (Avatar) event.getSource();
		Account account = accountManager.getAccount(avatar.getAccount());
		if (account.getAvatarId() != avatar.getId()) {
			account.setAvatarId(avatar.getId());
			accountManager.updateAccount(account);
		}
		account.getSession().setAttribute(MessageConstant.ATTR_AVATAR_ID,
				avatar.getId());
		accountManager.updateAidMap(account);
	}

	@Override
	public void destroy() {
		accountManager.destroy();
	}

	/**
	 * 玩家下线
	 * 
	 * @param msg
	 * @return
	 */
	@Packet(type = MessageType.LOGOUT_REQ, isAvatar = false)
	public Message logout(
			@Attribute(MessageConstant.ATTR_USERNAME) String username,
			@Attribute(MessageConstant.ATTR_AVATAR_ID) Object roleIdObj,
			IoSession session) {
		Account account = accountManager.getAccount(username);
		long sessionId = session.getId();
		long currSessionId = -1L;
		if ((account != null) && (account.getSession() != null)) {
			currSessionId = account.getSession().getId();
		} else {
			currSessionId = sessionId;
		}
		if (currSessionId != sessionId) {
			return null;
		}
		if (roleIdObj != null) {
			long roleId = (long) roleIdObj;
			Avatar avatar = avatarSystem.getAvatar(roleId);
			Event event = new Event(EventType.OFFLINE, avatar);
			context.addEvent(event);
		}
		account = accountManager.removeAccount(username);
		if (account != null) {
			account.setSession(null);
			log.info("账号[{}]下线,IP{}", username, account.getIp());
		}
		return null;
	}

	/**
	 * 用户登录
	 * 
	 * @param in
	 */
	@Packet(type = MessageType.LOGIN_REQ, isAvatar = false)
	public Message login(@Required("name") String name,
			@Required("pass") String token,
			@Required("platform") String platform, @Optional("os") int os,
			@Optional("sid") int sid, IoSession session) {

		System.out.println("收到登陆消息 " + name + "@" + platform);
		Account account = validate(name, token, platform, sid);
		Message msg = new Message(MessageType.LOGIN_RES);
		if (account != null) {
			session.setAttribute(MessageConstant.ATTR_IS_LOGIN,
					Boolean.valueOf(true));
			session.setAttribute(MessageConstant.ATTR_USERNAME, name);
			checkSession(session, name);
			account.setLastTime(new Date());
			account.setSession(session);
			account.setOs(os);
			if (Strings.isBlank(account.getIp())) {
				log.info("新账号登录:" + account.getName()
						+ session.getRemoteAddress());
				account.setIp(session.getRemoteAddress().toString());
				accountManager.addAccount(account);
			} else {
				log.info("老账号登录:" + account.getName()
						+ session.getRemoteAddress());
				account.setIp(session.getRemoteAddress().toString());
				accountManager.updateAccount(account);
			}
			long aid = account.getAvatarId();
			msg.put("avatarId", aid); // 若为0表示未创建角色
		} else {
			log.warn("账号登陆失败(令牌有误):" + name + session.getRemoteAddress());
			msg.setState(MessageState.PASS_ERROR);
		}
		return msg;
	}

	/**
	 * 重复登陆检查
	 * 
	 * @param session
	 * @param name
	 */
	private void checkSession(IoSession session, String name) {
		Account oldAccount = accountManager.getAccount(name);
		IoSession oldSession = oldAccount == null ? null : oldAccount
				.getSession();
		if ((oldSession != null) && (!oldSession.equals(session))) {
			Message msg = new Message(MessageType.LOGOUT_RES);
			msg.setState(MessageState.LOGIN_OTHER);
			msg.put("info", "您的账号已在别处登录,您被迫下线!");
			oldSession.write(msg);
			log.info(
					"账号[{}]重复登录{}/原有连接{}/已置为下线状态",
					new Object[] { oldAccount.getName(),
							session.getRemoteAddress(),
							oldSession.getRemoteAddress() });

			oldSession.setAttribute(MessageConstant.ATTR_IS_LOGIN, false);
			Object roleIdObj = oldSession
					.removeAttribute(MessageConstant.ATTR_AVATAR_ID);
			if (roleIdObj != null) {
				session.setAttribute(MessageConstant.ATTR_AVATAR_ID, roleIdObj);
			}
		}
	}

	/**
	 * 创建一个账号
	 * 
	 * @param userid
	 * @param password
	 * @param platform
	 * @return
	 */
	private Account createAccount(String userid, String password,
			String platform, int sid) {
		Account account = new Account(userid, password, platform);
		account.setLastTime(new Date());
		account.setCreateTime(new Date());
		account.setSid(sid);
		return account;
	}

	/**
	 * 验证逻辑,后期可以改为分平台走不同的验证逻辑
	 * 
	 * @param name
	 * @param password
	 * @param token
	 * @return
	 */
	private Account validate(String name, String token, String platform, int sid) {
		if (Strings.isBlank(name) || Strings.isBlank(token)) {
			return null;
		}
		switch (platform) {
		case "yxy": // 测试登录
			Account account = accountManager.queryAccount(name, sid);
			if (account == null) {
				account = createAccount(name, token, platform, sid);
				return account;
			}
			if ((!account.getName().equals(name))
					|| (!account.getToken().equals(token))) {
				return null;
			}
			return account;
		default:
			Map<String, Object> params = new HashMap<>();
			params.put("acc", name);
			params.put("sid", sid);
			params.put("token", token);
			// 向用户服务器发送请求验证请求
			// String url = context.getConfig("url.getUserKey");
			// Response resp = Http.post2(url, params, 10 * 1000);
			// String pwd = resp.getContent();
			// if (!"success".equals(pwd)) {
			// return null;
			// }
			account = accountManager.queryAccount(name, sid);
			if (account == null) {
				account = createAccount(name, token, platform, sid);
			}
			account.setToken(token);
			return account;
		}
	}

	public Account getAccount(String username) {
		return accountManager.getAccount(username);
	}

	public Account queryAccount(String accName, int sid) {
		if (Strings.isBlank(accName)) {
			return null;
		}
		Account account = accountManager.queryAccount(accName, sid);
		return account;
	}

	/**
	 * 给指定Aid的在线角色发送消息
	 * 
	 * @param avatarId
	 */
	public boolean sendMsg(long avatarId, Message msg) {
		Account account = accountManager.getAccountByAid(avatarId);
		if (account == null) {
			return false;
		}
		account.getSession().write(msg);
		return true;
	}

	/**
	 * 给所有的在线角色发送消息
	 * 
	 * @param msg
	 */
	public void sendAll(Message msg) {
		for (Account account : accountManager.getAllAccount())
			account.getSession().write(msg);
	}

	/**
	 * 发送所有人
	 * 
	 * @param msg
	 * @param excludeList
	 *            排除的人
	 * @since 2015年3月16日 下午6:21:07
	 * @author LSQ
	 */
	public void sendAll(Message msg, List<Long> excludeList) {
		if (excludeList == null || excludeList.size() <= 0) {
			for (Account account : accountManager.getAllAccount()) {
				account.getSession().write(msg);
			}
		} else {
			for (Account account : accountManager.getAllAccount()) {
				if (!excludeList.contains(account.getAvatarId())) {
					account.getSession().write(msg);
				}
			}
		}
	}

	/**
	 * T人下线
	 * 
	 * @param accName
	 * @return
	 */
	public String kick(String accName) {
		Account account = accountManager.getAccount(accName);
		account.getSession().setAttribute(MessageConstant.ATTR_IS_LOGIN, false);
		String ans = String.format("账号[%s]已被T除,连接%s/已置为下线状态", new Object[] {
				accName, account.getSession().getRemoteAddress() });
		log.info(ans);
		return ans;
	}

	/**
	 * 统计在线人数
	 * 
	 * @return
	 * @since 2015年5月7日 下午5:30:17
	 * @author Yongxinyu
	 */
	public StatOnline countAll() {
		int sessionNum = 0;
		int androidNum = 0;
		int iosNum = 0;
		int aPadNum = 0;
		int iPadNum = 0;
		Iterator<Account> it = accountManager.getGLOBAL_ACCOUNT().values()
				.iterator();
		while (it.hasNext()) {
			Account ac = it.next();
			sessionNum++;
			if (ac.getOs() == 1)
				androidNum++;
			else if (ac.getOs() == 2)
				iosNum++;
			else if (ac.getOs() == 3)
				aPadNum++;
			else if (ac.getOs() == 4)
				iPadNum++;
		}
		int onlineNum = avatarSystem.getAllAvatar().size();
		StatOnline sol = new StatOnline();
		sol.setAndroidNum(androidNum);
		sol.setIosNum(iosNum);
		sol.setaPadNum(aPadNum);
		sol.setiPadNum(iPadNum);
		sol.setOnlineNum(onlineNum);
		sol.setSessionNum(sessionNum);
		sol.setStatTime(new Date());
		return sol;
	}
}
