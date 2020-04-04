package com.mkt.gtw.filters;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.mkt.core.model.Message;

import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class ErrorFilter extends AbstractErrorWebExceptionHandler {
	
	private final static Logger LOGGER = LogManager.getLogger();

	public ErrorFilter(ErrorAttributes errorAttributes, ResourceProperties resourceProperties,
			ApplicationContext applicationContext,ServerCodecConfigurer configurer) {
		super(errorAttributes, resourceProperties, applicationContext);
		this.setMessageWriters(configurer.getWriters());
	}

	@Override
	protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
		
		return RouterFunctions.route(RequestPredicates.all(),this::renderErrorResponse);
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 * 
	 * Change the error template as
	 * 
	 * {
	    timestamp": 2020-04-04T22:37:02.751+0000",
	    path": /servic,
	    status": 404,
	    error": Not Found,
	    message": null,
	    requestId": e3a7d446-1,
	    trace: exception 
	 * 
	 */
	
	private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
		
		Map<String, Object> errorPropertiesMap = getErrorAttributes(request, true);
		
		Message error = new Message();
		error.setCode("CODE");
		error.setType("ERROR");
		error.setDescription("Ocurrio un error en: "+request.path());
		error.setStatus(HttpStatus.valueOf(Integer.valueOf(errorPropertiesMap.get("status").toString())));
		
	    LOGGER.info("Error {}",errorPropertiesMap.get("trace"));
		
		return ServerResponse.status(HttpStatus.BAD_REQUEST)
		         .contentType(MediaType.APPLICATION_JSON)
		         .body(BodyInserters.fromValue(error));
		
	}
	
	

}
