package cn.vfire.common.utils;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

import lombok.Getter;
import lombok.Setter;

public class SerializeUtilsTest {

	public class Per implements Serializable {

		private static final long serialVersionUID = 1L;

		@Getter
		@Setter
		private String name;

		@Getter
		@Setter
		private int age;

		@Getter
		@Setter
		private Per per;

	}

	@Test
	public void test() {

		Per pA = new Per();
		Per pB = new Per();

		pA.setName("Pa");
		pA.setAge(20);
		pA.setPer(pB);

		pB.setName("Pb");
		pB.setAge(13);

		String serObj = SerializeUtils.serializeForJson(pA, Per.class);

		System.out.println(serObj);

		Per pC = SerializeUtils.deserializeFromJson(serObj, Per.class);

		System.out.println(pC.getName());

	}

	@Test
	public void testMd5() {

		String s = "sdfsdfddddddddddddddddddddddddddddddddddddddddddfffffffffffffffdddddddddddddddddsf";

		String md5s = SerializeUtils.md5(s);

		System.out.println(md5s);

		System.out.println(md5s.length());

	}

	@Test
	public void testRegular() {

		String keyRegular = "^CrawlKey-[0-9]{13}-[0-9A-F]{32}$";

		String input = "CrawlKey-1460955929978-6A7D551DB2D1AFDB9043C74F22134015";

		boolean flag = Pattern.matches(keyRegular, input);

		System.out.println(flag);

		System.out.println(System.currentTimeMillis());
	}

}
