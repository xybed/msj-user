package com.msj.user.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.SignatureException;
import java.util.Date;

public class JWTUtil {
    private static final String TOKEN_KEY = "7JiaAuth";

    public static String createNewToken(String appId){
        //获取当前时间
        Date now = new Date(System.currentTimeMillis());
        //过期时间，设置为一天
        Date expiration = new Date(now.getTime() + 24 * 60 * 60 * 1000);
        return Jwts
                .builder()
                .setSubject(appId)
                //.claim(YAuthConstants.Y_AUTH_ROLES, userDbInfo.getRoles())
                .setIssuedAt(now)
                .setIssuer("Online YAuth Builder")
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, TOKEN_KEY)
                .compact();
    }

    public static Claims getClaims(String token) throws ExpiredJwtException,SignatureException {
        return Jwts
                .parser()
                .setSigningKey(TOKEN_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
