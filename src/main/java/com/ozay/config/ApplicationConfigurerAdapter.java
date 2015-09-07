package com.ozay.config;
import com.ozay.config.metrics.DatabaseHealthIndicator;
import com.ozay.security.RequestInterceptor;
import com.ozay.web.rest.BuildingResource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.cloud.service.relational.DataSourceConfig;
import javax.sql.DataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.context.EnvironmentAware;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.Arrays;

/**
 * Created by kn9b731 on 6/17/15
 */
@Configuration
public class ApplicationConfigurerAdapter extends WebMvcConfigurerAdapter implements EnvironmentAware  {

    private RelaxedPropertyResolver propertyResolver;

    private Environment env;

    @Inject
    private DataSource datasource;


    @Bean
    public RequestInterceptor pagePopulationInterceptor() {
        return new RequestInterceptor();
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry)  {
        registry.addInterceptor(pagePopulationInterceptor())
            .addPathPatterns("/**")
            .excludePathPatterns("/")
            .excludePathPatterns("/app/**")
            .excludePathPatterns("/api/organization-user/key")
            .excludePathPatterns("/api/organization")
            .excludePathPatterns("/api/search/**")
            .excludePathPatterns("/oauth/**")
            .excludePathPatterns("/error");

    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
        this.propertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");
    }


}
