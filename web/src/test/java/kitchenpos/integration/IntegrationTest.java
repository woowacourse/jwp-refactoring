package kitchenpos.integration;

import kitchenpos.util.DatabaseCleaner;
import kitchenpos.util.FixtureFactory;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class IntegrationTest {

    @Autowired
    protected FixtureFactory fixtureFactory;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {
        databaseCleaner.clean();
    }
}
