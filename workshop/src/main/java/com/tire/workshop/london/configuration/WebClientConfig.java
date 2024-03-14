package com.tire.workshop.london.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.xml.Jaxb2XmlDecoder;
import org.springframework.http.codec.xml.Jaxb2XmlEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration()
public class WebClientConfig {

    @Value("${london-service-url}")
    private String url;

    @Bean()
    public WebClient webClient() {
        return WebClient
                .builder()
                .baseUrl(url)
                .exchangeStrategies(ExchangeStrategies.builder().codecs((configurer) -> {
                    configurer.defaultCodecs().jaxb2Encoder(new Jaxb2XmlEncoder());
                    configurer.defaultCodecs().jaxb2Decoder(new Jaxb2XmlDecoder());
                }).build())
                .build();
    }
}
