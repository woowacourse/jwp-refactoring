package kitchenpos.support;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DataDependentIntegrationTest {

    @Autowired
    private DataCleaner dataCleaner;

    @AfterEach
    void tearDown() {
        dataCleaner.clear();
    }
}
