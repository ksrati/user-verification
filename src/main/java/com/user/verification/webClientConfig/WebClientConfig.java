package com.user.verification.webClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    @Bean(name = "randomUserWebClient")
    public WebClient randomUserWebClient() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(java.time.Duration.ofMillis(2000))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new io.netty.handler.timeout.ReadTimeoutHandler(2))
                        .addHandlerLast(new io.netty.handler.timeout.WriteTimeoutHandler(2)));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("https://randomuser.me/api/")
                .build();
    }

    @Bean(name = "nationalizeWebClient")
    public WebClient nationalizeWebClient() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(java.time.Duration.ofMillis(1000))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new io.netty.handler.timeout.ReadTimeoutHandler(1))
                        .addHandlerLast(new io.netty.handler.timeout.WriteTimeoutHandler(1)));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("https://api.nationalize.io/")
                .build();
    }

    @Bean(name = "genderizeWebClient")
    public WebClient genderizeWebClient() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(java.time.Duration.ofMillis(1000))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new io.netty.handler.timeout.ReadTimeoutHandler(1))
                        .addHandlerLast(new io.netty.handler.timeout.WriteTimeoutHandler(1)));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("https://api.genderize.io/")
                .build();
    }
}
