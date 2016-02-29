package com.yxy.core.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
	public String alias() default "";

	public String parser() default "";

	public String getKey() default "getKey";

	public boolean allowNull() default false;
}