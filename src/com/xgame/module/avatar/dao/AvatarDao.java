package com.xgame.module.avatar.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.nutz.dao.Sqls;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.xgame.util.DateUtil;
import com.yxy.core.cache.DataProvide;

@IocBean(create = "init")
public class AvatarDao implements DataProvide<Long, Avatar> {
	@Inject("refer:gameDao")
	private NutDao dao;

	public void init() {
		dao.create(AvatarWrapper.class, false);
	}

	@Override
	public Avatar get(Long key) {
		AvatarWrapper wrap = dao.fetch(AvatarWrapper.class, key);
		if (wrap == null) {
			return null;
		}
		return wrap.unWrap();
	}

	@Override
	public void add(Long key, Avatar value) {
		AvatarWrapper wrap = new AvatarWrapper(value);
		dao.insert(wrap);
	}

	@Override
	public void update(Long key, Avatar value) {
		AvatarWrapper wrap = new AvatarWrapper(value);
		dao.update(wrap);
	}

	@Override
	public void delete(Long key) {
		dao.delete(AvatarWrapper.class, key);
	}

	/**
	 * 加载全服角色信息
	 * 
	 * @return
	 * @since 2015年2月25日 下午5:49:48
	 * @author Yongxinyu
	 */
	public ArrayList<Object[]> loadAllRoleInfo() {
		Sql sql = Sqls
				.create("SELECT id,name,vipLevel,level,score FROM avatar");
		sql.setCallback(new SqlCallback() {
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				ArrayList<Object[]> ans = new ArrayList<Object[]>();
				while (rs.next()) {
					long id = rs.getLong("id");
					String name = rs.getString("name");
					int vipLevel = rs.getInt("vipLevel");
					int level = rs.getInt("level");
					long score = rs.getLong("score");
					ans.add(new Object[] { id, name, vipLevel, level, score });
				}
				return ans;
			}
		});
		dao.execute(sql);
		ArrayList<Object[]> ans = (ArrayList<Object[]>) sql
				.getList(Object[].class);
		return ans;
	}

	/**
	 * 查询最大ID
	 * 
	 * @return
	 */
	public long getMaxId() {
		Sql sql = Sqls.create("SELECT max(id) mid FROM avatar");
		sql.setCallback(Sqls.callback.longValue());
		dao.execute(sql);
		return sql.getNumber().longValue();
	}

	/**
	 * 按军衔等级由高到低查询查询角色id
	 * 
	 * @param honorLevel
	 *            军衔等级要求
	 * @return
	 * @since 2015年3月3日 下午3:42:06
	 * @author Yongxinyu
	 */
	@SuppressWarnings("unchecked")
	public List<Long> queryByHonorLevel(int honorLevel) {
		Sql sql = Sqls.create("SELECT id FROM avatar WHERE " + honorLevel
				+ "<= honorLevel ORDER BY honorLevel,sumHonors,lastHonorDate");
		sql.setCallback(new SqlCallback() {
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				List<Long> list = new LinkedList<>();
				while (rs.next()) {
					long id = rs.getLong("id");
					list.add(id);
				}
				return list;
			}
		});
		dao.execute(sql);
		return (List<Long>) sql.getResult();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> scoreRank(int size) {
		Sql sql = Sqls
				.create("SELECT id,name,level,score FROM avatar ORDER BY score DESC,level DESC LIMIT "
						+ size);
		sql.setCallback(new SqlCallback() {
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				ArrayList<Object[]> ans = new ArrayList<Object[]>();
				while (rs.next()) {
					long aid = rs.getLong("id");
					String name = rs.getString("name");
					int level = rs.getInt("level");
					long score = rs.getLong("score");
					ans.add(new Object[] { aid, name, level, score });
				}
				return ans;
			}
		});
		dao.execute(sql);
		ArrayList<Object[]> ans = (ArrayList<Object[]>) sql.getResult();
		return ans;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> scoreActRank(int size) {
		Sql sql = Sqls
				.create("SELECT id,name,maxBats,lastCBMDate FROM avatar ORDER BY maxBats DESC,lastCBMDate ASC LIMIT "
						+ size);
		sql.setCallback(new SqlCallback() {
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				ArrayList<Object[]> ans = new ArrayList<Object[]>();
				while (rs.next()) {
					long aid = rs.getLong("id");
					String name = rs.getString("name");
					long maxBats = rs.getLong("maxBats");
					long lastCBMDate = rs.getLong("lastCBMDate");
					ans.add(new Object[] { aid, name, maxBats, lastCBMDate });
				}
				return ans;
			}
		});
		dao.execute(sql);
		ArrayList<Object[]> ans = (ArrayList<Object[]>) sql.getResult();
		return ans;
	}

	@SuppressWarnings("unchecked")
	public int selfScoreRank(long score) {
		Sql sql = Sqls
				.create("select count(id) as rank from avatar where score > "
						+ score);
		sql.setCallback(new SqlCallback() {
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				ArrayList<Object[]> ans = new ArrayList<Object[]>();
				while (rs.next()) {
					int rownum = rs.getInt("rank");
					ans.add(new Object[] { rownum });
				}
				return ans;
			}
		});
		dao.execute(sql);
		ArrayList<Object[]> ans = (ArrayList<Object[]>) sql.getResult();
		return (int) ans.get(0)[0];
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> honorRank(int size) {
		Sql sql = Sqls
				.create("SELECT id,name,sumHonors,level FROM avatar ORDER BY sumHonors DESC,level DESC LIMIT "
						+ size);
		sql.setCallback(new SqlCallback() {
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				ArrayList<Object[]> ans = new ArrayList<Object[]>();
				while (rs.next()) {
					long aid = rs.getLong("id");
					String name = rs.getString("name");
					int sumHonors = rs.getInt("sumHonors");
					int level = rs.getInt("level");
					ans.add(new Object[] { aid, name, sumHonors, level });
				}
				return ans;
			}
		});
		dao.execute(sql);
		ArrayList<Object[]> ans = (ArrayList<Object[]>) sql.getResult();
		return ans;
	}

	@SuppressWarnings("unchecked")
	public int selfHonorRank(int honor) {
		Sql sql = Sqls
				.create("select count(id) as rank from avatar where honor > "
						+ honor);
		sql.setCallback(new SqlCallback() {
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				ArrayList<Object[]> ans = new ArrayList<Object[]>();
				while (rs.next()) {
					int rownum = rs.getInt("rank");
					ans.add(new Object[] { rownum });
				}
				return ans;
			}
		});
		dao.execute(sql);
		ArrayList<Object[]> ans = (ArrayList<Object[]>) sql.getResult();
		return (int) ans.get(0)[0];
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> levelRank(int size) {
		Sql sql = Sqls
				.create("SELECT id,name,level,lastULDate FROM avatar ORDER BY level DESC,lastULDate ASC LIMIT "
						+ size);
		sql.setCallback(new SqlCallback() {
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				ArrayList<Object[]> ans = new ArrayList<Object[]>();
				while (rs.next()) {
					long aid = rs.getLong("id");
					String name = rs.getString("name");
					int level = rs.getInt("level");
					long lastULDate = rs.getLong("lastULDate");
					ans.add(new Object[] { aid, name, level, lastULDate });
				}
				return ans;
			}
		});
		dao.execute(sql);
		ArrayList<Object[]> ans = (ArrayList<Object[]>) sql.getResult();
		return ans;
	}

	public List<Object[]> getLoginLevelList() {
		long time = new Date().getTime() - 5 * DateUtil.NM;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = dateFormat.format(new Date(time));
		Sql sql = Sqls
				.create("SELECT level,count(level) AS count FROM avatar WHERE loginTime LIKE '"
						+ dateStr + "%' GROUP BY level ORDER BY level ASC");
		sql.setCallback(new SqlCallback() {
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				ArrayList<Object[]> ans = new ArrayList<Object[]>();
				while (rs.next()) {
					int level = rs.getInt("level");
					int count = rs.getInt("count");
					ans.add(new Object[] { level, count });
				}
				return ans;
			}
		});
		dao.execute(sql);
		ArrayList<Object[]> ans = (ArrayList<Object[]>) sql
				.getList(Object[].class);
		return ans;
	}
}
