package kitchenpos.ui;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public abstract class ControllerTest {

    private static final String ORIGIN = "http://localhost:8080";
    private static final TestRestTemplate restTemplate = new TestRestTemplate();

    protected <T> ResponseEntity<T> get(final String url, final Class<T> clazz) {
        return restTemplate.getForEntity(url, clazz);
    }

    protected <T> ResponseEntity<T> post(final String url, final Object request, final Class<T> clazz) {
        return restTemplate.postForEntity(url, request, clazz);
    }

    protected String url(final String url) {
        return ORIGIN + url;
    }
}
