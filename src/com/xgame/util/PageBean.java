package com.xgame.util;

import java.util.ArrayList;
import java.util.List;

public class PageBean<T> {
	/** 当前页码 */
	private int currPage = 1;
	/** 每页大小 */
	private int pageSize = 20;
	/** 总页数 */
	private int allPage;
	/** 总条数 */
	private int count;
	/** 结果 */
	private List<T> result;

	public PageBean(int currPage, int pageSize, List<T> allResult) {
		if (pageSize > 0) {
			this.pageSize = pageSize;
		}
		if (currPage > 0) {
			this.currPage = currPage;
		}
		if (allResult == null) {
			result = new ArrayList<>();
			return;
		}
		this.count = allResult.size();
		this.allPage = count % this.pageSize == 0 ? count / this.pageSize
				: (count / this.pageSize + 1);
		this.result = new ArrayList<>();
		int start = (this.currPage - 1) * this.pageSize;
		if (allResult.size() > start) {
			for (int i = start; i < allResult.size(); i++) {
				if (i >= start) {
					result.add(allResult.get(i));
				}
				if (result.size() >= this.pageSize) {
					break;
				}
			}
		}
	}

	public int getCurrPage() {
		return currPage;
	}

	public int getAllPage() {
		return allPage;
	}

	public int getCount() {
		return count;
	}

	public int getPageSize() {
		return pageSize;
	}

	public List<T> getResult() {
		return result;
	}
}
