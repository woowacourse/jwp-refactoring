package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
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
