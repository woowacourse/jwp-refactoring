package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public abstract class ServiceTest {

    @MockBean
    protected MenuDao menuDao;
    @MockBean
    protected MenuGroupDao menuGroupDao;
    @MockBean
    protected MenuProductDao menuProductDao;
    @MockBean
    protected OrderDao orderDao;
    @MockBean
    protected OrderLineItemDao orderLineItemDao;
    @MockBean
    protected OrderTableDao orderTableDao;
    @MockBean
    protected ProductDao productDao;
    @MockBean
    protected TableGroupDao tableGroupDao;

}
