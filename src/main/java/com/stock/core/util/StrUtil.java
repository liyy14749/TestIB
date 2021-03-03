package com.stock.core.util;

public class StrUtil {

    public static String anonymous(String e) {
    	StringBuilder email = new StringBuilder(e);
        try {
        	int index1 = email.lastIndexOf("@");
            email = email.replace(2, index1, repeat('*', index1-2));
            index1 = email.lastIndexOf("@");
            email = email.replace(index1+2, email.length()-1, repeat('*', email.length()-index1-3));
            return email.toString();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
        return email.toString();
    }
    
    public static String repeat(char s,int num) {
    	StringBuilder sb = new StringBuilder();
        for(int i=0;i<num;i++) {
        	sb.append(s);
        }
        return sb.toString();
    }
    
    public static String uniqueDevice(String model,String deviceName) {
    	StringBuilder sb = new StringBuilder();
        if(model == null) {
        	model = "";
        }
        if(deviceName == null) {
        	deviceName = "";
        }
        sb.append(model).append("_").append(deviceName);
        return sb.toString();
    }


    public static void main(String[] args) {
		System.out.println(anonymous("jpx_011@163.com"));
	}
}
