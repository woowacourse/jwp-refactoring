package kitchenpos.integration;

import kitchenpos.support.DatabaseCleaner;
import kitchenpos.support.FixtureFactory;
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
