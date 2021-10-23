package kitchenpos.ui;

import javax.sql.DataSource;
import kitchenpos.common.DatabaseCleaner;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("test")
@Sql(value = "classpath:/test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class IntegrationTest {

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected DataSource dataSource;

    @Autowired
    protected DatabaseCleaner databaseCleaner;

    @AfterEach
    void tearDown() {
        databaseCleaner.execute();
    }
}
