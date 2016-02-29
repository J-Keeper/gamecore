package com.xgame.web;

import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Modules;

import com.xgame.web.module.OperateModule;

/**
 * web主模块
 * 
 * @author yaowenhao
 * @date 2014年7月12日 下午4:44:02
 */
@Modules({ OperateModule.class })
@Fail("void")
public class MainModule {
}