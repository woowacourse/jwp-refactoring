package kitchenpos.domain;

import kitchenpos.DatabaseCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DomainTest {
    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void clear() {
        databaseCleaner.clear();
    }
}
