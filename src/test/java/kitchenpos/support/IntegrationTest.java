package kitchenpos.support;

import kitchenpos.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class IntegrationTest {

    @Autowired
    protected ProductDao productDao;
}
