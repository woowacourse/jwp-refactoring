package kitchenpos.application;

import kitchenpos.menu.repository.dao.MenuDao;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.TableGroupService;
import kitchenpos.order.application.TableService;
import kitchenpos.order.repository.dao.OrderDao;
import kitchenpos.order.repository.dao.OrderLineItemDao;
import kitchenpos.order.repository.dao.OrderTableDao;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.product.repository.ProductDao;
import kitchenpos.order.repository.dao.TableGroupDao;
import kitchenpos.product.application.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ServiceTest {

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
    protected TableService tableService;

    @Autowired
    protected MenuDao menuDao;

    @Autowired
    protected ProductDao productDao;

    @Autowired
    protected OrderDao orderDao;

    @Autowired
    protected OrderTableDao orderTableDao;

    @Autowired
    protected OrderLineItemDao orderLineItemDao;

    @Autowired
    protected TableGroupDao tableGroupDao;
}
