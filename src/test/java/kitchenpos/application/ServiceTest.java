package kitchenpos.application;

import kitchenpos.application.support.DatabaseCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void cleanUp() {
        databaseCleaner.clear();
        databaseCleaner.insertInitialData();
    }
}
