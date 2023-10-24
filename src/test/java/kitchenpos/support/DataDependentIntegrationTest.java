package kitchenpos.support;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DataDependentIntegrationTest {

    @Autowired
    private DataCleaner dataCleaner;

    @BeforeEach
    void setUp() {
        dataCleaner.clear();
    }
}
