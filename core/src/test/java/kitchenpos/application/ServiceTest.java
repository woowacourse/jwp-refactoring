package kitchenpos.application;

import kitchenpos.DatabaseCleaner;
import kitchenpos.Product.application.ProductService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.order.application.OrderService;
import kitchenpos.table.application.OrderTableService;
import kitchenpos.table.application.TableGroupService;
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
