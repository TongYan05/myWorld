package digitalmanagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")          // 对所有路径（所有接口）都启用跨域支持
                .allowedOriginPatterns("*") // 允许任意来源的请求（* 表示所有域名、IP 都可以访问）
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的 HTTP 请求方法：查询、新增、修改、删除、预检
                .allowedHeaders("*")        // 允许请求中携带任意请求头（如 Content-Type、Token 等）
                .allowCredentials(true);    // 允许携带凭证信息（如 Cookie、Authorization 请求头）
    }
}
