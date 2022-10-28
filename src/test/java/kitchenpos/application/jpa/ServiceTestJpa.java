package kitchenpos.application.jpa;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTestJpa {

    @Autowired
    private DatabaseCleanerJpa databaseCleaner;

    @BeforeEach
    void cleanUp() {
        databaseCleaner.clear();
        databaseCleaner.insertInitialData();
    }
}
