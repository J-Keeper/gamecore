package com.yxy.core.cache;

public class CacheUseExample<E> {

	protected CacheMap<Long, E> groupMap;

	// ***初始化***
	public void init() {
		// groupMap = new CacheMap<>(EManager.class.getName(), daoName);
		// 构造方法的两个参数，第一个是某个类的名，第二个是daoName
	}

	// 关于dao关联的实现,举例
	// public class ExpeditionDao implements DataProvide<Long, Expedition> {
	// @Inject("refer:gameDao")
	// private NutDao dao;(这里以使用nutz的dao为样例，其他框架可以一次调用自己的dao)
	//
	// public void init() {
	// dao.create(ExpeditionWrapper.class, false);
	// }
	//
	// @Override
	// public void add(Long key, Expedition ed) {
	// ExpeditionWrapper wrap = new ExpeditionWrapper(ed);
	// dao.insert(wrap);
	// }
	//
	// @Override
	// public void delete(Long key) {
	// dao.delete(ExpeditionWrapper.class, key);
	// }
	//
	// @Override
	// public Expedition get(Long key) {
	// ExpeditionWrapper wrap = dao.fetch(ExpeditionWrapper.class, key);
	// if (wrap == null)
	// return null;
	// return wrap.unWrap();
	// }
	//
	// @Override
	// public void update(Long key, Expedition ed) {
	// ExpeditionWrapper wrap = new ExpeditionWrapper(ed);
	// dao.update(wrap);
	// }
	//
	// }

	public E getWithCache(long avatarId) {
		if (avatarId == 0) {
			return null;
		}
		return groupMap.getWithCache(avatarId, true);
	}

	public E addGroup(E g, long id) {
		return groupMap.putWithCache(id, g, true);
	}

	public E addGroup(long avatarId) {
		if (avatarId == 0) {
			return null;
		}
		E g = groupMap.get(avatarId);
		if (g == null) {
			g = getWithCache(avatarId);
		}
		return g;
	}

	public E getOrLoadGroup(long avatarId) {
		if (avatarId == 0)
			return null;
		E g = groupMap.get(avatarId);
		if (g == null)
			g = groupMap.queryWithDB(avatarId);
		return g;
	}

	public void removeGroup(long avatarId) {
		groupMap.removeWithCache(avatarId, true);
	}

	public void updateAll() {
		groupMap.updateAll();
	}

	public void update(long id) {
		groupMap.update(id);
	}
}
