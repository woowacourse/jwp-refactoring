package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
abstract class ServiceTest {

    @Autowired
    protected ProductService productService;

    @Autowired
    protected ProductDao productDao;
}
