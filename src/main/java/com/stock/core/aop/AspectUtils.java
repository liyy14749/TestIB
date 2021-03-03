package com.stock.core.aop;

import com.stock.core.annotation.LogPoint;

public class AspectUtils {

	public static boolean isLogPoint(final Target target) {

		return target.getMethod().isAnnotationPresent(LogPoint.class);
	}

}
