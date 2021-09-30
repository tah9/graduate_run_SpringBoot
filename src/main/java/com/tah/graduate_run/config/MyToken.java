package com.tah.graduate_run.config;

import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * ->  tah9  2021/8/31 17:05
 */
public class MyToken {
//    /* 使用手机号创建，用密码加密
//     * @param: user
//     * @return: java.lang.String
//     */
//   public static String create(SysUser user){
//
//       String token = JWT.create().withAudience(String.valueOf(user.getPhone_number()))
//               .sign(Algorithm.HMAC256(user.getPassword()));
//       return token;
//    }
//
//    public static void verify(SysUser user,String token){
//        if (user == null) {
//            throw new RuntimeException("用户不存在，请重新登录");
//        }
//        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
//        try {
//            jwtVerifier.verify(token);
//        } catch (JWTVerificationException e) {
//            throw new RuntimeException("token和密码不匹配");
//        }
//    }

    private static final String SECRET = "asdfjadfjakldsjfladsf";//私密key
    private static final Long TTL_EXPIRATION = 1000L * 60 * 240*60; //过期时间240h
    private static final String ISSUER = "pibigstar";//发行人

    /**
     * 加密信息，生成token
     */
    public static String create(Map<String,Object> params) {
        SignatureAlgorithm signature = SignatureAlgorithm.HS256;

        byte[] secretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        Key secretKey = new SecretKeySpec(secretBytes, signature.getJcaName());
        Long expiration = System.currentTimeMillis()/1000 + TTL_EXPIRATION;

        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(expiration))
                .setIssuer(ISSUER)
                .setClaims(params)
                .signWith(signature,secretKey);

        return builder.compact();
    }

    /**
     * 解析token
     */
    public static Map<String, Object> verify(String token) throws SignatureException,MalformedJwtException,ExpiredJwtException {
        Claims  claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
                    .parseClaimsJws(token).getBody();
        return claims;
    }
    public static String getUid(HttpServletRequest request){
        String token = request.getHeader("token");
        Map<String, Object> verify = MyToken.verify(token);
        Map user = (Map) verify.get("user");
        return user.get("uid").toString();
    }
}
