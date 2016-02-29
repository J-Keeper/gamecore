package com.xgame.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DESUtil {

	private static final String CRYPT_KEY = "JLBCyMbVCrC09Sz7LSIauD/GSlJUiEshUikZWeOJy9E49ouU/Wu1J7nqPK8pz6p2lTtk6+WPpkpwUmfLTPEN5gZ5EQ==";

	private final static String DES = "DES";

	/**
	 * 密码解密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public final static String decrypt(String data) {
		try {
			return new String(decrypt(base642Byte(data.getBytes()),
					CRYPT_KEY.getBytes()));
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 密码加密
	 * 
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public final static String encrypt(String data) {
		try {
			return byte2Base64(encrypt(data.getBytes(), CRYPT_KEY.getBytes()));
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 数据解密(自定义密钥)
	 * 
	 * @param data
	 * @param key
	 *            密钥
	 * @throws Exception
	 */
	public final static String decrypt(String data, String key) {
		if (data == null || key == null)
			return null;
		try {
			return new String(decrypt(base642Byte(data.getBytes()),
					key.getBytes()));
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 数据加密(自定义密钥)
	 * 
	 * @param data
	 * @param key密钥
	 */
	public final static String encrypt(String data, String key) {
		if (data == null || key == null)
			return null;
		try {
			return byte2Base64(encrypt(data.getBytes(), key.getBytes()));
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 加密
	 * 
	 * @param src数据源
	 * @param key
	 *            密钥，长度必须是8的倍数
	 * @return 返回加密后的数据
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		// 从原始密匙数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(DES);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
		// 现在，获取数据并加密
		// 正式执行加密操作
		return cipher.doFinal(src);
	}

	/**
	 * 解密
	 * 
	 * @param src数据源
	 * @param key密钥
	 *            ，长度必须是8的倍数
	 * @return 返回解密后的原始数据
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		// 从原始密匙数据创建一个DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(DES);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
		// 现在，获取数据并解密
		// 正式执行解密操作
		return cipher.doFinal(src);
	}

	/**
	 * 二行制转十六进制
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	/**
	 * 十六进制转二进制
	 * 
	 * @param b
	 * @author frank
	 * @date 2013-5-28
	 */
	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	/**
	 * 字节数组转Base64位字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2Base64(byte[] b) {
		byte[] abyte0 = Base64Util.encode(b);
		return new String(abyte0);
	}

	/**
	 * Base64位字符串转字节数组
	 * 
	 * @param b
	 * @return
	 */
	public static byte[] base642Byte(byte[] b) {
		byte[] abyte0 = Base64Util.decode(b);
		return abyte0;
	}

	// /**
	// *
	// * @param args
	// * @author frank
	// * @date 2013-5-28
	// */
	// public static void main(String[] args) {
	//
	// String pwd =
	// "大家好!大家好!大家好!大家好!{\"r\":{\"p\":{\"lv\":1,\"pg\":20,\"upg\":100,\"gd\":0,\"sv\":120,\"vl\":0,\"cl\":2,\"ca\":2,\"pel\":30,\"pe\":30,\"pet\":0,\"ppl\":12,\"pp\":12,\"ppt\":0,\"mt\":0,\"name\":\"\",\"id\":12},\"pc\":[{\"cid\":10000046,\"l\":1,\"es\":0,\"e\":0,\"el\":100,\"sid\":0,\"h\":175,\"a\":0,\"d\":41,\"i\":233,\"ah\":0,\"aa\":0,\"ad\":0,\"ai\":0,\"p\":409,\"t\":\"\",\"sit\":1,\"fos\":\"\",\"bl\":0,\"pl\":50,\"bs\":2,\"id\":520}],\"pcs\":[],\"ps\":[{\"cid\":10000046,\"l\":1,\"e\":0,\"sid\":12000022,\"h\":0,\"a\":7,\"d\":0,\"i\":0,\"p\":192,\"psid\":443,\"tj\":1,\"pos\":1}],\"psc\":[],\"pst\":null,\"pm\":null,\"r\":{\"rtf\":5,\"tp\":10,\"mp\":300,\"rtc\":0,\"tcd\":0,\"hcd\":0,\"mcd\":0,\"hp\":100},\"pt\":\"\",\"pg\":[{\"a\":855,\"gid\":13300019},{\"a\":840,\"gid\":13300010},{\"a\":3,\"gid\":13300002}],\"pe\":[]},\"s\":0}{\"r\":{\"p\":{\"lv\":1,\"pg\":20,\"upg\":100,\"gd\":0,\"sv\":120,\"vl\":0,\"cl\":2,\"ca\":2,\"pel\":30,\"pe\":30,\"pet\":0,\"ppl\":12,\"pp\":12,\"ppt\":0,\"mt\":0,\"name\":\"\",\"id\":12},\"pc\":[{\"cid\":10000046,\"l\":1,\"es\":0,\"e\":0,\"el\":100,\"sid\":0,\"h\":175,\"a\":0,\"d\":41,\"i\":233,\"ah\":0,\"aa\":0,\"ad\":0,\"ai\":0,\"p\":409,\"t\":\"\",\"sit\":1,\"fos\":\"\",\"bl\":0,\"pl\":50,\"bs\":2,\"id\":520}],\"pcs\":[],\"ps\":[{\"cid\":10000046,\"l\":1,\"e\":0,\"sid\":12000022,\"h\":0,\"a\":7,\"d\":0,\"i\":0,\"p\":192,\"psid\":443,\"tj\":1,\"pos\":1}],\"psc\":[],\"pst\":null,\"pm\":null,\"r\":{\"rtf\":5,\"tp\":10,\"mp\":300,\"rtc\":0,\"tcd\":0,\"hcd\":0,\"mcd\":0,\"hp\":100},\"pt\":\"\",\"pg\":[{\"a\":855,\"gid\":13300019},{\"a\":840,\"gid\":13300010},{\"a\":3,\"gid\":13300002}],\"pe\":[]},\"s\":0}{\"r\":{\"p\":{\"lv\":1,\"pg\":20,\"upg\":100,\"gd\":0,\"sv\":120,\"vl\":0,\"cl\":2,\"ca\":2,\"pel\":30,\"pe\":30,\"pet\":0,\"ppl\":12,\"pp\":12,\"ppt\":0,\"mt\":0,\"name\":\"\",\"id\":12},\"pc\":[{\"cid\":10000046,\"l\":1,\"es\":0,\"e\":0,\"el\":100,\"sid\":0,\"h\":175,\"a\":0,\"d\":41,\"i\":233,\"ah\":0,\"aa\":0,\"ad\":0,\"ai\":0,\"p\":409,\"t\":\"\",\"sit\":1,\"fos\":\"\",\"bl\":0,\"pl\":50,\"bs\":2,\"id\":520}],\"pcs\":[],\"ps\":[{\"cid\":10000046,\"l\":1,\"e\":0,\"sid\":12000022,\"h\":0,\"a\":7,\"d\":0,\"i\":0,\"p\":192,\"psid\":443,\"tj\":1,\"pos\":1}],\"psc\":[],\"pst\":null,\"pm\":null,\"r\":{\"rtf\":5,\"tp\":10,\"mp\":300,\"rtc\":0,\"tcd\":0,\"hcd\":0,\"mcd\":0,\"hp\":100},\"pt\":\"\",\"pg\":[{\"a\":855,\"gid\":13300019},{\"a\":840,\"gid\":13300010},{\"a\":3,\"gid\":13300002}],\"pe\":[]},\"s\":0}{\"r\":{\"p\":{\"lv\":1,\"pg\":20,\"upg\":100,\"gd\":0,\"sv\":120,\"vl\":0,\"cl\":2,\"ca\":2,\"pel\":30,\"pe\":30,\"pet\":0,\"ppl\":12,\"pp\":12,\"ppt\":0,\"mt\":0,\"name\":\"\",\"id\":12},\"pc\":[{\"cid\":10000046,\"l\":1,\"es\":0,\"e\":0,\"el\":100,\"sid\":0,\"h\":175,\"a\":0,\"d\":41,\"i\":233,\"ah\":0,\"aa\":0,\"ad\":0,\"ai\":0,\"p\":409,\"t\":\"\",\"sit\":1,\"fos\":\"\",\"bl\":0,\"pl\":50,\"bs\":2,\"id\":520}],\"pcs\":[],\"ps\":[{\"cid\":10000046,\"l\":1,\"e\":0,\"sid\":12000022,\"h\":0,\"a\":7,\"d\":0,\"i\":0,\"p\":192,\"psid\":443,\"tj\":1,\"pos\":1}],\"psc\":[],\"pst\":null,\"pm\":null,\"r\":{\"rtf\":5,\"tp\":10,\"mp\":300,\"rtc\":0,\"tcd\":0,\"hcd\":0,\"mcd\":0,\"hp\":100},\"pt\":\"\",\"pg\":[{\"a\":855,\"gid\":13300019},{\"a\":840,\"gid\":13300010},{\"a\":3,\"gid\":13300002}],\"pe\":[]},\"s\":0}{\"r\":{\"p\":{\"lv\":1,\"pg\":20,\"upg\":100,\"gd\":0,\"sv\":120,\"vl\":0,\"cl\":2,\"ca\":2,\"pel\":30,\"pe\":30,\"pet\":0,\"ppl\":12,\"pp\":12,\"ppt\":0,\"mt\":0,\"name\":\"\",\"id\":12},\"pc\":[{\"cid\":10000046,\"l\":1,\"es\":0,\"e\":0,\"el\":100,\"sid\":0,\"h\":175,\"a\":0,\"d\":41,\"i\":233,\"ah\":0,\"aa\":0,\"ad\":0,\"ai\":0,\"p\":409,\"t\":\"\",\"sit\":1,\"fos\":\"\",\"bl\":0,\"pl\":50,\"bs\":2,\"id\":520}],\"pcs\":[],\"ps\":[{\"cid\":10000046,\"l\":1,\"e\":0,\"sid\":12000022,\"h\":0,\"a\":7,\"d\":0,\"i\":0,\"p\":192,\"psid\":443,\"tj\":1,\"pos\":1}],\"psc\":[],\"pst\":null,\"pm\":null,\"r\":{\"rtf\":5,\"tp\":10,\"mp\":300,\"rtc\":0,\"tcd\":0,\"hcd\":0,\"mcd\":0,\"hp\":100},\"pt\":\"\",\"pg\":[{\"a\":855,\"gid\":13300019},{\"a\":840,\"gid\":13300010},{\"a\":3,\"gid\":13300002}],\"pe\":[]},\"s\":0}{\"r\":{\"p\":{\"lv\":1,\"pg\":20,\"upg\":100,\"gd\":0,\"sv\":120,\"vl\":0,\"cl\":2,\"ca\":2,\"pel\":30,\"pe\":30,\"pet\":0,\"ppl\":12,\"pp\":12,\"ppt\":0,\"mt\":0,\"name\":\"\",\"id\":12},\"pc\":[{\"cid\":10000046,\"l\":1,\"es\":0,\"e\":0,\"el\":100,\"sid\":0,\"h\":175,\"a\":0,\"d\":41,\"i\":233,\"ah\":0,\"aa\":0,\"ad\":0,\"ai\":0,\"p\":409,\"t\":\"\",\"sit\":1,\"fos\":\"\",\"bl\":0,\"pl\":50,\"bs\":2,\"id\":520}],\"pcs\":[],\"ps\":[{\"cid\":10000046,\"l\":1,\"e\":0,\"sid\":12000022,\"h\":0,\"a\":7,\"d\":0,\"i\":0,\"p\":192,\"psid\":443,\"tj\":1,\"pos\":1}],\"psc\":[],\"pst\":null,\"pm\":null,\"r\":{\"rtf\":5,\"tp\":10,\"mp\":300,\"rtc\":0,\"tcd\":0,\"hcd\":0,\"mcd\":0,\"hp\":100},\"pt\":\"\",\"pg\":[{\"a\":855,\"gid\":13300019},{\"a\":840,\"gid\":13300010},{\"a\":3,\"gid\":13300002}],\"pe\":[]},\"s\":0}{\"r\":{\"p\":{\"lv\":1,\"pg\":20,\"upg\":100,\"gd\":0,\"sv\":120,\"vl\":0,\"cl\":2,\"ca\":2,\"pel\":30,\"pe\":30,\"pet\":0,\"ppl\":12,\"pp\":12,\"ppt\":0,\"mt\":0,\"name\":\"\",\"id\":12},\"pc\":[{\"cid\":10000046,\"l\":1,\"es\":0,\"e\":0,\"el\":100,\"sid\":0,\"h\":175,\"a\":0,\"d\":41,\"i\":233,\"ah\":0,\"aa\":0,\"ad\":0,\"ai\":0,\"p\":409,\"t\":\"\",\"sit\":1,\"fos\":\"\",\"bl\":0,\"pl\":50,\"bs\":2,\"id\":520}],\"pcs\":[],\"ps\":[{\"cid\":10000046,\"l\":1,\"e\":0,\"sid\":12000022,\"h\":0,\"a\":7,\"d\":0,\"i\":0,\"p\":192,\"psid\":443,\"tj\":1,\"pos\":1}],\"psc\":[],\"pst\":null,\"pm\":null,\"r\":{\"rtf\":5,\"tp\":10,\"mp\":300,\"rtc\":0,\"tcd\":0,\"hcd\":0,\"mcd\":0,\"hp\":100},\"pt\":\"\",\"pg\":[{\"a\":855,\"gid\":13300019},{\"a\":840,\"gid\":13300010},{\"a\":3,\"gid\":13300002}],\"pe\":[]},\"s\":0}{\"r\":{\"p\":{\"lv\":1,\"pg\":20,\"upg\":100,\"gd\":0,\"sv\":120,\"vl\":0,\"cl\":2,\"ca\":2,\"pel\":30,\"pe\":30,\"pet\":0,\"ppl\":12,\"pp\":12,\"ppt\":0,\"mt\":0,\"name\":\"\",\"id\":12},\"pc\":[{\"cid\":10000046,\"l\":1,\"es\":0,\"e\":0,\"el\":100,\"sid\":0,\"h\":175,\"a\":0,\"d\":41,\"i\":233,\"ah\":0,\"aa\":0,\"ad\":0,\"ai\":0,\"p\":409,\"t\":\"\",\"sit\":1,\"fos\":\"\",\"bl\":0,\"pl\":50,\"bs\":2,\"id\":520}],\"pcs\":[],\"ps\":[{\"cid\":10000046,\"l\":1,\"e\":0,\"sid\":12000022,\"h\":0,\"a\":7,\"d\":0,\"i\":0,\"p\":192,\"psid\":443,\"tj\":1,\"pos\":1}],\"psc\":[],\"pst\":null,\"pm\":null,\"r\":{\"rtf\":5,\"tp\":10,\"mp\":300,\"rtc\":0,\"tcd\":0,\"hcd\":0,\"mcd\":0,\"hp\":100},\"pt\":\"\",\"pg\":[{\"a\":855,\"gid\":13300019},{\"a\":840,\"gid\":13300010},{\"a\":3,\"gid\":13300002}],\"pe\":[]},\"s\":0}{\"r\":{\"p\":{\"lv\":1,\"pg\":20,\"upg\":100,\"gd\":0,\"sv\":120,\"vl\":0,\"cl\":2,\"ca\":2,\"pel\":30,\"pe\":30,\"pet\":0,\"ppl\":12,\"pp\":12,\"ppt\":0,\"mt\":0,\"name\":\"\",\"id\":12},\"pc\":[{\"cid\":10000046,\"l\":1,\"es\":0,\"e\":0,\"el\":100,\"sid\":0,\"h\":175,\"a\":0,\"d\":41,\"i\":233,\"ah\":0,\"aa\":0,\"ad\":0,\"ai\":0,\"p\":409,\"t\":\"\",\"sit\":1,\"fos\":\"\",\"bl\":0,\"pl\":50,\"bs\":2,\"id\":520}],\"pcs\":[],\"ps\":[{\"cid\":10000046,\"l\":1,\"e\":0,\"sid\":12000022,\"h\":0,\"a\":7,\"d\":0,\"i\":0,\"p\":192,\"psid\":443,\"tj\":1,\"pos\":1}],\"psc\":[],\"pst\":null,\"pm\":null,\"r\":{\"rtf\":5,\"tp\":10,\"mp\":300,\"rtc\":0,\"tcd\":0,\"hcd\":0,\"mcd\":0,\"hp\":100},\"pt\":\"\",\"pg\":[{\"a\":855,\"gid\":13300019},{\"a\":840,\"gid\":13300010},{\"a\":3,\"gid\":13300002}],\"pe\":[]},\"s\":0}{\"r\":{\"p\":{\"lv\":1,\"pg\":20,\"upg\":100,\"gd\":0,\"sv\":120,\"vl\":0,\"cl\":2,\"ca\":2,\"pel\":30,\"pe\":30,\"pet\":0,\"ppl\":12,\"pp\":12,\"ppt\":0,\"mt\":0,\"name\":\"\",\"id\":12},\"pc\":[{\"cid\":10000046,\"l\":1,\"es\":0,\"e\":0,\"el\":100,\"sid\":0,\"h\":175,\"a\":0,\"d\":41,\"i\":233,\"ah\":0,\"aa\":0,\"ad\":0,\"ai\":0,\"p\":409,\"t\":\"\",\"sit\":1,\"fos\":\"\",\"bl\":0,\"pl\":50,\"bs\":2,\"id\":520}],\"pcs\":[],\"ps\":[{\"cid\":10000046,\"l\":1,\"e\":0,\"sid\":12000022,\"h\":0,\"a\":7,\"d\":0,\"i\":0,\"p\":192,\"psid\":443,\"tj\":1,\"pos\":1}],\"psc\":[],\"pst\":null,\"pm\":null,\"r\":{\"rtf\":5,\"tp\":10,\"mp\":300,\"rtc\":0,\"tcd\":0,\"hcd\":0,\"mcd\":0,\"hp\":100},\"pt\":\"\",\"pg\":[{\"a\":855,\"gid\":13300019},{\"a\":840,\"gid\":13300010},{\"a\":3,\"gid\":13300002}],\"pe\":[]},\"s\":0}{\"r\":{\"p\":{\"lv\":1,\"pg\":20,\"upg\":100,\"gd\":0,\"sv\":120,\"vl\":0,\"cl\":2,\"ca\":2,\"pel\":30,\"pe\":30,\"pet\":0,\"ppl\":12,\"pp\":12,\"ppt\":0,\"mt\":0,\"name\":\"\",\"id\":12},\"pc\":[{\"cid\":10000046,\"l\":1,\"es\":0,\"e\":0,\"el\":100,\"sid\":0,\"h\":175,\"a\":0,\"d\":41,\"i\":233,\"ah\":0,\"aa\":0,\"ad\":0,\"ai\":0,\"p\":409,\"t\":\"\",\"sit\":1,\"fos\":\"\",\"bl\":0,\"pl\":50,\"bs\":2,\"id\":520}],\"pcs\":[],\"ps\":[{\"cid\":10000046,\"l\":1,\"e\":0,\"sid\":12000022,\"h\":0,\"a\":7,\"d\":0,\"i\":0,\"p\":192,\"psid\":443,\"tj\":1,\"pos\":1}],\"psc\":[],\"pst\":null,\"pm\":null,\"r\":{\"rtf\":5,\"tp\":10,\"mp\":300,\"rtc\":0,\"tcd\":0,\"hcd\":0,\"mcd\":0,\"hp\":100},\"pt\":\"\",\"pg\":[{\"a\":855,\"gid\":13300019},{\"a\":840,\"gid\":13300010},{\"a\":3,\"gid\":13300002}],\"pe\":[]},\"s\":0}{\"r\":{\"p\":{\"lv\":1,\"pg\":20,\"upg\":100,\"gd\":0,\"sv\":120,\"vl\":0,\"cl\":2,\"ca\":2,\"pel\":30,\"pe\":30,\"pet\":0,\"ppl\":12,\"pp\":12,\"ppt\":0,\"mt\":0,\"name\":\"\",\"id\":12},\"pc\":[{\"cid\":10000046,\"l\":1,\"es\":0,\"e\":0,\"el\":100,\"sid\":0,\"h\":175,\"a\":0,\"d\":41,\"i\":233,\"ah\":0,\"aa\":0,\"ad\":0,\"ai\":0,\"p\":409,\"t\":\"\",\"sit\":1,\"fos\":\"\",\"bl\":0,\"pl\":50,\"bs\":2,\"id\":520}],\"pcs\":[],\"ps\":[{\"cid\":10000046,\"l\":1,\"e\":0,\"sid\":12000022,\"h\":0,\"a\":7,\"d\":0,\"i\":0,\"p\":192,\"psid\":443,\"tj\":1,\"pos\":1}],\"psc\":[],\"pst\":null,\"pm\":null,\"r\":{\"rtf\":5,\"tp\":10,\"mp\":300,\"rtc\":0,\"tcd\":0,\"hcd\":0,\"mcd\":0,\"hp\":100},\"pt\":\"\",\"pg\":[{\"a\":855,\"gid\":13300019},{\"a\":840,\"gid\":13300010},{\"a\":3,\"gid\":13300002}],\"pe\":[]},\"s\":0}{\"r\":{\"p\":{\"lv\":1,\"pg\":20,\"upg\":100,\"gd\":0,\"sv\":120,\"vl\":0,\"cl\":2,\"ca\":2,\"pel\":30,\"pe\":30,\"pet\":0,\"ppl\":12,\"pp\":12,\"ppt\":0,\"mt\":0,\"name\":\"\",\"id\":12},\"pc\":[{\"cid\":10000046,\"l\":1,\"es\":0,\"e\":0,\"el\":100,\"sid\":0,\"h\":175,\"a\":0,\"d\":41,\"i\":233,\"ah\":0,\"aa\":0,\"ad\":0,\"ai\":0,\"p\":409,\"t\":\"\",\"sit\":1,\"fos\":\"\",\"bl\":0,\"pl\":50,\"bs\":2,\"id\":520}],\"pcs\":[],\"ps\":[{\"cid\":10000046,\"l\":1,\"e\":0,\"sid\":12000022,\"h\":0,\"a\":7,\"d\":0,\"i\":0,\"p\":192,\"psid\":443,\"tj\":1,\"pos\":1}],\"psc\":[],\"pst\":null,\"pm\":null,\"r\":{\"rtf\":5,\"tp\":10,\"mp\":300,\"rtc\":0,\"tcd\":0,\"hcd\":0,\"mcd\":0,\"hp\":100},\"pt\":\"\",\"pg\":[{\"a\":855,\"gid\":13300019},{\"a\":840,\"gid\":13300010},{\"a\":3,\"gid\":13300002}],\"pe\":[]},\"s\":0}{\"r\":{\"p\":{\"lv\":1,\"pg\":20,\"upg\":100,\"gd\":0,\"sv\":120,\"vl\":0,\"cl\":2,\"ca\":2,\"pel\":30,\"pe\":30,\"pet\":0,\"ppl\":12,\"pp\":12,\"ppt\":0,\"mt\":0,\"name\":\"\",\"id\":12},\"pc\":[{\"cid\":10000046,\"l\":1,\"es\":0,\"e\":0,\"el\":100,\"sid\":0,\"h\":175,\"a\":0,\"d\":41,\"i\":233,\"ah\":0,\"aa\":0,\"ad\":0,\"ai\":0,\"p\":409,\"t\":\"\",\"sit\":1,\"fos\":\"\",\"bl\":0,\"pl\":50,\"bs\":2,\"id\":520}],\"pcs\":[],\"ps\":[{\"cid\":10000046,\"l\":1,\"e\":0,\"sid\":12000022,\"h\":0,\"a\":7,\"d\":0,\"i\":0,\"p\":192,\"psid\":443,\"tj\":1,\"pos\":1}],\"psc\":[],\"pst\":null,\"pm\":null,\"r\":{\"rtf\":5,\"tp\":10,\"mp\":300,\"rtc\":0,\"tcd\":0,\"hcd\":0,\"mcd\":0,\"hp\":100},\"pt\":\"\",\"pg\":[{\"a\":855,\"gid\":13300019},{\"a\":840,\"gid\":13300010},{\"a\":3,\"gid\":13300002}],\"pe\":[]},\"s\":0}{\"r\":{\"p\":{\"lv\":1,\"pg\":20,\"upg\":100,\"gd\":0,\"sv\":120,\"vl\":0,\"cl\":2,\"ca\":2,\"pel\":30,\"pe\":30,\"pet\":0,\"ppl\":12,\"pp\":12,\"ppt\":0,\"mt\":0,\"name\":\"\",\"id\":12},\"pc\":[{\"cid\":10000046,\"l\":1,\"es\":0,\"e\":0,\"el\":100,\"sid\":0,\"h\":175,\"a\":0,\"d\":41,\"i\":233,\"ah\":0,\"aa\":0,\"ad\":0,\"ai\":0,\"p\":409,\"t\":\"\",\"sit\":1,\"fos\":\"\",\"bl\":0,\"pl\":50,\"bs\":2,\"id\":520}],\"pcs\":[],\"ps\":[{\"cid\":10000046,\"l\":1,\"e\":0,\"sid\":12000022,\"h\":0,\"a\":7,\"d\":0,\"i\":0,\"p\":192,\"psid\":443,\"tj\":1,\"pos\":1}],\"psc\":[],\"pst\":null,\"pm\":null,\"r\":{\"rtf\":5,\"tp\":10,\"mp\":300,\"rtc\":0,\"tcd\":0,\"hcd\":0,\"mcd\":0,\"hp\":100},\"pt\":\"\",\"pg\":[{\"a\":855,\"gid\":13300019},{\"a\":840,\"gid\":13300010},{\"a\":3,\"gid\":13300002}],\"pe\":[]},\"s\":0}{\"r\":{\"p\":{\"lv\":1,\"pg\":20,\"upg\":100,\"gd\":0,\"sv\":120,\"vl\":0,\"cl\":2,\"ca\":2,\"pel\":30,\"pe\":30,\"pet\":0,\"ppl\":12,\"pp\":12,\"ppt\":0,\"mt\":0,\"name\":\"\",\"id\":12},\"pc\":[{\"cid\":10000046,\"l\":1,\"es\":0,\"e\":0,\"el\":100,\"sid\":0,\"h\":175,\"a\":0,\"d\":41,\"i\":233,\"ah\":0,\"aa\":0,\"ad\":0,\"ai\":0,\"p\":409,\"t\":\"\",\"sit\":1,\"fos\":\"\",\"bl\":0,\"pl\":50,\"bs\":2,\"id\":520}],\"pcs\":[],\"ps\":[{\"cid\":10000046,\"l\":1,\"e\":0,\"sid\":12000022,\"h\":0,\"a\":7,\"d\":0,\"i\":0,\"p\":192,\"psid\":443,\"tj\":1,\"pos\":1}],\"psc\":[],\"pst\":null,\"pm\":null,\"r\":{\"rtf\":5,\"tp\":10,\"mp\":300,\"rtc\":0,\"tcd\":0,\"hcd\":0,\"mcd\":0,\"hp\":100},\"pt\":\"\",\"pg\":[{\"a\":855,\"gid\":13300019},{\"a\":840,\"gid\":13300010},{\"a\":3,\"gid\":13300002}],\"pe\":[]},\"s\":0}{\"r\":{\"p\":{\"lv\":1,\"pg\":20,\"upg\":100,\"gd\":0,\"sv\":120,\"vl\":0,\"cl\":2,\"ca\":2,\"pel\":30,\"pe\":30,\"pet\":0,\"ppl\":12,\"pp\":12,\"ppt\":0,\"mt\":0,\"name\":\"\",\"id\":12},\"pc\":[{\"cid\":10000046,\"l\":1,\"es\":0,\"e\":0,\"el\":100,\"sid\":0,\"h\":175,\"a\":0,\"d\":41,\"i\":233,\"ah\":0,\"aa\":0,\"ad\":0,\"ai\":0,\"p\":409,\"t\":\"\",\"sit\":1,\"fos\":\"\",\"bl\":0,\"pl\":50,\"bs\":2,\"id\":520}],\"pcs\":[],\"ps\":[{\"cid\":10000046,\"l\":1,\"e\":0,\"sid\":12000022,\"h\":0,\"a\":7,\"d\":0,\"i\":0,\"p\":192,\"psid\":443,\"tj\":1,\"pos\":1}],\"psc\":[],\"pst\":null,\"pm\":null,\"r\":{\"rtf\":5,\"tp\":10,\"mp\":300,\"rtc\":0,\"tcd\":0,\"hcd\":0,\"mcd\":0,\"hp\":100},\"pt\":\"\",\"pg\":[{\"a\":855,\"gid\":13300019},{\"a\":840,\"gid\":13300010},{\"a\":3,\"gid\":13300002}],\"pe\":[]},\"s\":0}{\"r\":{\"p\":{\"lv\":1,\"pg\":20,\"upg\":100,\"gd\":0,\"sv\":120,\"vl\":0,\"cl\":2,\"ca\":2,\"pel\":30,\"pe\":30,\"pet\":0,\"ppl\":12,\"pp\":12,\"ppt\":0,\"mt\":0,\"name\":\"\",\"id\":12},\"pc\":[{\"cid\":10000046,\"l\":1,\"es\":0,\"e\":0,\"el\":100,\"sid\":0,\"h\":175,\"a\":0,\"d\":41,\"i\":233,\"ah\":0,\"aa\":0,\"ad\":0,\"ai\":0,\"p\":409,\"t\":\"\",\"sit\":1,\"fos\":\"\",\"bl\":0,\"pl\":50,\"bs\":2,\"id\":520}],\"pcs\":[],\"ps\":[{\"cid\":10000046,\"l\":1,\"e\":0,\"sid\":12000022,\"h\":0,\"a\":7,\"d\":0,\"i\":0,\"p\":192,\"psid\":443,\"tj\":1,\"pos\":1}],\"psc\":[],\"pst\":null,\"pm\":null,\"r\":{\"rtf\":5,\"tp\":10,\"mp\":300,\"rtc\":0,\"tcd\":0,\"hcd\":0,\"mcd\":0,\"hp\":100},\"pt\":\"\",\"pg\":[{\"a\":855,\"gid\":13300019},{\"a\":840,\"gid\":13300010},{\"a\":3,\"gid\":13300002}],\"pe\":[]},\"s\":0}{\"r\":{\"p\":{\"lv\":1,\"pg\":20,\"upg\":100,\"gd\":0,\"sv\":120,\"vl\":0,\"cl\":2,\"ca\":2,\"pel\":30,\"pe\":30,\"pet\":0,\"ppl\":12,\"pp\":12,\"ppt\":0,\"mt\":0,\"name\":\"\",\"id\":12},\"pc\":[{\"cid\":10000046,\"l\":1,\"es\":0,\"e\":0,\"el\":100,\"sid\":0,\"h\":175,\"a\":0,\"d\":41,\"i\":233,\"ah\":0,\"aa\":0,\"ad\":0,\"ai\":0,\"p\":409,\"t\":\"\",\"sit\":1,\"fos\":\"\",\"bl\":0,\"pl\":50,\"bs\":2,\"id\":520}],\"pcs\":[],\"ps\":[{\"cid\":10000046,\"l\":1,\"e\":0,\"sid\":12000022,\"h\":0,\"a\":7,\"d\":0,\"i\":0,\"p\":192,\"psid\":443,\"tj\":1,\"pos\":1}],\"psc\":[],\"pst\":null,\"pm\":null,\"r\":{\"rtf\":5,\"tp\":10,\"mp\":300,\"rtc\":0,\"tcd\":0,\"hcd\":0,\"mcd\":0,\"hp\":100},\"pt\":\"\",\"pg\":[{\"a\":855,\"gid\":13300019},{\"a\":840,\"gid\":13300010},{\"a\":3,\"gid\":13300002}],\"pe\":[]},\"s\":0}{\"r\":{\"p\":{\"lv\":1,\"pg\":20,\"upg\":100,\"gd\":0,\"sv\":120,\"vl\":0,\"cl\":2,\"ca\":2,\"pel\":30,\"pe\":30,\"pet\":0,\"ppl\":12,\"pp\":12,\"ppt\":0,\"mt\":0,\"name\":\"\",\"id\":12},\"pc\":[{\"cid\":10000046,\"l\":1,\"es\":0,\"e\":0,\"el\":100,\"sid\":0,\"h\":175,\"a\":0,\"d\":41,\"i\":233,\"ah\":0,\"aa\":0,\"ad\":0,\"ai\":0,\"p\":409,\"t\":\"\",\"sit\":1,\"fos\":\"\",\"bl\":0,\"pl\":50,\"bs\":2,\"id\":520}],\"pcs\":[],\"ps\":[{\"cid\":10000046,\"l\":1,\"e\":0,\"sid\":12000022,\"h\":0,\"a\":7,\"d\":0,\"i\":0,\"p\":192,\"psid\":443,\"tj\":1,\"pos\":1}],\"psc\":[],\"pst\":null,\"pm\":null,\"r\":{\"rtf\":5,\"tp\":10,\"mp\":300,\"rtc\":0,\"tcd\":0,\"hcd\":0,\"mcd\":0,\"hp\":100},\"pt\":\"\",\"pg\":[{\"a\":855,\"gid\":13300019},{\"a\":840,\"gid\":13300010},{\"a\":3,\"gid\":13300002}],\"pe\":[]},\"s\":0}";
	// // String pwd =
	// //
	// "23.|$5l}xb+@f\"/^\"&(\"{~n(9n|7%.z.@p>4~nz+2ca2bj>s3<p:+>``*%<24n6j7<6n&t3_6~;=z\"dtnyhb{os4";
	// // &content=buchang&awards=85001:500&title=buchang
	// // String pwd1 =
	// //
	// "content=buchangs&title=buchangs&aid=2000224&awards=85001:2000;20041:20;20034:20;80043:5";
	// String pwd1 = "";
	// long time = System.currentTimeMillis();
	// String enc = encrypt(pwd1);
	// System.out.println(URLEncoder.encode(enc));
	// System.out.println(decrypt(enc));
	// System.out.println(System.currentTimeMillis() - time);
	// }

	/**
	 * 添加文件内容
	 * 
	 * @param file
	 * @param content
	 * @author Administrator
	 * @date 2009-12-21
	 */
	public static void apptendTextFile(String path, String content) {
		// 声明一个缓冲输出流
		BufferedWriter bw = null;
		// 声明一个控制台缓冲输入流,动态写入信息。
		BufferedReader br = null;
		try {
			File file = new File(path);
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			// 构造文件输出字符流,第二参数为true是向文件追加信息的意思
			if (!file.exists())
				file.createNewFile();
			FileWriter fw = new FileWriter(file, true);
			bw = new BufferedWriter(fw);
			// 从控制台输入信息
			bw.write(content + "\r\n");
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (bw != null) {
					bw.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
