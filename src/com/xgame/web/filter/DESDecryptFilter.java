package com.xgame.web.filter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;
import org.nutz.mvc.view.VoidView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xgame.util.DESUtil;

/**
 * 游戏参数自动解密过滤器,所有web请求都需要加密
 * 
 * @since 2015年3月25日 下午7:44:28
 */
public class DESDecryptFilter implements ActionFilter {
	private Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * @see org.nutz.mvc.ActionFilter#match(org.nutz.mvc.ActionContext)
	 */
	@Override
	public View match(ActionContext actionContext) {
		String uri = actionContext.getRequest().getRequestURI();
		HttpServletRequest req = actionContext.getRequest();
		try {
			String p = req.getParameter("param");
			String decParam = DESUtil.decrypt(p);
			String[] paramArray = decParam.split("&");
			Map<String, String[]> parms = new HashMap<>();
			for (int i = 0; i < paramArray.length; i++) {
				String[] paramArr = paramArray[i].split("=");
				if (paramArr.length == 2) {
					String key = paramArr[0];
					String value = paramArr[1];
					parms.put(key, new String[] { value });
				}
			}
			DESRequestWrapper wrapper = new DESRequestWrapper(req, parms);
			actionContext.setRequest(wrapper);
			log.info("请求: " + uri + " 参数: " + Json.toJson(parms));
			return null;
		} catch (Exception e) {
			log.info(
					"参数无法解密:"
							+ Json.toJson(req.getParameterMap(),
									JsonFormat.compact()), e);
			return new VoidView();
		}
	}
}
