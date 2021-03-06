package eu.spyropoulos.wikiapp;

import eu.spyropoulos.wikiapp.config.AppConfig;
import eu.spyropoulos.wikiapp.config.DBConfig;
import eu.spyropoulos.wikiapp.config.ESConfig;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class WebInitializer implements WebApplicationInitializer {

  public void onStartup(ServletContext servletContext) throws ServletException {
    AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
    ctx.register(AppConfig.class, DBConfig.class, ESConfig.class);
    ctx.setServletContext(servletContext);
    ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(ctx));
    servlet.addMapping("/");
    servlet.setLoadOnStartup(1);
  }
}
