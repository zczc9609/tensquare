package com.tensquare.jwt;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class CreatJwt {
    public static void main(String[] args) {

        //创建JWT
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId("666")
                .setSubject("zhangsan")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "itcast")
                .setExpiration(new Date(new Date().getTime()+60000))
                .claim("role","admin");
        System.out.println(jwtBuilder.compact());

    }
}
