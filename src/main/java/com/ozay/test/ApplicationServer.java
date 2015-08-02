package com.ozay.test;

/**
 * Created by naofumiezaki on 8/2/15.
 */
import com.ozay.config.*;
//import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;


//@Slf4j
@Import(value = {ApplicationConfigurerAdapter.class, MetricsConfiguration.class, LoggingAspectConfiguration.class, CacheConfiguration.class, SecurityConfiguration.class, DatabaseConfiguration.class, ThymeleafConfiguration.class, OAuth2ServerConfiguration.class, SecurityConfiguration.class})
@ComponentScan(basePackages = {"com.ozay.repository", "com.ozay.service"}, excludeFilters = {@ComponentScan.Filter(Configuration.class)})
@Configuration
@EnableTransactionManagement(proxyTargetClass = false, mode = AdviceMode.PROXY)
public class ApplicationServer {


}
