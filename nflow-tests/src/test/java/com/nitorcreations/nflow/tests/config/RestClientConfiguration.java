package com.nitorcreations.nflow.tests.config;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.Arrays;

import javax.inject.Named;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.nitorcreations.nflow.rest.config.JacksonJodaModule;

@Configuration
public class RestClientConfiguration {

  @Bean(name="base-webclient")
  public WebClient baseWebClient(JacksonJsonProvider jsonProvider, Environment env) {
    JAXRSClientFactoryBean bean = new JAXRSClientFactoryBean();
    bean.setAddress(env.getRequiredProperty("nflow.url"));
    bean.getFeatures().add(new LoggingFeature());
    bean.setProviders(Arrays.asList(jsonProvider));
    bean.setBus(cxf());
    return bean.createWebClient().type(APPLICATION_JSON).accept(APPLICATION_JSON);
  }

  @Bean(destroyMethod = "shutdown")
  public SpringBus cxf() {
    return new SpringBus();
  }

  @Bean
  public ObjectMapper objectMapper() {
    // this must be kept in sync with the server side
    return new ObjectMapper().registerModule(new JacksonJodaModule());
  }

  @Bean
  public JacksonJsonProvider jsonProvider(ObjectMapper mapper) {
    return new JacksonJsonProvider(mapper);
  }

  @Bean(name="workflow-instance")
  public WebClient workflowInstanceWebService(@Named("base-webclient") WebClient client) {
    return client.path("v0").path("workflow-instance");
  }

}
