package digitalmanagement.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration//配置类
public class MybatisPlusConfig {

//    @Bean//使用 @Autowired 注入某个对象时，Spring 就是从这个仓库里把对应的 Bean 拿给你的。
//    public MybatisPlusInterceptor mybatisPlusInterceptor() {
//        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();//创建分页拦截器对象
//        //new paginationinnerinterceptor()创建分页拦截器的具体零件
//        //addInnerInterceptor()方法->把创建的分页拦截器零件安装到分页拦截器上面去
//        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));//databasetype是mysql
//        return interceptor;
//    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor m = new MybatisPlusInterceptor();
        m.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return m;
    }

}
