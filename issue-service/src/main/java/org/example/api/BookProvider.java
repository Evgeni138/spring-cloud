package org.example.api;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import lombok.Data;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class BookProvider {

    // HttpClient - java.net
    // RestTemplate - spring.web
    // WebClient - spring.react

    public final WebClient webClient;
//    public final EurekaClient eurekaClient;

    public BookProvider(EurekaClient eurekaClient,
                        ReactorLoadBalancerExchangeFilterFunction loadBalancerExchangeFilterFunction) {
        webClient = WebClient.builder()
                .filter(loadBalancerExchangeFilterFunction)
                .build();
//        this.eurekaClient = eurekaClient;
    }
    public UUID getRandomBookId() {
        // Цель: вызвать GET http://localhost:8180/api/book/random, получить id и вернуть.
        BookResponse bookResponse = webClient.get()
//                .uri(getBookServiceIp() + "/api/book/random")
                .uri("http://book-service/api/book/random")
                .retrieve()
                .bodyToMono(BookResponse.class)
                .block();

        return bookResponse.getId();
    }

//    private String getBookServiceIp() {
//        Application application = eurekaClient.getApplication("BOOK-SERVICE");
//        List<InstanceInfo> instances = application.getInstances();
//
//        int randomIndex = ThreadLocalRandom.current().nextInt(instances.size());
//        InstanceInfo randomInstance = instances.get(randomIndex);
//        return "http://" + randomInstance.getIPAddr() + ":" + randomInstance.getPort();
//    }

    @Data
    private static class BookResponse {
        private UUID id;
    }

}
