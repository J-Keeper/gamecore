package com.xgame.module.account.dao;

import java.io.Serializable;
import java.util.Date;

import org.apache.mina.core.session.IoSession;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.PK;
import org.nutz.dao.entity.annotation.Table;

import com.yxy.core.net.codec.Message;

/**
 * 游戏账号
 * 
 * @author YXY
 * @date 2016年2月19日 下午3:09:57
 */
@Table("account")
@PK({ "name", "sid" })
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column
	private int sid; // 服务器ID
	@Column
	private String name; // 用户名
	@Column
	private String token; // 令牌
	@Column
	private String platform; // 渠道ID
	@Column
	private int adult; // 成年人认证
	@Column
	private int status; // 状态
	@Column
	private long avatarId; // 关联角色ID
	@Column
	private String ip; // 最近登录的IP
	@Column
	private int os; // 系统类型 1.Android 2.IOS 3.Android平板 4.IPAD
	@Column
	private Date lastTime; // 最后一次登录时间
	@Column
	private Date createTime; // 账号创建时间

	private transient IoSession session;

	public Account() {
	}

	public Account(String name, String token, String platform) {
		this.name = name;
		this.token = token;
		this.platform = platform;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPlatform() {
		return this.platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public int getAdult() {
		return this.adult;
	}

	public void setAdult(int adult) {
		this.adult = adult;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public long getAvatarId() {
		return avatarId;
	}

	public void setAvatarId(long avatarId) {
		this.avatarId = avatarId;
	}

	public Date getLastTime() {
		return this.lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public IoSession getSession() {
		return this.session;
	}

	public void setSession(IoSession session) {
		this.session = session;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void send(Message msg) {
		session.write(msg);
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public int getOs() {
		return os;
	}

	public void setOs(int os) {
		this.os = os;
	}

}