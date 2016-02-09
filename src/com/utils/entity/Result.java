package com.utils.entity; 

import java.util.HashMap;
import java.util.Map;

/** 
 * @author springCat E-mail:329839705@qq.com 
 * @version 创建时间：2014年6月13日 上午10:18:34 
 */
public class Result{
	
	private Integer code;
	
	private String message;
	
	private Map<String, Object> resultMap = new HashMap<String, Object>();
	private Map<String, Object> responseMap = new HashMap<String, Object>();
	
	public Result(){
		
	}
	public Result( Integer code,String message){
		this.code = code;
		this.message = message;
	}

	public static Result getResult(String name){

		switch(name){
			case "success": return new Result(0,"成功");
			case "fail": return new Result(2,"失败");
			case "mobile_is_exist": return new Result(3,"该手机号已存在于该场次的报名中！");

			/**
			 * 登录、权限信息
			 */
			case "login_success": return new Result(0,"登录成功!");
			case "login_account_or_password_error": return new Result(2,"账号或密码错误");
			case "login_lock_error": return new Result(4,"账号已被锁定，请与系统管理员联系");
			case "login_noPermission_put": return new Result(5,"您没有授权!");

			default:
	             throw new IllegalArgumentException("Invalid result type: " + name);
		}
	}
	
	public Integer getCode(){
		return this.code;
	}
	
	public String getMessage(){
		return this.message;
	}
	
	public Map<String, Object> getResponseMap(){
		return this.responseMap;
	}
	
	public void setResponseMap(Map<String, Object> responseMap){
		this.responseMap = responseMap;
	}	
	
	public Map<String, Object> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
	}

	public Map<String, Object> toMap(){
		Map<String, Object> m = new HashMap<String, Object>();

		resultMap.put("code", this.code);
		resultMap.put("message", this.message);
		
		m.put("result", resultMap);
		m.put("response", responseMap);
		
		return m;
	}
	
	public Result setParam(String key,Object value){	
		this.responseMap.put(key, value);
		return this;
	}
	
	public Result setParam(Map<String, Object> map){
		for(Map.Entry<String, Object> entry : map.entrySet()){
			responseMap.put(entry.getKey(), entry.getValue());
		}
		return this;
	}
}
