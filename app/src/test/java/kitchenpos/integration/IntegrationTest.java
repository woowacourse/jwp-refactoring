package kitchenpos.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.integration.helper.DatabaseCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("test")
class IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void tearDown() {
        databaseCleaner.execute();
    }
}
