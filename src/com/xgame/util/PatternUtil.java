package com.xgame.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nutz.lang.Strings;

/**
 * 正则表达式工具类
 * 
 * @author frank
 */
public class PatternUtil {
	/**
	 * 字符串是否匹配
	 * 
	 * @param regEx
	 * @param line
	 * @author frank
	 * @date 2009-8-27
	 */
	public static boolean isMatcher(String regEx, String line) {
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(line);
		return m.find();
	}

	/**
	 * 解析字符串
	 * 
	 * @param regEx
	 * @param line
	 * @author frank
	 * @date 2009-7-2
	 */
	public static String parseString(String regEx, String line) {
		String content = "";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(line);
		boolean rs = m.find();
		for (int i = 1; i <= m.groupCount(); i++) {
			if (rs)
				content = m.group(i);
		}
		return content;
	}

	/**
	 * 解析字符串
	 * 
	 * @param regEx
	 * @param line
	 * @param len
	 * @author frank
	 * @date 2009-7-3
	 */
	public static String[] parseString(String regEx, String line, int len) {
		String[] content = new String[len];
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(line);
		boolean rs = m.find();
		for (int i = 1; i <= m.groupCount(); i++) {
			if (rs) {
				String tmp = m.group(i);
				if (Strings.isBlank(tmp))
					tmp = "";
				content[i - 1] = tmp;
			}
		}
		return content;
	}

	/**
	 * 解析字符串
	 * 
	 * @param regEx
	 * @param line
	 * @return
	 * @author frank
	 * @date 2010-5-18
	 */
	public static List<String> parseStringList(String regEx, String line) {
		List<String> strList = new ArrayList<String>();
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(line);
		while (m.find()) {
			for (int i = 1; i <= m.groupCount(); i++) {
				strList.add(m.group(i));
			}
		}
		return strList;
	}

}
