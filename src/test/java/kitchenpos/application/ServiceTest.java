package kitchenpos.application;

import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.ordertable.OrderTableRepository;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.tablegroup.TableGroupRepository;
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
