package com.stock.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropConfig {

	@Value("${my.schedule.switch}")
	private String scheduleSwitch;

	public String getScheduleSwitch() {
		return scheduleSwitch;
	}

	public void setScheduleSwitch(String scheduleSwitch) {
		this.scheduleSwitch = scheduleSwitch;
	}
	
}
