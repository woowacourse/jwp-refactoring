package kitchenpos.application;

import kitchenpos.common.DatabaseInitializer;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(DatabaseInitializer.class)
public class ServiceTest {

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected TableService tableService;

    @Autowired
    protected TableGroupService tableGroupService;

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected OrderTableRepository orderTableRepository;

    @Autowired
    protected TableGroupRepository tableGroupRepository;
}
