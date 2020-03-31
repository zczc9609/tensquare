package com.tensquare.manager.filter;


import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import util.JwtUtil;


@Component
public class ManagerFilter implements GlobalFilter, Ordered {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
        //放行OPTIONS请求
//        if(request.getMethod().equals("OPTIONS")){
//            return null;
//        }

        //放行/login

        String header = exchange.getRequest().getHeaders().getFirst("Authorization");
        System.out.println(header);
        if(header!=null && !header.equals("")){
            if(header.startsWith("Bearer ")){
                String token = header.substring(7);
                Claims claims = jwtUtil.parseJWT(token);
                if(claims!=null && claims.get("roles").equals("admin")){
                    return chain.filter(exchange);
                }
            }
        }
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
