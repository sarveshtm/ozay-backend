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

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * Created by kn9b731 on 6/17/15
 */
@Configuration
public class ApplicationConfigurerAdapter extends WebMvcConfigurerAdapter implements EnvironmentAware  {

    private RelaxedPropertyResolver propertyResolver;

    private Environment env;

    @Override
    public void addInterceptors(final InterceptorRegistry registry)  {

        RequestInterceptor requestInterceptor = new RequestInterceptor();
        requestInterceptor.set(this.getDataSource());
        registry.addInterceptor(requestInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/")
            .excludePathPatterns("/#/login")
            .excludePathPatterns("/api/**")
            .excludePathPatterns("/error");

    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
        this.propertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");
    }

    public DataSource getDataSource() {
        if (propertyResolver.getProperty("url") == null && propertyResolver.getProperty("databaseName") == null) {
            throw new ApplicationContextException("Database connection pool is not configured correctly");
        }
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(propertyResolver.getProperty("dataSourceClassName"));
        if(StringUtils.isEmpty(propertyResolver.getProperty("url"))) {
            config.addDataSourceProperty("databaseName", propertyResolver.getProperty("databaseName"));
            config.addDataSourceProperty("serverName", propertyResolver.getProperty("serverName"));
        } else {
            config.addDataSourceProperty("url", propertyResolver.getProperty("url"));
        }
        config.addDataSourceProperty("user", propertyResolver.getProperty("username"));
        config.addDataSourceProperty("password", propertyResolver.getProperty("password"));

        return new HikariDataSource(config);
    }
}
