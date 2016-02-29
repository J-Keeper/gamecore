package com.xgame.util;

import java.io.UnsupportedEncodingException;

/**
 * Base64编码
 * 
 * @author frank
 */
public class Base64Util {

	private final static String UTF8 = "utf-8";

	protected static byte[] _encode_map = { (byte) 'A', (byte) 'B', (byte) 'C',
			(byte) 'D', (byte) 'E', (byte) 'F', (byte) 'G', (byte) 'H',
			(byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L', (byte) 'M',
			(byte) 'N', (byte) 'O', (byte) 'P', (byte) 'Q', (byte) 'R',
			(byte) 'S', (byte) 'T', (byte) 'U', (byte) 'V', (byte) 'W',
			(byte) 'X', (byte) 'Y', (byte) 'Z', (byte) 'a', (byte) 'b',
			(byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f', (byte) 'g',
			(byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k', (byte) 'l',
			(byte) 'm', (byte) 'n', (byte) 'o', (byte) 'p', (byte) 'q',
			(byte) 'r', (byte) 's', (byte) 't', (byte) 'u', (byte) 'v',
			(byte) 'w', (byte) 'x', (byte) 'y', (byte) 'z', (byte) '0',
			(byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5',
			(byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) '+',
			(byte) '/' };
	protected static byte _decode_map[] = new byte[128];
	static {
		for (int i = 0; i < _encode_map.length; i++) {
			_decode_map[_encode_map[i]] = (byte) i;
		}
	}

	/**
	 * This class isn't meant to be instantiated.
	 */
	private Base64Util() {

	}

	/**
	 * BASE64编码
	 * 
	 * @param s
	 * @return String
	 */
	public static String encryptBASE64(String s) {
		if (s != null) {
			byte abyte0[];
			try {
				abyte0 = s.getBytes(UTF8);
				abyte0 = encode(abyte0);
				s = new String(abyte0);
				return s;
			} catch (UnsupportedEncodingException excp) {
				excp.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * BASE64反编码
	 * 
	 * @param s
	 * @return String
	 */
	public static String decryptBASE64(String s) {
		try {
			if (s != null) {
				byte abyte0[] = s.getBytes();
				abyte0 = decode(abyte0);
				s = new String(abyte0, UTF8);
				abyte0 = null;
				return s;
			}
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * BASE64编码
	 * 
	 * @param s
	 * @return String
	 */
	public static byte[] enBASE64(byte[] bytes) {
		return encode(bytes);
	}

	/**
	 * BASE64反编码
	 * 
	 * @param bytes
	 * @return byte[]
	 */
	public static byte[] deBASE64(byte[] bytes) {
		return decode(bytes);
	}

	/**
	 * This method encodes the given byte[] using the Base64 encoding
	 * 
	 * @param data
	 *            the data to encode.
	 * @return the Base64 encoded <var>data</var>
	 */
	public final static byte[] encode(byte[] data) {
		if (data == null) {
			return (null);
		}
		/*
		 * Craete a buffer to hold the results
		 */
		byte dest[] = new byte[((data.length + 2) / 3) * 4];
		/*
		 * 3-byte to 4-byte conversion and 0-63 to ascii printable conversion
		 */
		int i, j;
		int data_len = data.length - 2;
		for (i = 0, j = 0; i < data_len; i += 3) {
			dest[j++] = _encode_map[(data[i] >>> 2) & 077];
			dest[j++] = _encode_map[(data[i + 1] >>> 4) & 017 | (data[i] << 4)
					& 077];
			dest[j++] = _encode_map[(data[i + 2] >>> 6) & 003
					| (data[i + 1] << 2) & 077];
			dest[j++] = _encode_map[data[i + 2] & 077];
		}
		if (i < data.length) {
			dest[j++] = _encode_map[(data[i] >>> 2) & 077];

			if (i < data.length - 1) {
				dest[j++] = _encode_map[(data[i + 1] >>> 4) & 017
						| (data[i] << 4) & 077];
				dest[j++] = _encode_map[(data[i + 1] << 2) & 077];
			} else {
				dest[j++] = _encode_map[(data[i] << 4) & 077];
			}
		}
		/*
		 * Pad with "=" characters
		 */
		for (; j < dest.length; j++) {
			dest[j] = (byte) '=';
		}
		return (dest);
	}

	/**
	 * This method decodes the given byte[] using the Base64 encoding
	 * 
	 * @param data
	 *            the Base64 encoded data to decode.
	 * @return the decoded <var>data</var>.
	 */
	public final static byte[] decode(byte[] data) {
		if (data == null)
			return (null);
		/*
		 * Remove the padding on the end
		 */
		int ending = data.length;
		if (ending < 1) {
			return (null);
		}
		while (data[ending - 1] == '=')
			ending--;
		/*
		 * Create a buffer to hold the results
		 */
		byte dest[] = new byte[ending - data.length / 4];
		/*
		 * ASCII printable to 0-63 conversion
		 */
		for (int i = 0; i < data.length; i++) {
			data[i] = _decode_map[data[i]];
		}
		/*
		 * 4-byte to 3-byte conversion
		 */
		int i, j;
		int dest_len = dest.length - 2;
		for (i = 0, j = 0; j < dest_len; i += 4, j += 3) {
			dest[j] = (byte) (((data[i] << 2) & 255) | ((data[i + 1] >>> 4) & 003));
			dest[j + 1] = (byte) (((data[i + 1] << 4) & 255) | ((data[i + 2] >>> 2) & 017));
			dest[j + 2] = (byte) (((data[i + 2] << 6) & 255) | (data[i + 3] & 077));
		}
		if (j < dest.length) {
			dest[j] = (byte) (((data[i] << 2) & 255) | ((data[i + 1] >>> 4) & 003));
		}
		j++;
		if (j < dest.length) {
			dest[j] = (byte) (((data[i + 1] << 4) & 255) | ((data[i + 2] >>> 2) & 017));
		}
		return (dest);
	}
}
