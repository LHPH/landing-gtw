package com.mkt.gtw.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.WebFilter;

import com.mkt.gtw.model.RouteModel;

import reactor.core.publisher.Mono;

@Configuration
public class ApplicationConfig {
	
	private final static Logger LOGGER = LogManager.getLogger();
	
	@Autowired
	private RoutesConfig config;
	
	@Value("${server.servlet.context-path}")
	private String contextPath;

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		
		LOGGER.info("*** {}",config.getRoutes().get("key1"));
		
		RouteModel landing = config.getRoutes().get("key1");
		
		String get = landing.getDomainRoute()+":"+landing.getPortRoute();
		
	    return builder.routes().route(landing.getIdRoute(),
	    		p -> p.path("/service/landing")
	    	.filters(f ->  f.rewritePath("^(\\/service)",landing.getPathRoute())
	    					.addRequestHeader("token",landing.getHeadersRequest().get("key")))
	    	.uri(get)	    		
	    	)
	    		.build();
	}
	
	@Bean
	public WebFilter contextPathWebFilter() {
		
		return (exchange,chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			if (request.getURI().getPath().startsWith(contextPath)) {
	            return chain.filter(
	                exchange.mutate().request(request.mutate()
	                		.path(request.getURI().getPath().replace(contextPath,"")).build())
	                .build());
	        }
			ServerHttpResponse response = exchange.getResponse();
			response.setStatusCode(HttpStatus.NOT_FOUND);
	        return response.setComplete();
		};
	}
	
}
