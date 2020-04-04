package com.mkt.gtw.model;

import java.util.Map;

import com.mkt.core.base.BaseModel;

public class RouteModel extends BaseModel{
	
	private String idRoute;
	private String domainRoute;
	private String pathRoute;
	private String portRoute;
	private Map<String,String> headersRequest;
	
	
	public String getIdRoute() {
		return idRoute;
	}
	public void setIdRoute(String idRoute) {
		this.idRoute = idRoute;
	}
	public String getPathRoute() {
		return pathRoute;
	}
	public void setPathRoute(String pathRoute) {
		this.pathRoute = pathRoute;
	}
	public String getPortRoute() {
		return portRoute;
	}
	public void setPortRoute(String portRoute) {
		this.portRoute = portRoute;
	}
	public Map<String, String> getHeadersRequest() {
		return headersRequest;
	}
	public void setHeadersRequest(Map<String, String> headersRequest) {
		this.headersRequest = headersRequest;
	}
	public String getDomainRoute() {
		return domainRoute;
	}
	public void setDomainRoute(String domainRoute) {
		this.domainRoute = domainRoute;
	}
	
	

}
