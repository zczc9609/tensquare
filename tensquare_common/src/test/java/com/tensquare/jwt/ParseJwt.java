package com.tensquare.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.text.SimpleDateFormat;

public class ParseJwt {
    public static void main(String[] args) {

        //解析JWT
        Claims claims = Jwts.parser().setSigningKey("itcast")
                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NjYiLCJzdWIiOiJ6aGFuZ3NhbiIsImlhdCI6MTU4NDc5NTMwNywiZXhwIjoxNTg0Nzk1MzY3fQ.NuX4AflQgg0qyxqlJhcOBsyoiLqejneHxX_7ClU73nw")
                .getBody();
        System.out.println(claims.getId());
        System.out.println(claims.getSubject());
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getIssuedAt()));
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getExpiration()));
        System.out.println(claims.get("role"));
    }
}
