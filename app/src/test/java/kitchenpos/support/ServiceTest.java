package kitchenpos.support;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.order.application.OrderService;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.product.application.ProductService;
import kitchenpos.tablegroup.application.TableGroupService;
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
