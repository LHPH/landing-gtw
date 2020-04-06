package com.mkt.gtw.filters;

import java.util.Map;
import java.util.function.Consumer;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.mkt.gtw.model.RouteModel;

@Component
public class HeadersFilter extends AbstractGatewayFilterFactory<RouteModel>{

	@Override
	public GatewayFilter apply(RouteModel config) {
		
		Map<String,String> requestHeaders = config.getHeadersRequest();
			
		Consumer<HttpHeaders> httpHeaders = httpHeader -> {
			for(Map.Entry<String, String> entry : requestHeaders.entrySet() ) {
				httpHeader.add(entry.getKey(), entry.getValue());
			}
		};
		
		return (exchange,chain) -> {
			
			exchange.getRequest().mutate().headers(httpHeaders).build();
			
			return chain.filter(exchange);
		};
	}

}
