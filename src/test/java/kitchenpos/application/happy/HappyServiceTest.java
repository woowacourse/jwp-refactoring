package kitchenpos.application.happy;

import java.sql.SQLException;
import kitchenpos.DatabaseCleaner;
import kitchenpos.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.order.application.OrderService;
import kitchenpos.product.application.ProductService;
import kitchenpos.table.application.TableService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HappyServiceTest {
    @Autowired
    protected DatabaseCleaner databaseCleaner;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected TableService tableService;

    @BeforeEach
    void cleanTables() throws SQLException {
        databaseCleaner.clean();
    }
}
