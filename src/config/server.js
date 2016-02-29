var ioc = {
	// 读取配置文件
	config : {
		type : "org.nutz.ioc.impl.PropertiesProxy",
		fields : {
			paths : [ "game.properties" ]
		}
	},
	// 游戏服数据库
	gameDataSource : {
		type : "com.alibaba.druid.pool.DruidDataSource",
		events : {
			depose : "close"
		},
		fields : {
			driverClassName : "com.mysql.jdbc.Driver",
			url             : {java :"$config.get('db.game.url')"},
			username        : {java :"$config.get('db.game.username')"},
			password        : {java :"$config.get('db.game.password')"},
			initialSize     : 1,
			maxActive       : 50,
			testOnReturn    : true,
			// validationQueryTimeout : 5,
			validationQuery : "select 1"
		}
	},
	// Dao
	gameDao : {
		type : 'org.nutz.dao.impl.NutDao',
		args : [ {
			refer : "gameDataSource"
		} ]
	},
	// 配置数据库
	configDataSource : {
		type : "com.alibaba.druid.pool.DruidDataSource",
		events : {
			depose : "close"
		},
		fields : {
			driverClassName : "com.mysql.jdbc.Driver",
			url             : {java :"$config.get('db.config.url')"},
			username        : {java :"$config.get('db.config.username')"},
			password        : {java :"$config.get('db.config.password')"},
			initialSize     : 1,
			maxActive       : 50,
			testOnReturn    : true,
			// validationQueryTimeout : 5,
			validationQuery : "select 1"
		}
	},
	configDao : {
		type : 'org.nutz.dao.impl.NutDao',
		args : [ {
			refer : "configDataSource"
		} ]
	},
	// 日志统计数据库
	logDataSource : {
		type : "com.alibaba.druid.pool.DruidDataSource",
		events : {
			depose : "close"
		},
		fields : {
			driverClassName : "com.mysql.jdbc.Driver",
			url             : {java :"$config.get('db.log.url')"},
			username        : {java :"$config.get('db.log.username')"},
			password        : {java :"$config.get('db.log.password')"},
			initialSize     : 1,
			maxActive       : 50,
			testOnReturn    : true,
			// validationQueryTimeout : 5,
			validationQuery : "select 1"
		}
	},
	logNDao : {
		type : 'org.nutz.dao.impl.NutDao',
		args : [ {
			refer : "logDataSource"
		} ]
	},
	// MessageDispatcher dispatcher;
	dispatcher : {
		type : "com.yxy.core.msg.MessageDispatcher",
		args : [{
			java : "$config.get('game.threadsize')"
		}]
	}
	// NetServer netServer;
	netServer : {
		type : 'com.yxy.core.net.NetServer',
		args : [
		    {java : "$config.get('game.port')"},
		    {java : "$config.get('game.maxconn')"},
			{type : "com.yxy.core.net.codec.DemuxCodecFactory"}
				]
	}
	// ClientMsgManager gsManager;
	msgHandler : {
		type : 'com.yxy.core.msg.MessageHandler',
		args : [
		    {refer : "dispatcher"},
		    ]
	}
	// SystemHolder systemContext;
	systemHolder : {
		type : 'com.yxy.core.SystemHolder',		
	}
	eventManager: {
		type : 'com.yxy.core.event.EventManager',
	}
	gameContext : {
		type : 'com.yxy.core.GameContext',		
		events : {
			create : 'init'
		},
		fields : {
			netServer		: {refer : 'netServer'},
			dispatcher		: {refer : 'dispatcher'},
			msgHandler		: {refer : 'msgHandler'},
			systemHolder	: {refer : 'systemHolder'},
			eventManager	: {refer : 'eventManager'},
			ioc				: {refer : '$Ioc'}
		}
	}
};
