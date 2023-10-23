package kitchenpos.acceptance;

import javax.persistence.EntityManager;
import kitchenpos.utils.DataCleaner;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AcceptanceTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DataCleaner dataCleaner;

    @AfterEach
    void tearDown() {
        dataCleaner.clear();
    }

    protected void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
