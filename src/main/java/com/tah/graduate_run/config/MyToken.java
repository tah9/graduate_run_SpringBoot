package com.tah.graduate_run.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.tah.graduate_run.entity.Article;
import com.tah.graduate_run.entity.SysUser;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Long TTL_EXPIRATION = 1000L * 60 * 240 * 60; //过期时间240h
    private static final String ISSUER = "pibigstar";//发行人

    /**
     * 加密信息，生成token
     */
    public static String create(Map<String, Object> params) {
        SignatureAlgorithm signature = SignatureAlgorithm.HS256;

        byte[] secretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        Key secretKey = new SecretKeySpec(secretBytes, signature.getJcaName());
        Long expiration = System.currentTimeMillis() / 1000 + TTL_EXPIRATION;

        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(expiration))
                .setIssuer(ISSUER)
                .setClaims(params)
                .signWith(signature, secretKey);

        return builder.compact();
    }

    /**
     * 解析token
     */
    public static Map<String, Object> verify(String token) throws SignatureException, MalformedJwtException, ExpiredJwtException {
        Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
                .parseClaimsJws(token).getBody();
        return claims;
    }

    public static String getUid(HttpServletRequest request) {
        String token = request.getHeader("token");
        return MyToken.verify(token).get("uid").toString();
    }

    private static final Logger log = LoggerFactory.getLogger(MyToken.class);

//    public static SysUser getUser(HttpServletRequest request) throws Exception {
//        String token = request.getHeader("token");
//        Map<String, Object> verify = MyToken.verify(token);
//        Map map = (Map) verify.get("user");
//        log.info(map.toString());
//        SysUser user = new SysUser();
//        user.setUsername(map.get("username").toString());
//        user.setUid(Long.parseLong(map.get("uid").toString()));
//        user.setCreate_time(map.get("create_time").toString());
//        user.setPhone_number(map.get("phone_number").toString());
//        user.setLogin_ip(map.get("login_ip").toString());
//        user.setLogintime(map.get("logintime").toString());
//        user.setUserAvatar(map.get("userAvatar").toString());
//        user.setHeight(Integer.parseInt(map.get("height").toString()));
//        user.setWeight(Integer.parseInt(map.get("weight").toString()));
//        user.setGender(Integer.parseInt(map.get("gender").toString()));
//        user.setBirthday(map.get("birthday").toString());
//        user.setCover(map.get("cover").toString());
//        user.setBio(map.get("bio")==null?"这里是签名~":map.get("bio").toString());
//        return user;
//    }
}
