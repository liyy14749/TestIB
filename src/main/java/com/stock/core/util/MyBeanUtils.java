package com.stock.core.util;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
 
public class MyBeanUtils extends BeanUtils{
 
	public static String[] getNullPropertyNames (Object source,boolean copyId) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
 
        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        if(!copyId) {
        	emptyNames.add("id");
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
 
	//封装同名称属性复制，但是空属性不复制过去
    public static void copyPropertiesIgnoreNull(Object src, Object target){
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src,true));
    }
    
    public static void copyPropertiesIgnoreNullAndId(Object src, Object target){
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src,false));
    }
	
}