package com.stock.core.util;

import org.apache.commons.codec.digest.DigestUtils;

public class PwdUtil {
	
	public static String SALT = "0123456789";

	public static String getPwd(String password) {
		return DigestUtils.md5Hex(String.format("%s%s", password , SALT));
	}
	
	public static void main(String[] args) {
		System.out.println(getPwd("111111"));
	}
}
