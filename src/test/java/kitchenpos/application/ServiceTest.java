package kitchenpos.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;

@SpringBootTest
public class ServiceTest {

    @Autowired
    protected ProductService productService;
    @Autowired
    protected MenuService menuService;
    @Autowired
    protected MenuGroupService menuGroupService;
    @Autowired
    protected TableService tableService;
    @Autowired
    protected TableGroupService tableGroupService;
    @Autowired
    protected OrderService orderService;

    @Autowired
    protected MenuGroupDao menuGroupDao;
    @Autowired
    protected ProductDao productDao;
    @Autowired
    protected OrderDao orderDao;
    @Autowired
    protected OrderLineItemDao orderLineItemDao;
    @Autowired
    protected OrderTableDao orderTableDao;
    @Autowired
    protected TableGroupDao tableGroupDao;
    @Autowired
    protected MenuDao menuDao;

}
