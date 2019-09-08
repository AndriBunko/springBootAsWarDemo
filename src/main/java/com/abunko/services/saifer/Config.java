package com.abunko.services.saifer;

import com.abunko.services.saifer.controller.MyResponseErrorHandler;
import com.abunko.services.saifer.services.SimpleOperationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

@Component
public class Config {
    @Value("${saifer.url}")
    private String BASE_SIGN_SERVER_API_URL;

    @Autowired
    private MyResponseErrorHandler myResponseErrorHandler;

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        restTemplate.setErrorHandler(myResponseErrorHandler);
        return restTemplate;
    }

    @Bean
    public SimpleOperationHelper simpleOperationHelper(){
        return new SimpleOperationHelper(BASE_SIGN_SERVER_API_URL);
    }
}
