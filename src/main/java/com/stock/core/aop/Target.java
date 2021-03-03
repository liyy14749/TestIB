package com.stock.core.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

public class Target {
	private Class<?> type;

	private Method method;

	private Object[] args;

	private Class<?>[] parameterTypes;

	private Class<?> returnType;

	public Target(final JoinPoint joinPoint) {
		this.args = joinPoint.getArgs();
		this.type = joinPoint.getTarget().getClass();
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

		this.parameterTypes = methodSignature.getParameterTypes();
		try {
			this.method = this.type.getDeclaredMethod(methodSignature.getName(), this.parameterTypes);

			this.returnType = this.method.getReturnType();
		} catch (NoSuchMethodException | SecurityException e) {
		}
	}

	public Class<?> getType() {
		return this.type;
	}

	public Method getMethod() {
		return this.method;
	}

	public Object[] getArgs() {
		return this.args;
	}

	public Class<?>[] getParameterTypes() {
		return this.parameterTypes;
	}

	public String getMethodName() {
		return this.method.getName();
	}

	public Class<?> getReturnType() {
		return this.returnType;
	}

}
