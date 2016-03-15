package cn.vfire.common.utils.enums;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public enum ClzTypeEnum {

	NULL, BYTE, BYTE_ARRAY, CHAR, CHAR_ARRAY, SHORT, SHORT_ARRAY, INT, INT_ARRAY, LONG, LONG_ARRAY, FLOAT, FLOAT_ARRAY, DOUBLE, DOUBLE_ARRAY, BOOLEAN, BOOLEAN_ARRAY, STRING, STRING_ARRAY, OBJECT, OBJECT_ARRAY, LIST, SET, COLLECTION, MAP;

	public static ClzTypeEnum parseClzTypeEnum(Class<?> clz) {

		if (clz == byte.class || clz == Byte.class) {
			return BYTE;
		} else if (clz == byte[].class || clz == Byte[].class) {
			return BYTE_ARRAY;
		} else if (clz == char.class || clz == Character.class) {
			return CHAR;
		} else if (clz == char[].class || clz == Character[].class) {
			return CHAR_ARRAY;
		} else if (clz == short.class || clz == Short.class) {
			return SHORT;
		} else if (clz == short[].class || clz == Short[].class) {
			return SHORT_ARRAY;
		} else if (clz == int.class || clz == Integer.class) {
			return INT;
		} else if (clz == int[].class || clz == Integer[].class) {
			return INT_ARRAY;
		} else if (clz == long.class || clz == Long.class) {
			return LONG;
		} else if (clz == long[].class || clz == Long[].class) {
			return LONG_ARRAY;
		} else if (clz == float.class || clz == Float.class) {
			return FLOAT;
		} else if (clz == float[].class || clz == Float[].class) {
			return FLOAT_ARRAY;
		} else if (clz == double.class || clz == Double.class) {
			return DOUBLE;
		} else if (clz == double[].class || clz == Double[].class) {
			return DOUBLE_ARRAY;
		} else if (clz == boolean.class || clz == Boolean.class) {
			return BOOLEAN;
		} else if (clz == boolean[].class || clz == Boolean[].class) {
			return BOOLEAN_ARRAY;
		} else if (clz == String.class) {
			return STRING;
		} else if (clz == String[].class) {
			return STRING_ARRAY;
		} else if (clz == Object.class) {
			return OBJECT;
		} else if (clz == Object[].class) {
			return OBJECT_ARRAY;
		} else if (clz == List.class || List.class.isAssignableFrom(clz)) {
			return LIST;
		} else if (clz == Set.class || Set.class.isAssignableFrom(clz)) {
			return SET;
		} else if (clz == Collection.class || Collection.class.isAssignableFrom(clz)) {
			return COLLECTION;
		} else if (clz == Map.class || Map.class.isAssignableFrom(clz)) {
			return MAP;
		}

		return NULL;
	}

}
