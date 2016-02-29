package com.xgame.module.account.dao;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

import com.xgame.module.account.dao.Account;
import com.xgame.module.account.dao.AccountDao;
import com.yxy.core.cache.CacheMap;

/**
 * 账号数据管理
 * 
 * @author YXY
 * @date 2016年2月19日 下午3:10:55
 */
@IocBean(create = "init")
public class AccountManager {
	private CacheMap<String, Account> GLOBAL_ACCOUNT;
	private Map<Long, String> AID_USER_MAP;
	@Inject
	private AccountDao accountDao;

	public void init() {
		this.AID_USER_MAP = new ConcurrentHashMap<>();
		this.GLOBAL_ACCOUNT = new CacheMap<String, Account>(
				AccountManager.class.getName(), accountDao);
	}

	/**
	 * @param account
	 */
	public void updateAidMap(Account account) {
		AID_USER_MAP.put(account.getAvatarId(), account.getName());
	}

	/**
	 * 获取所有在线账号
	 * 
	 * @return
	 */
	public Collection<Account> getAllAccount() {
		return this.GLOBAL_ACCOUNT.values();
	}

	/**
	 * 
	 */
	public void time2save() {
		GLOBAL_ACCOUNT.updateAll();
	}

	/**
	 * 
	 */
	public void destroy() {
		GLOBAL_ACCOUNT.updateAll();
	}

	public Account getAccount(String accName) {
		Account account = null;
		if (accName != null) {
			account = this.GLOBAL_ACCOUNT.get(accName);
		}
		return account;
	}

	public Account getAccountByAid(long avatarId) {
		String username = this.AID_USER_MAP.get(avatarId);
		if (username == null) {
			return null;
		}
		Account account = this.GLOBAL_ACCOUNT.get(username);
		return account;
	}

	public boolean addAccount(Account account) {
		boolean ans = true;
		this.GLOBAL_ACCOUNT.putWithCache(account.getName(), account, true);
		return ans;
	}

	public Account removeAccount(String accName) {
		Account account = null;
		if (accName != null) {
			account = this.GLOBAL_ACCOUNT.removeWithCache(accName, true);
		}
		return account;
	}

	public CacheMap<String, Account> getGLOBAL_ACCOUNT() {
		return GLOBAL_ACCOUNT;
	}

	/**
	 * 查询指定名称的账户信息
	 * 
	 * @param accName
	 *            account name
	 * @return
	 */
	public Account queryAccount(String accName, int sid) {
		if (Strings.isBlank(accName)) {
			return null;
		}
		Account account = getAccount(accName);
		if (account == null || account.getSid() != sid) {
			account = accountDao.get(accName, sid);
		}
		return account;
	}

	/**
	 * 更新账号
	 * 
	 * @param account
	 */
	public void updateAccount(Account account) {
		this.GLOBAL_ACCOUNT.put(account.getName(), account);
		this.GLOBAL_ACCOUNT.update(account.getName());
	}

}
