package com.xgame.module.account.dao;

import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.yxy.core.cache.DataProvide;

@IocBean(create = "init")
public class AccountDao implements DataProvide<String, Account> {
	@Inject("refer:gameDao")
	private NutDao dao;

	public void init() {
		dao.create(Account.class, false);
	}

	public Account get(String key) {
		Account account = (Account) dao.fetch(Account.class, key);
		return account;
	}

	public Account get(String name, int sid) {
		Account account = (Account) dao.fetchx(Account.class, name, sid);
		return account;
	}

	public void add(String key, Account value) {
		dao.insert(value);
	}

	public void update(String key, Account value) {
		dao.update(value);
	}

	public void delete(String key) {
		dao.delete(Account.class, key);
	}

}