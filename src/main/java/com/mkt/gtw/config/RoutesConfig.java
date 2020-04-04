package com.mkt.gtw.config;

import static com.mkt.core.constants.ConstantsGeneral.YML_APP_CONFIG_PREFIX;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.mkt.gtw.model.RouteModel;

@Component
@ConfigurationProperties(prefix=YML_APP_CONFIG_PREFIX)
public class RoutesConfig {
	
	

	private Map<String,RouteModel> routes;

	public Map<String, RouteModel> getRoutes() {
		return routes;
	}

	public void setRoutes(Map<String, RouteModel> routes) {
		this.routes = routes;
	}

	
	
	
	
}
