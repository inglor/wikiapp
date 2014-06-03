package eu.spyropoulos.wikiapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.velocity.VelocityConfig;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

import java.util.Properties;

@EnableWebMvc
@Configuration
@ComponentScan("eu.spyropoulos.wikiapp")
public class AppConfig {

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  @Bean
  public VelocityConfig velocityConfig() {
    VelocityConfigurer velocityConfigurer = new VelocityConfigurer();
    Properties velocityProperties = new Properties();
    velocityProperties.put("input.encoding", "UTF-8");
    velocityProperties.put("output.encoding", "UTF-8");
    velocityConfigurer.setVelocityProperties(velocityProperties);
    velocityConfigurer.setResourceLoaderPath("WEB-INF/views/");
    return velocityConfigurer;
  }

  @Bean
  public ViewResolver getViewResolver() {
    VelocityViewResolver r = new VelocityLayoutViewResolver();
    r.setCache(true);
    r.setContentType("text/html; charset=utf-8");
    r.setSuffix(".vm");
    return r;
  }
}
