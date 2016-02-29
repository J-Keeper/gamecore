package com.yxy.core.event;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 事件监听处理接口 作用在方法之上
 * @author YongXinYu
 * @date 2015年8月5日 下午10:02:40
 */
@Target({ java.lang.annotation.ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Listener {

	public int value();
}