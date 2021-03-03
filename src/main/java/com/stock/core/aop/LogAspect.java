package com.stock.core.aop;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stock.core.exception.BusinessException;
import com.stock.core.util.IPAdressUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Aspect
@Component
public class LogAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

//	private static Class<?>[] FILTER = new Class<?>[] { HttpServletRequest.class, HttpServletResponse.class,
//			BindingResult.class };
	private static ThreadLocal<LogData> local = new ThreadLocal<>();

	public static LogData getLogData() {
		return LogAspect.local.get();
	}

	@Pointcut("@annotation(com.stock.core.annotation.LogPoint)")
	public void aspectRequest() {
	}


	@Around("aspectRequest()")
	public Object around(final ProceedingJoinPoint joinPoint) throws Throwable {
		Target target = new Target(joinPoint);

		LogData data = new LogData();
		this.setInput(data, target);
		LogAspect.local.set(data);

		long start = System.currentTimeMillis();
		try {
			Object result = joinPoint.proceed(target.getArgs());
			long time = System.currentTimeMillis() - start;
			data.setTime(time);
			this.setOutput(data, result, target.getReturnType());
			String s = JSON.toJSONString(data);
			LogAspect.LOGGER.info(s);
			return result;
		} catch (Throwable e) {
			long time = System.currentTimeMillis() - start;
			data.setTime(time);
			if (e instanceof BusinessException) {
				Map<String, Object> map = new HashMap<>();
				map.put("errno", ((BusinessException) e).getCode());
				map.put("errmsg", ((BusinessException) e).getMessage());
				data.setResp(map);
			} else {
				LogAspect.LOGGER.error("around error",e);
				data.setResp(e.getMessage());
			}
			LogAspect.LOGGER.info(JSON.toJSONString(data));
			throw e;
		} finally {
			LogAspect.local.remove();
		}
	}

	private HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	private void setInput(final LogData data, final Target target) {
		data.setIp(IPAdressUtils.getIP(this.getRequest()));
		HttpServletRequest request=this.getRequest();
		if(request!=null){
			data.setUri(request.getRequestURI());
		}
		List<Object> args = new ArrayList<>();
		Object[] argsCopy = Arrays.copyOf(target.getArgs(), target.getArgs().length);
		for (int i = 0; i < argsCopy.length; i++) {
			if(argsCopy[i]!=null){
				if(HttpServletRequest.class.isAssignableFrom(argsCopy[i].getClass()) || HttpServletResponse.class.isAssignableFrom(argsCopy[i].getClass())){
					//Map<String, String> map=ReqUtil.getNotifyParams((HttpServletRequest)argsCopy[i]);
					//args.add(map);
				} else if(argsCopy[i].getClass().getName().equals("org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile")) {

				} else if(argsCopy[i].getClass().isArray() && argsCopy[i].getClass().getComponentType().getName().equals("org.springframework.web.multipart.MultipartFile")){

				} else if(argsCopy[i].getClass().getName().contains("HttpServletRequest")) {

				} else {
					args.add(argsCopy[i]);
				}
			}
		}
		if (args.size() != 0) {
			data.setReq(args);
		}
	}

	private void setOutput(final LogData data, final Object result, Class<?> returnType) {
		if (returnType == Object.class) {
			returnType = result.getClass();
		}

		if (returnType == Void.class) {
			return;
		}

		if (returnType.isPrimitive() || returnType.isArray() || returnType.isEnum() || (returnType == JSONObject.class)
				|| (returnType == JSONArray.class) || (result instanceof Map)) {
			data.setResp(result);
			return;
		}

		Field[] fields = returnType.getDeclaredFields();

		Map<String, Object> attrs = new TreeMap<>();

		for (int i = 0; i < fields.length; i += 1) {
			Field field = fields[i];
			if (Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			field.setAccessible(true);
			try {
				attrs.put(field.getName(), field.get(result));
			} catch (Exception e) {
			}
		}
		if (attrs.size() != 0) {
			data.setResp(attrs);
		}
	}

}
