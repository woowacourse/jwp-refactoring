package kitchenpos.application;

import javax.sql.DataSource;
import kitchenpos.DatabaseCleaner;
import kitchenpos.Fixtures;
import kitchenpos.application.menu.MenuGroupService;
import kitchenpos.application.menu.MenuService;
import kitchenpos.application.order.OrderService;
import kitchenpos.application.product.ProductService;
import kitchenpos.application.table.TableGroupService;
import kitchenpos.application.table.TableService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class ServiceTest extends Fixtures {

    @Autowired
    MenuGroupService menuGroupService;

    @Autowired
    MenuService menuService;

    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;

    @Autowired
    TableService tableService;

    @Autowired
    TableGroupService tableGroupService;

    @Autowired
    ProductDao productDao;

    @Autowired
    MenuProductDao menuProductDao;

    @Autowired
    TableGroupDao tableGroupDao;

    @Autowired
    OrderTableDao orderTableDao;

    @Autowired
    MenuGroupDao menuGroupDao;

    @Autowired
    MenuDao menuDao;

    @Autowired
    DataSource dataSource;

    @Autowired
    DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setup() {
        databaseCleaner.clear();
    }
}
