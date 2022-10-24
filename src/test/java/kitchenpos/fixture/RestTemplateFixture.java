package kitchenpos.fixture;

import org.springframework.boot.test.web.client.TestRestTemplate;

public class RestTemplateFixture {

    public static TestRestTemplate create() {
        return new TestRestTemplate();
    }
}
