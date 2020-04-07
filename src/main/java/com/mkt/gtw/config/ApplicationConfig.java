package com.mkt.gtw.config;

import java.util.Map;

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

import com.mkt.gtw.filters.HeadersFilter;
import com.mkt.gtw.model.RouteModel;

import reactor.core.publisher.Mono;

@Configuration
public class ApplicationConfig {
	
	private final static Logger LOGGER = LogManager.getLogger();
	
	@Autowired
	private RoutesPropertiesConfig config;
	
	@Autowired
	private HeadersFilter headersFilter;
	
	@Value("${server.servlet.context-path}")
	private String contextPath;

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		
		Map<String,RouteModel> routes=config.getRoutes();
		
		RouteLocatorBuilder.Builder builderRoutes = builder.routes();
		
		for(Map.Entry<String, RouteModel> entry : routes.entrySet()) {
			
			RouteModel routeModel = entry.getValue();
			String routeKey = entry.getKey();
			LOGGER.info("Registrando route para {}",routeKey);
			
			String typeService = routeModel.getTypeService();
			String endpoint = routeModel.getDomainRoute()+":"+routeModel.getPortRoute();
			String exposureRoute = routeModel.getExposureRoute();
			
			builderRoutes.route(routeModel.getIdRoute(),p -> p.path("/"+typeService+exposureRoute+"/**") //
					.filters(f ->  f.rewritePath("^(\\/service)",routeModel.getPathRoute())
							.filter(headersFilter.apply(routeModel)))
					.uri(endpoint)).build();
		}
		
	    return builderRoutes.build();
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
