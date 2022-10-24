package kitchenpos.application;

import javax.transaction.Transactional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class ServiceTest {

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected TableService tableService;

    @Autowired
    protected TableGroupService tableGroupService;

    @Autowired
    protected MenuGroupDao menuGroupDao;

    @Autowired
    protected MenuDao menuDao;

    @Autowired
    protected ProductDao productDao;

    @Autowired
    protected OrderTableDao orderTableDao;

    @Autowired
    protected TableGroupDao tableGroupDao;

    @Autowired
    protected OrderDao orderDao;
}
