package com.stock.core.common;

/**
 * @description:
 * @author: YJH
 * @time: 2020/4/5 10:47
 */
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author xiong
 *
 */
public class Result extends HashMap<String, Object> implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Result() {
		this.put("code", 200);
		this.put("msg", "SUCCESS");
	}
	public Result(int code,String msg){
		this.put("code", code);
		this.put("msg", msg);
	}
	public Result(int code,String msg,Object data){
		this.put("code", code);
		this.put("msg", msg);
		this.put("data", data);
	}

	public Result(int code,String msg,String fieldName,Object data){
		this.put("code", code);
		this.put("msg", msg);
		this.put(fieldName, data);
	}
	public Result(int code) {
		this.put("code", String.valueOf(code));
	}
	public int getCode() {
		return Integer.parseInt(this.get("code").toString());
	}
	public String getMsg() {
		return this.get("msg").toString();
	}

	public Result setCode(int code) {
		this.put("code", String.valueOf(code));
		return this;
	}
	public Result setMsg(String msg) {
		this.put("msg", msg);
		return this;
	}
	public Result setCodeMsg(int code,String errorMessage) {
		this.put("code", String.valueOf(code));
		this.put("msg", errorMessage);
		return this;
	}
	public static Result ok(){
		return new Result();
	}
	public static Result fail(int code,String msg){
		Result json=new Result();
		json.put("code", code);
		json.put("msg", msg);
		return json; 
	}
	
	public static Result fail(int code,String msg,Map<String, Object> params){
		Result json=new Result();
		json.put("code", code);
		json.put("msg", msg);
		if(params!=null && params.size()>0) {
			for(String key:params.keySet()) {
				json.put(key, params.get(key));
			}
		}
		return json; 
	}
	
    public static Object badArgument() {
        return fail(401, "param cannot is null");
    }

    public static Object badArgumentValue() {
        return fail(402, "param cannot is null");
    }

    public static Object unlogin() {
        return fail(501, "请登录");
    }

    public static Object serious() {
        return fail(502, "系统内部错误");
    }

    public static Object unsupport() {
        return fail(503, "业务不支持");
    }

    public static Object updatedDateExpired() {
        return fail(504, "更新数据已经失效");
    }

    public static Object updatedDataFailed() {
        return fail(505, "更新数据失败");
    }

    public static Object unauthz() {
        return fail(506, "无操作权限");
    }
}
