package kitchenpos.core.application;

import kitchenpos.DatabaseCleaner;
import kitchenpos.core.Product.application.ProductService;
import kitchenpos.core.menu.application.MenuService;
import kitchenpos.core.menugroup.application.MenuGroupService;
import kitchenpos.core.order.application.OrderService;
import kitchenpos.core.table.application.OrderTableService;
import kitchenpos.core.table.application.TableGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Transactional
@SpringBootTest
public class ServiceTest {

    @Autowired
    protected DatabaseCleaner databaseCleaner;

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected TableGroupService tableGroupService;

    @Autowired
    protected OrderTableService tableService;

    @BeforeEach
    void cleanTables() throws SQLException {
        databaseCleaner.clear();
    }
}
