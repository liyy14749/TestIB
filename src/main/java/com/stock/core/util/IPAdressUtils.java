package com.stock.core.util;

import javax.servlet.http.HttpServletRequest;

public class IPAdressUtils {
	private static final String[] HEADER_NAMES = { "x-forwarded-for", "x-real-ip", "Proxy-Client-IP",
			"WL-Proxy-Client-IP", "clientip" };

	public static String getIP(final HttpServletRequest request) {
		String ip = null;
		for (int i = 0; i < IPAdressUtils.HEADER_NAMES.length; i += 1) {
			ip = request.getHeader(IPAdressUtils.HEADER_NAMES[i]);
			if (IPAdressUtils.validateIP(ip)) {
				continue;
			} else {
				int index = ip.indexOf(",");
				if (index != -1) {
					ip = ip.substring(0, index);
					if (IPAdressUtils.validateIP(ip)) {
						continue;
					}
				}
				return ip;
			}
		}
		if (ip == null) {
			ip = request.getRemoteAddr();
		}
		if("0:0:0:0:0:0:0:1".equals(ip)) {
			ip="127.0.0.1";
		}
		return ip;
	}

	private static boolean validateIP(final String ip) {
		return (ip == null) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip);
	}
}
