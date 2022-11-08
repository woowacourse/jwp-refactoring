package kitchenpos.common;

import kitchenpos.core.menu.domain.MenuDao;
import kitchenpos.core.menugroup.domain.MenuGroupDao;
import kitchenpos.core.order.domain.OrderDao;
import kitchenpos.core.order.domain.OrderLineItemDao;
import kitchenpos.core.table.domain.OrderTableDao;
import kitchenpos.core.product.domain.ProductDao;
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
