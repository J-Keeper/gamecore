package com.yxy.core.msg.adp;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.session.IoSession;

import com.yxy.core.msg.Attribute;
import com.yxy.core.msg.Optional;
import com.yxy.core.msg.Required;
import com.yxy.core.net.codec.Message;

public class Adaptor implements IAdaptor {
	private Annotation[] requiredArr;
	private Class<?>[] paramTypes;
	private Field[] fields;
	private List<Class<?>> noAnnList;

	public void init(Method method) {
		this.noAnnList = new ArrayList<>();
		this.noAnnList.add(Message.class);
		this.noAnnList.add(IoSession.class);

		this.fields = Message.class.getFields();
		this.paramTypes = method.getParameterTypes();
		this.requiredArr = new Annotation[paramTypes.length];
		Annotation[][] aanns = method.getParameterAnnotations();
		for (int i = 0; i < aanns.length; i++) {
			Annotation[] ans = aanns[i];
			if (ans.length == 0) {
				Class<?> type = this.paramTypes[i];
				if (!noAnnList.contains(type)) {
					throw new IllegalArgumentException(new StringBuilder()
							.append(method.getName()).append("有参数缺少必要的注解")
							.toString());
				}
			} else {
				for (Annotation an : ans)
					if ((an instanceof Required)) {
						Required ran = (Required) an;
						this.requiredArr[i] = ran;
					} else if ((an instanceof Optional)) {
						Optional ran = (Optional) an;
						this.requiredArr[i] = ran;
					} else if ((an instanceof Attribute)) {
						Attribute ran = (Attribute) an;
						this.requiredArr[i] = ran;
					} else {
						throw new IllegalArgumentException(new StringBuilder()
								.append(method.getName()).append("参数注解类型错误")
								.toString());
					}
			}
		}
	}

	public Object[] adaptorMsg(Message msg) throws AdaptorException {
		Object[] parmas = new Object[requiredArr.length];
		for (int i = 0; i < requiredArr.length; i++) {
			Annotation an = requiredArr[i];
			Class<?> clazz = paramTypes[i];
			if (an == null) {
				parmas[i] = getNoAnnValue(clazz, msg);
				if (parmas[i] == null) {
					throw getException(null, null, msg, null);
				}
			} else if ((an instanceof Required)) {
				Required ran = (Required) an;
				parmas[i] = getRequiredObj(ran.value(), this.paramTypes[i], msg);
			} else if ((an instanceof Optional)) {
				Optional ran = (Optional) an;
				parmas[i] = getOptionalObj(ran.value(), this.paramTypes[i], msg);
			} else if ((an instanceof Attribute)) {
				Attribute ran = (Attribute) an;
				parmas[i] = getAttributeObj(ran.value(), msg);
			} else {
				throw getException(null, null, msg, null);
			}
		}

		return parmas;
	}

	private Object getAttributeObj(String paramName, Message msg) {
		Object attri = msg.getAttribute(paramName);
		return attri;
	}

	private Object getOptionalObj(String paramName, Class<?> clazz, Message msg)
			throws AdaptorException {
		return getValue(paramName, clazz, msg);
	}

	private Object getRequiredObj(String paramName, Class<?> clazz, Message msg)
			throws AdaptorException {
		Object obj = msg.getBody().get(paramName);
		if (obj == null) {
			throw getException(paramName, clazz, msg, null);
		}
		return getValue(paramName, clazz, msg);
	}

	private Object getValue(String paramName, Class<?> clazz, Message msg)
			throws AdaptorException {
		try {
			if ((clazz == Integer.TYPE) || (clazz == Integer.class))
				return Integer.valueOf(msg.getBody().getIntValue(paramName));
			if ((clazz == Long.TYPE) || (clazz == Long.class))
				return Long.valueOf(msg.getBody().getLongValue(paramName));
			if ((clazz == Boolean.TYPE) || (clazz == Boolean.class))
				return Boolean
						.valueOf(msg.getBody().getBooleanValue(paramName));
			if ((clazz == Byte.TYPE) || (clazz == Byte.class))
				return Byte.valueOf(msg.getBody().getByteValue(paramName));
			if ((clazz == Double.TYPE) || (clazz == Double.class))
				return Double.valueOf(msg.getBody().getDoubleValue(paramName));
			if ((clazz == Short.TYPE) || (clazz == Short.class))
				return Short.valueOf(msg.getBody().getShortValue(paramName));
			if ((clazz == Float.TYPE) || (clazz == Float.class))
				return Float.valueOf(msg.getBody().getFloatValue(paramName));
			if (clazz == String.class) {
				return msg.getBody().getString(paramName);
			}
			throw new Exception();
		} catch (Exception e) {
			throw getException(paramName, clazz, msg, e);
		}
	}

	private AdaptorException getException(String paramName, Class<?> clazz,
			Message msg, Exception e) {
		StringBuilder sb = new StringBuilder();
		sb.append("消息:");
		sb.append(msg.getType());
		sb.append(" 解析参数错误.");
		if (paramName != null) {
			sb.append("参数名:");
			sb.append(paramName);
		}
		if (clazz != null) {
			sb.append(" 参数类型:");
			sb.append(clazz.getName());
		}
		return new AdaptorException(sb.toString(), e);
	}

	private Object getNoAnnValue(Class<?> clazz, Message msg)
			throws AdaptorException {
		try {
			Object obj = null;

			if (clazz == Message.class)
				obj = msg;
			else if (clazz == IoSession.class)
				obj = msg.getSession();
			else {
				for (int i = 0; i < this.noAnnList.size(); i++) {
					Class<?> type = noAnnList.get(i);
					if (type == clazz) {
						this.fields[i].setAccessible(true);
						obj = this.fields[i].get(msg);
						break;
					}
				}
			}
			if (obj == null) {
				throw new IllegalArgumentException();
			}
			return obj;
		} catch (SecurityException | IllegalArgumentException
				| IllegalAccessException e) {
			throw getException(null, clazz, msg, e);
		}
	}
}