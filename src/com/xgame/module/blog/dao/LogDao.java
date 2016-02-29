package com.xgame.module.blog.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Sqls;
import org.nutz.dao.TableName;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@IocBean(create = "init")
public class LogDao {
	@Inject("refer:logNDao")
	private NutDao dao;

	public void init() {
		dao.create(StatOnline.class, false);
		dao.create(StartStop.class, false);
		dao.create(TableMark.class, false);
		dao.create(ActivityLog.class, false);
		for (int i = 0; i < 12; i++) {
			TableName.set(i);
			dao.create(Blog.class, false);
			TableName.clear();
		}
	}

	public void addLog(final Blog blog, boolean isClear) {
		int tName = blog.getTableCid();
		TableName.set(tName);
		if (isClear) {
			dao.clear(Blog.class);
		}
		dao.insert(blog);
		TableName.clear();
	}

	public void saveLog(final Blog blog) {
		dao.insert(blog);
	}

	public void addOnlineStat(StatOnline sol) {
		dao.insert(sol);
	}

	public StartStop onStart(StartStop value) {
		return dao.insert(value);
	}

	public void onShutdown(StartStop value) {
		dao.update(value);
	}

	public void saveActivityLog(ActivityLog value) {
		dao.insert(value);
	}

	public StartStop getFirstStartTime() {
		StartStop ss = dao.fetch(StartStop.class, 1L);
		return ss;
	}

	public TableMark getTableMark() {
		return dao.fetch(TableMark.class);
	}

	public void saveTableMark(TableMark tableMark) {
		int clunt = dao.count(TableMark.class);
		if (clunt > 0) {
			dao.update(tableMark);
		} else {
			dao.insert(tableMark);
		}
	}

	/**
	 * 获取当天日志表名称
	 * 
	 * @author Yongxinyu
	 * @return
	 */
	public String getTableName() {
		Calendar cal = Calendar.getInstance();
		String tableName = "logs_" + cal.get(Calendar.WEEK_OF_YEAR) % 12;
		return tableName;
	}

	/**
	 * 统计当天兵器谱声望产出值
	 * 
	 * @author Yongxinyu
	 * @return
	 * 
	 */
	public long countWeaponsPrestige() {
		String logs = getTableName();
		Sql sql = Sqls
				.create("SELECT  SUM(obtainPrestige) as sum  FROM  "
						+ logs
						+ " WHERE opt = 'weapons_award' AND date(createTime) =curdate() - INTERVAL 1 day");
		sql.setCallback(new SqlCallback() {
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				long sum = 0;
				while (rs.next()) {
					sum = rs.getLong("sum");
				}
				return sum;
			}
		});
		dao.execute(sql);
		return (long) sql.getResult();
	}

	/**
	 * 统计打擂台当天声望产出值
	 * 
	 * @author Yongxinyu
	 * @return
	 * 
	 */
	public long countArenaPrestige() {
		String logs = getTableName();
		Sql sql = Sqls
				.create("SELECT obtainItem FROM "
						+ logs
						+ " WHERE opt = 'mail_getItem' AND obtainItem LIKE '%i70001%' AND date(createTime) =curdate() - INTERVAL 1 day");
		sql.setCallback(new SqlCallback() {
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				long sum = 0;
				while (rs.next()) {
					String obtainItem = rs.getString("obtainItem");
					if (obtainItem == null) {
						continue;
					}
					JSONObject json = JSONObject.parseObject(obtainItem);
					sum += Integer.parseInt(json.getString("i70001"));
				}
				return sum;
			}
		});
		dao.execute(sql);
		return (long) sql.getResult();
	}

	/**
	 * 统计当天声望消耗
	 * 
	 * @author Yongxinyu
	 * @return
	 */
	public long countPrestigeCost() {
		String logs = getTableName();
		Sql sql = Sqls
				.create("SELECT SUM(lostPrestige) AS sum  FROM "
						+ logs
						+ " WHERE  opt = 'weapons_transction' AND date(createTime) =curdate() - INTERVAL 1 day");
		sql.setCallback(new SqlCallback() {
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				long sum = 0;
				while (rs.next()) {
					sum = rs.getLong("sum");
				}
				return sum;
			}
		});
		dao.execute(sql);
		return (long) sql.getResult();
	}

	/**
	 * 统计今天签到礼包的领取情况
	 * 
	 * @return 礼包类型-人数
	 */
	@SuppressWarnings("unchecked")
	public Map<Integer, Integer> countSignGift() {
		String logs = getTableName();
		Sql sql = Sqls
				.create("SELECT note  FROM  "
						+ logs
						+ "  WHERE  note IS NOT NULL AND opt IN('gift_sign','gift_servenday') AND date(createTime) =curdate() - INTERVAL 1 day");
		sql.setCallback(new SqlCallback() {
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				Map<Integer, Integer> map = new HashMap<>();
				String note = "";
				while (rs.next()) {
					note = rs.getString("note");
					JSONObject jo = JSON.parseObject(note);
					int day = (int) jo.get("type");
					if (!map.containsKey(day)) {
						map.put(day, 1);
					} else {
						switch (day) {
						case 1:
							map.put(1, map.get(1) + 1);
							break;
						case 2:
							map.put(2, map.get(2) + 1);
							break;
						case 3:
							map.put(3, map.get(3) + 1);
							break;
						case 4:
							map.put(4, map.get(4) + 1);
							break;
						case 5:
							map.put(5, map.get(5) + 1);
							break;
						case 6:
							map.put(6, map.get(6) + 1);
							break;
						case 7:
							map.put(7, map.get(7) + 1);
							break;
						default:
							break;
						}
					}
				}
				return map;
			}
		});
		dao.execute(sql);
		return (Map<Integer, Integer>) sql.getResult();
	}

	// 统计等级礼包领取人数
	@SuppressWarnings("unchecked")
	public Map<Integer, Integer> countLevelGiftNum() {
		String logs = getTableName();
		Sql sql = Sqls
				.create("SELECT note  FROM  "
						+ logs
						+ "  WHERE  note IS NOT NULL AND opt='gift_level' AND date(createTime) =curdate() - INTERVAL 1 day");
		sql.setCallback(new SqlCallback() {
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				Map<Integer, Integer> map = new HashMap<>();
				String note = "";
				while (rs.next()) {
					note = rs.getString("note");
					JSONObject jo = JSON.parseObject(note);
					int level = (int) jo.get("type");
					if (!map.containsKey(level)) {
						map.put(level, 1);
					} else {
						switch (level) {
						case 5:
							map.put(5, map.get(5) + 1);
							break;
						case 10:
							map.put(10, map.get(10) + 1);
							break;
						case 15:
							map.put(15, map.get(15) + 1);
							break;
						case 20:
							map.put(20, map.get(20) + 1);
							break;
						case 25:
							map.put(25, map.get(25) + 1);
							break;
						case 30:
							map.put(30, map.get(30) + 1);
							break;
						case 35:
							map.put(35, map.get(35) + 1);
							break;
						case 40:
							map.put(40, map.get(40) + 1);
							break;
						case 45:
							map.put(45, map.get(45) + 1);
							break;
						case 50:
							map.put(50, map.get(50) + 1);
							break;
						case 55:
							map.put(55, map.get(55) + 1);
							break;
						case 60:
							map.put(60, map.get(60) + 1);
							break;
						// 等级礼包提升之后，这里添加相应的等级
						default:
							break;
						}
					}

				}
				return map;
			}
		});
		dao.execute(sql);
		return (Map<Integer, Integer>) sql.getResult();
	}

	// 统计成长基金领取人数
	@SuppressWarnings("unchecked")
	public Map<Integer, Integer> countGrowthFundsNum() {
		String logs = getTableName();
		Sql sql = Sqls
				.create("SELECT note  FROM  "
						+ logs
						+ "  WHERE  note IS NOT NULL AND opt='activity_growthfundsget' AND date(createTime) =curdate() - INTERVAL 1 day");
		sql.setCallback(new SqlCallback() {
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				Map<Integer, Integer> map = new HashMap<>();
				String note = "";
				while (rs.next()) {
					note = rs.getString("note");
					JSONObject jo = JSON.parseObject(note);
					int type = (int) jo.get("getLevel");
					if (!map.containsKey(type)) {
						map.put(type, 1);
					} else {
						switch (type) {
						case 5:
							map.put(5, map.get(5) + 1);
							break;
						case 10:
							map.put(10, map.get(10) + 1);
							break;
						case 15:
							map.put(15, map.get(15) + 1);
							break;
						case 20:
							map.put(20, map.get(20) + 1);
							break;
						case 25:
							map.put(25, map.get(25) + 1);
							break;
						case 30:
							map.put(30, map.get(30) + 1);
							break;
						case 35:
							map.put(35, map.get(35) + 1);
							break;
						case 40:
							map.put(40, map.get(40) + 1);
							break;
						case 45:
							map.put(45, map.get(45) + 1);
							break;
						case 50:
							map.put(50, map.get(50) + 1);
							break;
						case 55:
							map.put(55, map.get(55) + 1);
							break;
						case 60:
							map.put(60, map.get(60) + 1);
							break;
						// 成长基金礼包提升之后，这里添加相应的等级
						default:
							break;
						}
					}

				}
				return map;
			}
		});
		dao.execute(sql);
		return (Map<Integer, Integer>) sql.getResult();
	}

	// 首冲统计 等级-人数 额度-人数 等级-总额度
	@SuppressWarnings("unchecked")
	public Map<String, String> countFRCNum() {
		String logs = getTableName();
		Sql sql = Sqls
				.create("SELECT note  FROM  "
						+ logs
						+ "  WHERE  note IS NOT NULL AND opt='recharge' AND date(createTime) =curdate() - INTERVAL 1 day");
		sql.setCallback(new SqlCallback() {
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				Map<Integer, Integer> LNMap = new HashMap<>();// 等级-人数
				Map<Float, Integer> MNMap = new HashMap<>();// 额度-人数
				Map<Integer, Float> LMMap = new HashMap<>();// 等级-总额度
				String note = "";
				while (rs.next()) {
					note = rs.getString("note");
					JSONObject jo = JSON.parseObject(note);
					int type = jo.getIntValue("type");
					// 非首冲
					if (type < 100) {
						continue;
					}
					// 等级-人数
					int level = (int) jo.get("level");
					if (!LNMap.containsKey(level)) {
						LNMap.put(level, 1);
					} else {
						LNMap.put(level, LNMap.get(level) + 1);
					}
					// 额度-人数
					float money = jo.getFloat("money");
					if (!MNMap.containsKey(money)) {
						MNMap.put(money, 1);
					} else {
						MNMap.put(money, MNMap.get(money) + 1);
					}
					// 等级-总额度
					if (!LMMap.containsKey(level)) {
						LMMap.put(level, money);
					} else {
						LMMap.put(level, LMMap.get(level) + money);
					}
				}
				Map<String, String> map = new HashMap<>();
				map.put("LN", JSON.toJSONString(LNMap));
				map.put("MN", JSON.toJSONString(MNMap));
				map.put("LM", JSON.toJSONString(LMMap));
				return map;
			}
		});
		dao.execute(sql);
		return (Map<String, String>) sql.getResult();
	}

	// 统计当天充值 次数,人数
	@SuppressWarnings("unchecked")
	public List<Integer> countRCNum() {
		String logs = getTableName();
		Sql sql = Sqls
				.create("SELECT  COUNT(id) as times , COUNT(distinct(aid))  AS	pep  FROM  "
						+ logs
						+ "  WHERE  opt='recharge' AND date(createTime) =curdate() - INTERVAL 1 day");
		sql.setCallback(new SqlCallback() {
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				List<Integer> list = new LinkedList<>();
				while (rs.next()) {
					int times = rs.getInt("times");
					list.add(times);
					int pep = rs.getInt("pep");
					list.add(pep);
				}
				return list;
			}
		});
		dao.execute(sql);
		return (List<Integer>) sql.getResult();
	}

	/**
	 * 统计今天领取首冲礼包的人数
	 * 
	 * @return
	 */
	public int openFRCGNum() {
		String logs = getTableName();
		Sql sql = Sqls
				.create("SELECT  COUNT(id) as sum  FROM  "
						+ logs
						+ "  WHERE  opt='activity_firstgiftsend' AND date(createTime) =curdate()");
		sql.setCallback(new SqlCallback() {
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				int sum = 0;
				while (rs.next()) {
					sum = rs.getInt("sum");
				}
				return sum;
			}
		});
		dao.execute(sql);
		return (int) sql.getResult();
	}

	/**
	 * 查询玩家今天在线时长(s)
	 * 
	 * @param aid
	 *            角色id
	 * @return
	 */
	public int getTodeyOnlineTimeByAid(String aid) {
		String logs = getTableName();
		Sql sql = Sqls
				.create("SELECT	note  FROM  "
						+ logs
						+ "  WHERE	 aid='"
						+ aid
						+ "'  AND opt='offline' AND note IS NOT NULL  AND date(createTime) =curdate()");
		sql.setCallback(new SqlCallback() {
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				int sum = 0;
				while (rs.next()) {
					String note = rs.getString("note");
					JSONObject jo = JSON.parseObject(note);
					int onlineTime = (int) jo.get("onlineTime");
					sum += onlineTime;
				}
				return sum;
			}
		});
		dao.execute(sql);
		return (int) sql.getResult();
	}
}