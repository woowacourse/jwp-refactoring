package kitchenpos.integration;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.net.URI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private DataSource dataSource;

    private final ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();

    @AfterEach
    void tearDown() {
        databasePopulator.addScript(new ClassPathResource("truncate.sql"));
        databasePopulator.addScript(new ClassPathResource("/db/migration/V2__Insert_default_data.sql"));
        databasePopulator.execute(dataSource);
    }

    protected <T> ResponseEntity<T> get(String uri, ParameterizedTypeReference<T> parameterizedTypeReference) {
        return testRestTemplate.exchange(
                uri,
                HttpMethod.GET,
                emptyHttpEntity(),
                parameterizedTypeReference
        );
    }

    private HttpEntity<Void> emptyHttpEntity() {
        return new HttpEntity<>(
                new HttpHeaders()
        );
    }

    protected <T> ResponseEntity<T> post(String uri, HttpHeaders httpHeaders, Object requestBody,
                                         ParameterizedTypeReference<T> parameterizedTypeReference) {

        return testRestTemplate.exchange(
                uri,
                HttpMethod.POST,
                new HttpEntity<>(requestBody, httpHeaders),
                parameterizedTypeReference
        );
    }

    protected String extractLocation(ResponseEntity<?> responseEntity) {
        URI location = responseEntity.getHeaders().getLocation();
        return location.getPath();
    }
}
