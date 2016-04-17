package cn.vfire.common.utils;

import static org.junit.Assert.*;

import java.io.Serializable;

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

}
