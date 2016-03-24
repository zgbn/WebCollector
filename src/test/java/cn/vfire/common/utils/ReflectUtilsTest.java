package cn.vfire.common.utils;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectUtilsTest {

	private final static Logger log = LoggerFactory.getLogger(ReflectUtilsTest.class);

	@Test
	public void testToListByBeanList() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetField() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetFieldValueObjectStringObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetFieldValueObjectFieldObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFieldValue() {
		fail("Not yet implemented");
	}

	@Test
	public void testValueOfBaseType() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsBaseType() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsCollectionType() {
		fail("Not yet implemented");
	}

	@Test
	public void testToMapByBean() {
		fail("Not yet implemented");
	}

	@Test
	public void testToGetterOrSetter() {

		String methodName = ReflectUtils.toGetterOrSetter("name", "get");

		log.info("testToGetterOrSetter:{}(...)", methodName);
	}

}
