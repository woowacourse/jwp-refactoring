package kitchenpos.application;

import javax.sql.DataSource;
import kitchenpos.DatabaseCleaner;
import kitchenpos.Fixtures;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.order.application.OrderService;
import kitchenpos.product.application.ProductService;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.TableService;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.table.repository.TableGroupRepository;
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
    ProductRepository productRepository;

    @Autowired
    MenuProductRepository menuProductRepository;

    @Autowired
    TableGroupRepository tableGroupRepository;

    @Autowired
    OrderTableRepository orderTableRepository;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    DataSource dataSource;

    @Autowired
    DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setup() {
        databaseCleaner.clear();
    }
}
