package com.yxy.core.net.codec;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(Message.class);
	protected Header header;
	private JSONObject body;
	private boolean isInner;
	protected long sessionId;
	private transient IoSession session;

	public Message() {
		this.header = new Header();
		this.body = new JSONObject();
	}

	public Message(short type) {
		this();
		this.header.setType(type);
	}

	public Message(IoSession session) {
		this();
		this.session = session;
		this.sessionId = session.getId();
	}

	public void clear() {
		this.header.clear();
		this.body = new JSONObject();
	}

	public Header getHeader() {
		return this.header;
	}

	public void put(String key, Object value) {
		this.body.put(key, value);
	}

	public void putAll(Map<String, Object> m) {
		this.body.putAll(m);
	}

	protected byte[] encodeBody() {
		SerializeWriter out = new SerializeWriter();
		try {
			new JSONSerializer(out).write(this.body);
			return out.toBytes(null);
		} finally {
			out.close();
		}
	}

	protected void setBodyBytes(byte[] bt) {
		this.body = JSON.parseObject(new String(bt));
	}

	public JSONObject getBody() {
		return this.body;
	}

	public void setBody(JSONObject json) {
		if (json == null) {
			throw new IllegalArgumentException("message body is null!");
		}
		this.body = json;
	}

	public boolean isInner() {
		return this.isInner;
	}

	public void setInner(boolean isInner) {
		this.isInner = isInner;
	}

	public IoSession getSession() {
		return this.session;
	}

	public void setSession(IoSession session) {
		this.session = session;
		this.sessionId = session.getId();
	}

	public Object setAttribute(String key, Object obj) {
		return this.session.setAttribute(key, obj);
	}

	public Object getAttribute(String key) {
		if (this.session == null) {
			return null;
		}
		return this.session.getAttribute(key);
	}

	public Object removeAttribute(String key) {
		return this.session.removeAttribute(key);
	}

	public void sendBySession() {
		this.session.write(this);
	}

	public void setType(short type) {
		this.header.setType(type);
	}

	public short getType() {
		return this.header.getType();
	}

	public void setState(int state) {
		this.header.setState(state);
	}

	public int getState() {
		return this.header.getState();
	}

	public int getCId() {
		return this.header.getId();
	}

	public void setCId(int id) {
		this.header.setId(id);
	}

	public long getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}

	public int getLength() {
		return Header.SIZE + this.header.length;
	}

	public IoBuffer build() {
		byte[] body = encodeBody();
		this.header.setLength(body.length);
		IoBuffer msgBuf = BufUtil.getAutoExpandBuf(getLength());
		msgBuf.put(getHeader().encode());
		msgBuf.put(body);
		msgBuf.flip();
		return msgBuf;
	}

	public String toString() {
		return "[header=" + this.header + ", ssid=" + this.sessionId
				+ ", body=" + this.body.toJSONString() + "]";
	}

	public static class Header implements Serializable {
		private static final long serialVersionUID = 1L;
		private short type;
		private int length = 0;
		private int id;
		private byte version = 1;
		private int state = 200;

		public static final byte SIZE = calcBytes();

		private static byte calcBytes() {
			Field[] fs = Header.class.getDeclaredFields();
			int bits = 0;
			Field[] arrayOfField1 = fs;
			int j = fs.length;
			for (int i = 0; i < j; i++) {
				Field f = arrayOfField1[i];
				int mod = f.getModifiers();
				String type = f.getGenericType().toString().intern();
				if (!Modifier.isStatic(mod)) {
					if (type == "byte") {
						bits += 8;
					} else if (type == "short")
						bits += 16;
					else if (type == "int")
						bits += 32;
					else if (type == "long")
						bits += 64;
					else if (type == "char")
						bits += 16;
					else if (type == "float")
						bits += 32;
					else if (type == "double")
						bits += 64;
					else {
						Message.log.info("non primitive type occurred: "
								+ Modifier.toString(mod) + " " + type + " ["
								+ f.getName() + "]");
					}
				} else {
					Message.log.info("static filed occurred: "
							+ Modifier.toString(mod) + " " + type + " ["
							+ f.getName() + "]");
				}
			}
			bits /= 8;
			if ((bits > 127) || (bits < -128)) {
				Message.log.error("数值[" + bits + "]超过byte边界");
				return 0;
			}
			return (byte) bits;
		}

		public void decode(IoBuffer in) {
			this.type = in.getShort();
			this.length = in.getInt();
			this.id = in.getInt();
			this.version = in.get();
			this.state = in.getInt();
		}

		public IoBuffer encode() {
			IoBuffer out = BufUtil.getAutoExpandBuf(SIZE);
			out.putShort(this.type);
			out.putInt(this.length);
			out.putInt(this.id);
			out.put(this.version);
			out.putInt(this.state);
			out.flip();
			return out;
		}

		public int getId() {
			return this.id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getLength() {
			return this.length;
		}

		public void setLength(int length) {
			this.length = length;
		}

		public short getType() {
			return this.type;
		}

		public void setType(short type) {
			this.type = type;
		}

		public int getState() {
			return this.state;
		}

		public void setState(int state) {
			this.state = state;
		}

		public String getTypeHexString() {
			return "0x" + Integer.toHexString(this.type);
		}

		public byte getVersion() {
			return this.version;
		}

		public void setVersion(byte version) {
			this.version = version;
		}

		public boolean checkVersion() {
			return this.version == 1;
		}

		public void clear() {
			this.id = 0;
			this.length = 0;
			this.state = 200;
			this.type = 0;
			this.version = 1;
		}

		public String toString() {
			return "[type=" + this.type + ", state=" + this.state + ", length="
					+ this.length + "]";
		}
	}
}