package com.stock.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropConfig {

	@Value("${schedule.switch}")
	private String scheduleSwitch;

	@Value("${ios.subscribe.pwd}")
	private String iosSubscribePwd;

	public String getIosSubscribePwd() {
		return iosSubscribePwd;
	}

	public void setIosSubscribePwd(String iosSubscribePwd) {
		this.iosSubscribePwd = iosSubscribePwd;
	}

	public String getScheduleSwitch() {
		return scheduleSwitch;
	}

	public void setScheduleSwitch(String scheduleSwitch) {
		this.scheduleSwitch = scheduleSwitch;
	}
	
}
