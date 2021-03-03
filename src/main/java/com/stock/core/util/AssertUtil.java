package com.stock.core.util;

import com.stock.core.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

public class AssertUtil {

	public static void validateEmpty(Object field,String fieldName) {
		if(field instanceof String) {
			if(StringUtils.isBlank((String)field)) {
				throw new BusinessException(401,String.format("%s is empty", fieldName));
			}
		} else {
			if(field == null) {
				throw new BusinessException(401,String.format("%s is empty", fieldName));
			}
		}
	}
	
}
