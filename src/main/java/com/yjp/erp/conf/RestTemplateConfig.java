package com.yjp.erp.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @ClassName: RestClientConfig
 * @Description: Spring封装库，采用注入方式提供更为简洁的资源请求方式RestTemplate
 * @author: jihongjin@yijiupi.com
 * @date: 2019年4月8日 上午9:54:51
 *
 * @Copyright: 2019 www.yijiupi.com Inc. All rights reserved.
 */
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory){
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(15000);
        factory.setReadTimeout(5000);
        return factory;
    }
}
