package fm.douban.config;

import fm.douban.interceptor.UserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;

/**
 * Created by pawncs on 2021/2/18.
 */

@Configuration
public class AppConfigurer implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // 仅演示，设置所有 url 都拦截
        registry.addInterceptor(new UserInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/authenticate") // 登录操作不需要登录
                .excludePathPatterns("/login")        // 登录页面不需要登录
                .excludePathPatterns("/sign")        // 注册页面不需要登录
                .excludePathPatterns("/register")        // 注册操作不需要登录
                .excludePathPatterns("/scss/**")           // 静态资源为文件不需要登录
                .excludePathPatterns("/error");            // 系统错误页面不需要登录

//        registry.addInterceptor(new UserInterceptor()).addPathPatterns("/**")
//                .excludePathPatterns("/**"); // 登录操作不需要登录需要登录

    }
}
