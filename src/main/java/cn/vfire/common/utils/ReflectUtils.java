package cn.vfire.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.vfire.common.utils.enums.ClzTypeEnum;

public class ReflectUtils {

	// private final static Logger log =
	// Logger.getLogger(ReflectUtils.class.getName());

	/**
	 * 过滤
	 * 
	 * @author ChenGang
	 *
	 */
	public static interface Filter {

		/**
		 * 准许返回的结果
		 * 
		 * @param o
		 * @param oname
		 * @param oclass
		 * @return
		 */
		public Object result(Object o, String oname, Class<?> oclass);
	}

	private final static Logger log = LoggerFactory.getLogger(ReflectUtils.class);

	@SuppressWarnings("unchecked")
	public static <T> T newInstance(String className, Class<T> clz) {

		if (className == null || clz == null) {
			return null;
		}

		try {

			Object obj = Class.forName(className).newInstance();

			boolean flag = clz.isAssignableFrom(obj.getClass());

			if (flag) {
				return (T) obj;
			} else {
				return null;
			}

		} catch (Exception e) {
			throw new RuntimeException("反射创建对象发生异常。异常信息:" + e.getMessage(), e);
		}

	}

	/**
	 * 反射得到Class的属性，包括父类的所有属性。
	 * 
	 * @param objClass
	 * @param fieldName
	 * @return
	 */
	public static Field getField(Class<?> objClass, String fieldName) {

		if (objClass == null || fieldName == null || "".equals(fieldName)) {
			return null;
		}

		Field f = null;

		try {
			f = objClass.getDeclaredField(fieldName);
			return f;
		} catch (Exception e) {
			log.warn("反射获取Class的Field对象发生异常。Class:{} FieldName:{} \n\t异常信息:{}", objClass, fieldName, e.getMessage());
		}

		try {
			f = objClass.getSuperclass().getDeclaredField(fieldName);
			return f;
		} catch (Exception e) {
			log.warn("反射获取Class的Field对象发生异常。Class:{} FieldName:{} \n\t异常信息:{}", objClass, fieldName, e.getMessage());
		}

		return null;

	}

	/**
	 * 反射获取对象属性值。
	 * 
	 * @param obj
	 * @param fname
	 * @param resultClass
	 * @return
	 */
	public static Object getFieldValue(Object obj, String fname) {

		if (obj == null || fname == null || "".equals(fname)) {
			return null;
		}

		Field field = getField(obj.getClass(), fname);

		if (field != null) {
			return getFieldValue(obj, field, field.getType());
		}

		return null;

	}

	/**
	 * 反射获取对象属性值。包含父类属性
	 * 
	 * @param obj
	 * @param fname
	 * @param resultClass
	 * @return
	 */
	public static <T> T getFieldValue(Object obj, String fname, Class<T> resultClass) {

		if (obj == null || fname == null || "".equals(fname)) {
			return null;
		}

		Field field = getField(obj.getClass(), fname);

		return getFieldValue(obj, field, resultClass);
	}

	/**
	 * 反射获取对象属性值。
	 * 
	 * @param obj
	 * @param f
	 * @param resultClass
	 * @return 失败返回null
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getFieldValue(Object obj, Field f, Class<T> resultClass) {

		if (obj == null || f == null || resultClass == null) {
			return null;
		}

		if (resultClass.isAssignableFrom(f.getType()) == false) {
			throw new RuntimeException(String.format("Field映射的属性Class类型与泛型指定resultClass类型不一致。Field映射属性Class:%s resultClass:%s", f.getType(), resultClass));
		}

		String fname = f.getName();

		try {

			f.setAccessible(true);

			Object v = f.get(obj);

			if (v != null) {
				return (T) v;
			}

		} catch (Exception e) {
			log.warn("反射获取对象属性值发生异常。对象:{} 属性:{} 异常信息:{}", obj, fname, e.getMessage());
		}

		return null;

	}

	/**
	 * 获取反射的Method对象，只会扫描该类的public的方法，包含父类和接口的public方法。
	 * 
	 * @param obj
	 * @param methodName
	 * @param parameterTypes
	 * @return 没有找到则返回Null
	 */
	public static Method getMethod(Class<?> objClass, String methodName, Class<?>... parameterTypes) {

		if (objClass == null || methodName == null || "".equals(methodName)) {
			return null;
		}

		try {

			Method method = objClass.getMethod(methodName, parameterTypes);

			if (method != null) {
				return method;
			}

		} catch (Exception e) {
			log.warn("反射获取Class的方法反射对象Method时发生异常。Class:{} 方法:{} 异常信息:{}", objClass, methodName, e.getMessage());
		}

		return null;

	}

	/**
	 * 直接通过属性名称获取该属性对应的Getter方法的Method对象
	 * 
	 * @param objClass
	 * @param fieldName
	 * @param parameterTypes
	 * @return 不存在则返回null
	 */
	public static Method getMethodGetter(Class<?> objClass, String fieldName, Class<?>... parameterTypes) {

		if (objClass == null || fieldName == null || "".equals(fieldName)) {
			return null;
		}

		Method method = null;

		String getterMethod = null;

		getterMethod = toGetterOrSetter(fieldName, "get");

		method = getMethod(objClass, getterMethod, parameterTypes);

		if (method == null) {

			getterMethod = toGetterOrSetter(fieldName, "is");

			method = getMethod(objClass, getterMethod, parameterTypes);
		}

		return method;

	}

	/**
	 * 获取指定属性名称对应的Setter方法反射的Method对象
	 * 
	 * @param objClass
	 * @param fieldName
	 * @param parameterTypes
	 * @return 不存在则返回null
	 */
	public static Method getMethodSetter(Class<?> objClass, String fieldName, Class<?>... parameterTypes) {

		if (objClass == null || fieldName == null || "".equals(fieldName)) {
			return null;
		}

		Method method = null;

		String setterMethod = "set";

		setterMethod = toGetterOrSetter(fieldName, setterMethod);

		method = getMethod(objClass, setterMethod, parameterTypes);

		return method;

	}

	public static boolean isBaseType(Class<?> clz, String fieldName) {
		Field f = getField(clz, fieldName);
		if (f != null) {
			return isBaseType(f.getType());
		}
		return false;
	}

	public static boolean isCollectionType(Class<?> clz, String fieldName) {
		Field f = getField(clz, fieldName);
		if (f != null) {
			return isCollectionType(f.getType());
		}
		return false;
	}

	/**
	 * 判断是否为基础类型。 <br/>
	 * 注意：int、Integer同为基础类型
	 * 
	 * @param clz
	 * @return
	 */
	public static boolean isBaseType(Class<?> clz) {

		ClzTypeEnum clzTypeEnum = ClzTypeEnum.parseClzTypeEnum(clz);

		switch (clzTypeEnum) {
		case BYTE:
			return true;
		case CHAR:
			return true;
		case SHORT:
			return true;
		case INT:
			return true;
		case LONG:
			return true;
		case FLOAT:
			return true;
		case DOUBLE:
			return true;
		case BOOLEAN:
			return true;
		case STRING:
			return true;
		default:
			return false;
		}
	}

	/**
	 * 判断是否为容器类型。
	 * 
	 * @see cn.vfire.common.utils.enums.ClzTypeEnum
	 * @param clz
	 * @return
	 */
	public static boolean isCollectionType(Class<?> clz) {

		ClzTypeEnum clzTypeEnum = ClzTypeEnum.parseClzTypeEnum(clz);

		switch (clzTypeEnum) {
		case LIST:
			return true;
		case SET:
			return true;
		case COLLECTION:
			return true;
		default:
			return false;
		}
	}

	/**
	 * 对象的属性赋值.
	 * 
	 * @param obj
	 * @param f
	 * @param fvalue
	 */
	public static boolean setFieldValue(Object obj, Field f, Object fvalue) {

		if (obj == null || f == null || fvalue == null) {
			return false;
		}

		f.setAccessible(true);

		Class<?> valueClass = fvalue.getClass();
		Class<?> fieldClass = f.getType();

		String fname = f.getName();

		if (valueClass != null && !(fieldClass == valueClass || fieldClass.isAssignableFrom(valueClass))) {
			return false;
		}

		try {

			ClzTypeEnum clzType = ClzTypeEnum.parseClzTypeEnum(f.getType());

			switch (clzType) {
			case LIST: {
				Object fobj = f.get(obj);
				if (fobj instanceof List) {
					@SuppressWarnings("unchecked")
					List<Object> flist = (List<Object>) fobj;
					flist.add(fvalue);
				} else if (fobj == null) {
					List<Object> flist = new ArrayList<Object>();
					flist.add(fvalue);
					f.set(obj, flist);
				}
				break;
			}
			default:
				f.set(obj, fvalue);
				break;
			}

			return true;

		} catch (Exception e) {
			log.warn("反射注入对象属性值发生异常。对象:{} 属性:{} 注入值:{} \n\t异常信息:{}", obj, fname, String.valueOf(fvalue), e.getMessage());
		}

		return false;
	}

	/**
	 * 直接对对象的属性赋值，如果存在setter方法则通过setter方法注入，如果不存在则直接通过属性注入。。
	 * 
	 * @param obj
	 * @param fieldName
	 * @param fvalue
	 */
	public static void setFieldValue(Object obj, String fieldName, Object fvalue) {

		if (obj == null) {
			return;
		}

		Field f = getField(obj.getClass(), fieldName);

		if (fvalue instanceof String) {
			fvalue = valueOfBaseType(f, (String) fvalue);
		}

		boolean rs = setFieldValueBySetter(obj, f, fvalue);

		if (rs == false) {
			setFieldValue(obj, f, fvalue);
		}

	}

	public static void setFieldAll(Object fromObj, Object toObj) {
		setFieldAll(fromObj, toObj, null);
	}

	/**
	 * 将一个对象属性值一对一复制到另一个对象，直接走属性赋值，不经过setter。<br/>
	 * 
	 * @param fromObj
	 *            被复制对象
	 * @param toObj
	 *            被赋值的对象
	 * @param filter
	 *            过滤器接口result的三个参数，对toObj的属性要注入的值，toObj的属性名字，toObj的属性的类型。
	 *            若filter可为null。
	 */
	public static void setFieldAll(Object fromObj, Object toObj, Filter filter) {

		if (fromObj == null || toObj == null) {
			return;
		}

		Class<?> toObjClass = toObj.getClass();
		Class<?> fromObjClass = fromObj.getClass();

		Field[] toObjFields = toObjClass.getDeclaredFields();

		int toObjFieldsLength = toObjFields == null ? 0 : toObjFields.length;

		for (int i = 0; i < toObjFieldsLength; i++) {

			Field toField = toObjFields[i];

			Field fromField = getField(fromObjClass, toField.getName());

			Object fromFieldValue = getFieldValue(fromObj, fromField, fromField.getType());

			if (filter != null) {
				fromFieldValue = filter.result(fromFieldValue, toField.getName(), toField.getType());
			}

			setFieldValue(toObj, toField, fromFieldValue);

		}

	}

	/**
	 * 通过setter方法注入属性值
	 * 
	 * @param obj
	 * @param f
	 * @param fvalue
	 * @return
	 */
	public static boolean setFieldValueBySetter(Object obj, Field f, Object... fvalue) {

		if (obj == null || f == null) {
			return false;
		}

		Class<?> objClass = obj.getClass();

		String fname = f.getName();

		Class<?>[] parameterTypes = null;

		int fvalueLength = fvalue == null ? 0 : fvalue.length;

		if (fvalueLength > 0) {

			parameterTypes = new Class<?>[fvalueLength];

			for (int i = 0; i < fvalueLength; i++) {
				parameterTypes[i] = fvalue[i].getClass();
			}

		}

		Method getterMethod = getMethodSetter(objClass, fname, parameterTypes);

		if (getterMethod != null) {

			try {
				getterMethod.invoke(obj, fvalue);
				return true;
			} catch (Exception e) {
				log.warn("反射注入对象属性值发生异常。对象:{} setter:{} 注入值:{} \n\t异常信息:{}", obj, fname, String.valueOf(fvalue), e);
			}

		}

		return false;
	}

	/**
	 * 转化getter、setter方法名字。
	 * 
	 * @param fname
	 * @param getset
	 *            set、get、is
	 * @return 不会返回null
	 */
	public static String toGetterOrSetter(String fname, String getset) {

		if (fname == null || "".equals(fname)) {
			return null;
		}

		if (!"get".equals(getset) && !"set".equals(getset) && !"is".equals(getset)) {
			return null;
		}

		StringBuffer sbfname = new StringBuffer(fname);

		String getsetName = new StringBuffer(getset).append(String.valueOf(sbfname.charAt(0)).toUpperCase()).append(sbfname.substring(1)).toString();

		return getsetName;

	}

	/**
	 * 将集合转化成List集合，并根据指定的Filter准许的元素。
	 * 
	 * @param beanList
	 * @param filter
	 * @return 可能返回null或者size==0的List
	 */
	public static List<Map<String, Object>> toListByBeanList(Collection<?> beanList, Filter filter) {

		if (beanList != null) {

			Iterator<?> iterator = beanList.iterator();

			List<Map<String, Object>> rslist = new ArrayList<Map<String, Object>>(beanList.size());

			while (iterator.hasNext()) {

				Map<String, Object> emap = toMapByBean(iterator.next(), filter);

				if (emap != null) {

					rslist.add(emap);

				}

			}

			return rslist;

		}

		return null;

	}

	/**
	 * 数据转化，将Object对象转化为Map，并可以通过Filter进行过滤。
	 * 
	 * @param bean
	 * @param filter
	 * @return
	 */
	public static Map<String, Object> toMapByBean(Object bean, Filter filter) {

		if (bean == null) {
			return null;
		}

		Field[] fields = bean.getClass().getDeclaredFields();

		if (fields != null && fields.length > 0) {

			Map<String, Object> map = new HashMap<String, Object>(fields.length);

			Class<?> fleldClass = null;
			String name = null;
			Object value = null;

			for (int i = 0; i < fields.length; i++) {

				Field fleld = fields[i];

				if (fleld == null) {
					continue;
				}

				try {
					fleld.setAccessible(true);
					fleldClass = fleld.getType();
					name = fleld.getName();
					value = fleld.get(bean);
				} catch (Exception e) {
					log.warn("反射获取对象属性值发生异常。对象:{} 属性:{} \n\t异常信息:{}", bean, name, e.getMessage());
				}

				if (filter != null) {
					value = filter.result(value, name, fleldClass);
				}

				if (value == null) {
					continue;
				}

				if (isBaseType(fleldClass)) {
					map.put(name, value);
				} else if (isCollectionType(fleldClass)) {
					List<Map<String, Object>> elist = toListByBeanList((Collection<?>) value, filter);
					if (elist != null) {
						map.put(name, elist);
					}
				} else {
					Map<String, Object> emap = toMapByBean(value, filter);
					if (emap != null) {
						map.put(name, emap);
					}
				}
			}

			return map;

		}

		return null;

	}

	/**
	 * 值转化，将String类型数据转化为Field映射基础类型。 <br/>
	 * 例如：String:"123" 转化为Integer:123
	 * 
	 * @param f
	 * @param value
	 * @return
	 */
	public static Object valueOfBaseType(Field f, String value) {

		if (f == null || value == null) {
			return null;
		}

		ClzTypeEnum clzTypeEnum = ClzTypeEnum.parseClzTypeEnum(f.getType());

		switch (clzTypeEnum) {
		case BYTE:
			return Byte.valueOf(value);
		case CHAR:
			return Character.valueOf(value.charAt(0));
		case SHORT:
			return Short.valueOf(value);
		case INT:
			return Integer.valueOf(value);
		case LONG:
			return Long.valueOf(value);
		case FLOAT:
			return Float.valueOf(value);
		case DOUBLE:
			return Double.valueOf(value);
		case BOOLEAN:
			return Boolean.valueOf(value);
		case STRING:
			return value;
		default:
			return null;
		}

	}
}
