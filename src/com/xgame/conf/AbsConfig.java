package com.xgame.conf;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.nutz.dao.entity.Record;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.impl.NutDao;
import org.nutz.lang.Strings;

import com.yxy.core.GameContext;
import com.yxy.core.framework.IConfig;
import com.yxy.core.util.Config;
import com.yxy.core.util.ConfigBuilder;

/**
 * 配置装配父类(配置控制)<br/>
 * 子类必须覆盖setValue方法
 * 
 * @author YXY
 * @data 2014年11月24日 下午3:02:28
 */
public abstract class AbsConfig implements IConfig {

	protected NutDao dao;

	@Override
	public void load(GameContext context) {
		dao = context.getIoc(NutDao.class, "configDao");
		setValue();
	}

	@Override
	public void reload(GameContext context) {
		load(context);
	}

	/**
	 * 加载配置并且放入集合
	 * 
	 * @author YXY
	 * @param <T>
	 *            存放配置的容器list或者map
	 * @data 2014年11月21日 下午2:31:11
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> T loadValue(T t, Class<?> clazz) {
		try {
			/** 容器判断 **/
			Map map = null;
			List list = null;
			if (t == null) {
				throw new Exception("Map或者list没有初始化");
			}
			if (t instanceof Map) {
				map = (Map) t;
			} else if (t instanceof List) {
				list = (List) t;
			} else {
				throw new Exception("必须使用Map或者list存放配置");
			}
			if (clazz == null)
				throw new Exception("类为空");

			Table table = clazz.getAnnotation(Table.class);
			if (table == null)
				throw new Exception("表名为空");

			String tName = table.value();
			if (Strings.isBlank(tName))
				throw new Exception("表名为空");
			Config conf = clazz.getAnnotation(Config.class);
			/** 设置数据 */
			List<Record> recordList = dao.query(tName, null);
			Properties prop = null;
			for (Record record : recordList) {
				prop = new Properties();
				Set<String> columnNames = record.getColumnNames();
				for (String name : columnNames) {
					String value = record.getString(name);
					if (value != null)
						prop.setProperty(name, value);
				}
				Object change = ConfigBuilder.autowired(prop, clazz);
				Object cast = clazz.cast(change);
				if (map != null) {
					if (conf == null)
						throw new Exception(
								"存放的容器map，Class必须指定配置@Config,如@Config(getId =\"getId\")");
					Method method = clazz.getMethod(conf.getKey());
					Object id = method.invoke(cast);
					map.put(id, change);
				} else if (list != null) {
					list.add(change);
				}
			}
			/** 返回 */
			if (map != null) {
				t = (T) map;
			} else {
				t = (T) list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	public abstract void setValue();
}
