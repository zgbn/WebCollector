package cn.vfire.web.collector3.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RegexRule {

	private ArrayList<String> positive = new ArrayList<String>();

	private ArrayList<String> negative = new ArrayList<String>();

	public RegexRule() {

	}

	public RegexRule(List<String> rules) {
		if (rules != null) {
			for (String rule : rules) {
				addRule(rule);
			}
		}
	}

	public RegexRule(List<String> rules, List<String> unrules) {

		if (rules != null) {
			for (String rule : rules) {
				addRule(rule);
			}
		}
		if (unrules != null) {
			for (String rule : unrules) {
				addRule("-" + rule);
			}
		}
	}

	public RegexRule(String rule) {
		addRule(rule);
	}

	/**
	 * 添加一个反正则规则
	 * 
	 * @param negativeregex
	 * @return
	 */
	public RegexRule addNegative(String negativeregex) {
		negative.add(negativeregex);
		return this;
	}

	/**
	 * 添加一个正正则规则
	 * 
	 * @param positiveregex
	 * @return
	 */
	public RegexRule addPositive(String positiveregex) {
		positive.add(positiveregex);
		return this;
	}

	/**
	 * 添加一个正则规则 正则规则有两种，正正则和反正则 URL符合正则规则需要满足下面条件： 1.至少能匹配一条正正则 2.不能和任何反正则匹配
	 * 
	 * @param rule
	 * @return
	 */
	public RegexRule addRule(String rule) {
		if (rule.length() == 0) {
			return this;
		}
		char pn = rule.charAt(0);
		String realrule = rule.substring(1);
		if (pn == '+') {
			addPositive(realrule);
		} else if (pn == '-') {
			addNegative(realrule);
		} else {
			addPositive(rule);
		}
		return this;
	}

	public boolean isEmpty() {
		return positive.isEmpty();
	}

	/**
	 * 判断输入字符串是否符合正则规则
	 * 
	 * @param str
	 * @return
	 */
	public boolean satisfy(String str) {

		for (String nregex : negative) {
			if (Pattern.matches(nregex, str)) {
				return false;
			}
		}

		int count = 0;
		for (String pregex : positive) {
			if (Pattern.matches(pregex, str)) {
				count++;
			}
		}
		if (count == 0) {
			return false;
		} else {
			return true;
		}

	}
}
