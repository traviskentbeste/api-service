package com.walletsquire.apiservice.cucumber;

import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class CucumberHttpClient {

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity getAll(final String url, final String endpoint) {
        return restTemplate.getForEntity(url + ':' + port + endpoint, String.class);
    }

    public <T> T get(final String url, final String endpoint, Class<T> classOfT) {
        return restTemplate.getForEntity(url + ':' + port + endpoint, classOfT).getBody();
    }

    public int put(final String url, final String endpoint) {
        return restTemplate.postForEntity(url + ':' + port, endpoint, Void.class).getStatusCodeValue();
    }

    public void delete(final String url, final String endpoint, final Long id) {
        restTemplate.delete(url + ':' + port + endpoint + '/' + id);
    }

}