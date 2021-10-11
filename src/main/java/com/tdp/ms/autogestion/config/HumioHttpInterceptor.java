package com.tdp.ms.autogestion.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class HumioHttpInterceptor implements WebFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(HumioHttpInterceptor.class);
    String queueNameBackend = "ms-trazabilidad-autogestion";//System.getenv("HUMIO_APPLICATION_NAME") != null ? System.getenv("HUMIO_APPLICATION_NAME") : "";

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {

        long init = System.currentTimeMillis();

        serverWebExchange.getResponse().beforeCommit(() -> {
        	if(!filterRoutes(serverWebExchange.getRequest())) {
        		logMetric(serverWebExchange.getRequest(), serverWebExchange.getResponse(), init);
        	}
            return Mono.empty();
        });

        return webFilterChain.filter(serverWebExchange);
    }

    private void logMetric(ServerHttpRequest request, ServerHttpResponse response, long startMillis) {
        String message = new StringBuilder()
                .append("\n"+"Microservicio: " + queueNameBackend )
                .append("\n"+"Endpoint: " + request.getMethod().name() + " " + request.getPath().toString() + " \n")
                .append("Time: " + (System.currentTimeMillis() - startMillis)/1000.00 +""+ "\n")
                .append("Status code: " + response.getStatusCode().value())
                .toString();
        LOGGER.info(message);
    }
    
    private boolean filterRoutes(ServerHttpRequest request) {
		return (HttpMethod.GET == request.getMethod() &&
				(
				StringUtils.equalsAny(request.getPath().toString(), "/", "/robots933456.txt")
				|| StringUtils.startsWithAny(request.getPath().toString(), 
						"/actuator", "/swagger-ui", "/webjars", "/v3/api-docs")
				)
			);
	}
}
