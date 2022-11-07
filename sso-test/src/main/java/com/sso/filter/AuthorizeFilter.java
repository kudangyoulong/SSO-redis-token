package com.sso.filter;

import com.sso.model.enums.AppHttpCodeEnum;
import com.sso.utils.AppJwtUtil;
import com.sso.utils.RedisUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@WebFilter(filterName = "authorizeFilter",urlPatterns = "/*")
@RequiredArgsConstructor
public class AuthorizeFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RedisTemplate redisTemplate = RedisUtils.redis;

        HttpServletResponse resp = ((HttpServletResponse)response);
        //判断是否是登录接口，是直接放行
        String path = ((HttpServletRequest)request).getRequestURI();
        System.out.println(path);
        if(path.equals("/user/login")){
            chain.doFilter(request, response);
        }

        //非登录接口，获取token
        String token = (String) redisTemplate.opsForValue().get("1");

        System.out.println(token);
        //判断token是否存在，不存在返回401(无权访问)
        if(StringUtils.isBlank(token)){
            Map<AppHttpCodeEnum, HttpStatus> errorDetails = new HashMap<>();
            errorDetails.put(AppHttpCodeEnum.TOKEN_REQUIRE,HttpStatus.UNAUTHORIZED);

            resp.setStatus(HttpStatus.UNAUTHORIZED.value());
            resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
            return;
        }

        //判断token是否有效，无效返回401(无权访问)
        try {
            Claims claimsBody = AppJwtUtil.getClaimsBody(token);
            int result = AppJwtUtil.verifyToken(claimsBody);
            if(result==0){
                Map<AppHttpCodeEnum, HttpStatus> errorDetails = new HashMap<>();
                errorDetails.put(AppHttpCodeEnum.TOKEN_INVALID,HttpStatus.UNAUTHORIZED);

                resp.setStatus(HttpStatus.UNAUTHORIZED.value());
                resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
                return;

            }

            //从Token的载荷里取出用户ID
            String userId = String.valueOf(claimsBody.get("id")) ;
            String newtoken = AppJwtUtil.getToken(Long.valueOf(userId));
            redisTemplate.opsForValue().set("1",newtoken,35 * 60 *1000, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //6.放行
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
