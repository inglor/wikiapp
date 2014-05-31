package eu.spyropoulos.wikiapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.velocity.VelocityConfig;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

@EnableWebMvc
@Configuration
@ComponentScan("eu.spyropoulos.wikiapp")
public class AppConfig {

//  @Bean
//  public UrlBasedViewResolver setupViewResolver() {
//    UrlBasedViewResolver resolver = new UrlBasedViewResolver();
//    resolver.setPrefix("/WEB-INF/views/");
//    resolver.setSuffix(".jsp");
//    resolver.setViewClass(JstlView.class);
//    return resolver;
//  }


  @Bean
  public VelocityConfig velocityConfig() {
    VelocityConfigurer velocityConfigurer = new VelocityConfigurer();
    velocityConfigurer.setResourceLoaderPath("WEB-INF/views/");
    return velocityConfigurer;
  }

  @Bean
  public ViewResolver getViewResolver() {
    VelocityViewResolver r = new VelocityLayoutViewResolver();
    r.setCache(true);
//    r.setPrefix("WEB-INF/views/");
    r.setSuffix(".vm");
    return r;
  }
}
