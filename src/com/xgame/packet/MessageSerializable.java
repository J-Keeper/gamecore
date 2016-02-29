package com.xgame.packet;

import java.util.Map;

/**
 * 消息序列化接口,用来将BO(业务逻辑)对象转为DTO(数据传输)对象
 * 
 * @author YXY
 * @date 2016年2月19日 下午2:32:28
 */
public interface MessageSerializable {
	// 如果需要自己序列化可以实现该
	public Map<String, Object> toMsg();

}