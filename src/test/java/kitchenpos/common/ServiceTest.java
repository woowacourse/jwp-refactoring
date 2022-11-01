package kitchenpos.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.TableGroupService;
import kitchenpos.order.application.TableService;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.dao.TableGroupDao;
import kitchenpos.product.application.ProductService;

@SpringBootTest
@Sql("classpath:truncate.sql")
public class ServiceTest {

    protected static Long NO_ID = null;

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected OrderDao orderDao;

    @Autowired
    protected TableService tableService;

    @Autowired
    protected TableGroupService tableGroupService;

    @Autowired
    protected TableGroupDao tableGroupDao;

    @Autowired
    protected OrderTableDao orderTableDao;
}
