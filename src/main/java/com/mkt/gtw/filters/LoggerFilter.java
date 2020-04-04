package com.mkt.gtw.filters;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;


@Component
public class LoggerFilter implements GlobalFilter{
	
	private final static Logger LOGGER = LogManager.getLogger();

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		
		Set<URI> uris = exchange.getAttributeOrDefault(GATEWAY_ORIGINAL_REQUEST_URL_ATTR, Collections.emptySet());
		String originalUri = (uris.isEmpty()) ? exchange.getRequest().getURI().toString() : uris.iterator().next().toString();
		Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
		URI routeUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
		
		LOGGER.info("Request: {} to route id: {}, url :{}",originalUri,route.getId(),routeUri);

		return chain.filter(exchange);
	}

}
