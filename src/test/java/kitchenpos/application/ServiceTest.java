package kitchenpos.application;

import kitchenpos.common.DataBaseCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTest {

    @Autowired
    private DataBaseCleaner dataBaseCleaner;

    @BeforeEach
    void beforeEach() {
        dataBaseCleaner.clear();
    }
}
