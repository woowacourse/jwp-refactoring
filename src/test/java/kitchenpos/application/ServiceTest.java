package kitchenpos.application;

import kitchenpos.dao.menu.MenuDao;
import kitchenpos.dao.menugroup.MenuGroupDao;
import kitchenpos.dao.order.OrderDao;
import kitchenpos.dao.orderlineitem.OrderLineItemDao;
import kitchenpos.dao.ordertable.OrderTableDao;
import kitchenpos.dao.product.ProductDao;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ServiceTest {

    @MockBean(name = "productDao")
    protected ProductDao productDao;

    @MockBean(name = "menuGroupDao")
    protected MenuGroupDao menuGroupDao;

    @MockBean(name = "menuDao")
    protected MenuDao menuDao;

    @MockBean(name = "orderTableDao")
    protected OrderTableDao orderTableDao;

    @MockBean(name = "orderDao")
    protected OrderDao orderDao;

    @MockBean(name = "orderLineItemDao")
    protected OrderLineItemDao orderLineItemDao;
}
