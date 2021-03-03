package com.stock.core.exception;

import com.stock.core.common.Result;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Set;

@ControllerAdvice
@Order
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public Object badArgumentHandler(IllegalArgumentException e) {
        logger.error(e.getMessage(), e);
        return Result.badArgumentValue();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public Object badArgumentHandler(MethodArgumentTypeMismatchException e) {
        logger.error(e.getMessage(), e);
        return Result.badArgumentValue();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public Object badArgumentHandler(MissingServletRequestParameterException e) {
        logger.error(e.getMessage(), e);
        return Result.badArgumentValue();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public Object badArgumentHandler(HttpMessageNotReadableException e) {
        logger.error(e.getMessage(), e);
        return Result.badArgumentValue();
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public Object badArgumentHandler(ValidationException e) {
        logger.error(e.getMessage(), e);
        if (e instanceof ConstraintViolationException) {
            ConstraintViolationException exs = (ConstraintViolationException) e;
            Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
            for (ConstraintViolation<?> item : violations) {
                String message = ((PathImpl) item.getPropertyPath()).getLeafNode().getName() + item.getMessage();
                return Result.fail(402, message);
            }
        }
        return Result.badArgumentValue();
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Object businessExceptionHandler(BusinessException e) {
        logger.error(String.format("code %s message %s", e.getCode(),e.getMessage()));
        return Result.fail(e.getCode(), e.getMessage(), e.getParams());
    }
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Object httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        logger.error("code {} message {}" ,401,"not support method");
        return Result.fail(401, "not support method");
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object seriousHandler(Exception e) {
        logger.error(e.getMessage(), e);
        return Result.fail(502, e.getMessage());
    }
}
