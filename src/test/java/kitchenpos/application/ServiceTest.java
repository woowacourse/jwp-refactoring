package kitchenpos.application;

import javax.sql.DataSource;
import kitchenpos.DatabaseCleanner;
import kitchenpos.Fixtures;
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
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
    DatabaseCleanner databaseCleanner;

    @BeforeEach
    void setup() {
        databaseCleanner.clear();
        menuGroupDao.save(메뉴그룹_한마리메뉴());
        productDao.save(상품_후라이드());
        menuDao.save(메뉴_후라이드치킨());
    }
}
