package com.xgame.module.blog.dao;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 表循环标识
 * 
 * @author YXY
 * @data 2014年10月22日 下午3:50:17
 */
@Table("tablemark")
public class TableMark {
	@Id
	public int id = 1;
	@Column
	public int tableCheke;

	public TableMark() {

	}

	public TableMark(int tableCheke) {
		this.tableCheke = tableCheke;
	}

	public int getTableCheke() {
		return tableCheke;
	}

	public void setTableCheke(int tableCheke) {
		this.tableCheke = tableCheke;
	}

}
