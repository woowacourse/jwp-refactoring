package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.support.extension.DataCleanerExtension;
import kitchenpos.support.extension.DataDefaultAddExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(DataCleanerExtension.class)
@ExtendWith(DataDefaultAddExtension.class)
@SpringBootTest
public abstract class IntegrationTest {

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
    protected TableGroupDao tableGroupDao;

    @Autowired
    protected OrderTableDao orderTableDao;

    @Autowired
    protected OrderDao orderDao;

    @Autowired
    protected OrderLineItemDao orderLineItemDao;
}
