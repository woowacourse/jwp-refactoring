package kitchenpos.support;

import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.application.ProductService;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ServiceTest {

    @Autowired
    protected ProductService productService;
    @Autowired
    protected MenuService menuService;
    @Autowired
    protected MenuGroupService menuGroupService;
    @Autowired
    protected OrderService orderService;
    @Autowired
    protected TableService tableService;
    @Autowired
    protected TableGroupService tableGroupService;


    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    public void tearDown() {
        databaseCleaner.execute();
    }
}
