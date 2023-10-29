package kitchenpos.integrationtest;

import io.restassured.RestAssured;
import kitchenpos.steps.IntegrationTestSteps;
import kitchenpos.steps.SharedContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
abstract class IntegrationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    DatabaseSetter databaseSetter;

    @Autowired
    IntegrationTestSteps steps;

    @Autowired
    SharedContext sharedContext;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseSetter.setUp();
    }
}
