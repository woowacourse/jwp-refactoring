package kitchenpos.acceptance;

import kitchenpos.AcceptanceApplication;
import kitchenpos.utils.DataCleaner;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AcceptanceApplication.class)
public class AcceptanceTest {

    @Autowired
    private DataCleaner dataCleaner;

    @AfterEach
    void tearDown() {
        dataCleaner.clear();
    }
}
