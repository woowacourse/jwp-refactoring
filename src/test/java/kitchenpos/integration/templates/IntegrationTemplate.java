package kitchenpos.integration.templates;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class IntegrationTemplate {

    private final TestRestTemplate testRestTemplate;

    public IntegrationTemplate(TestRestTemplate testRestTemplate) {
        this.testRestTemplate = testRestTemplate;
    }

    public <T> ResponseEntity<T> get(String url, Class<T> responseType) {
        return testRestTemplate.getForEntity(
            url,
            responseType
        );
    }

    public <T, P> ResponseEntity<P> post(String url, T request, Class<P> responseType) {
        return testRestTemplate.postForEntity(
            url,
            request,
            responseType
        );
    }

    public <T, P> ResponseEntity<T> put(String url, P pathVariable, T request,
                                        Class<T> responseType) {
        return testRestTemplate.exchange(
            url,
            HttpMethod.PUT,
            new HttpEntity<>(request),
            responseType,
            pathVariable
        );
    }

    public <T> ResponseEntity<Void> delete(String url, T pathVariable) {
        return testRestTemplate.exchange(
            url,
            HttpMethod.DELETE,
            HttpEntity.EMPTY,
            Void.class,
            pathVariable
        );
    }
}
