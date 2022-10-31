package kitchenpos.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.repository.TableGroupRepository;

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
    protected MenuGroupRepository menuGroupRepository;
    @Autowired
    protected ProductRepository productRepository;
    @Autowired
    protected OrderRepository orderRepository;
    @Autowired
    protected OrderLineItemRepository orderLineItemRepository;
    @Autowired
    protected OrderTableRepository orderTableRepository;
    @Autowired
    protected TableGroupRepository tableGroupRepository;
    @Autowired
    protected MenuRepository menuRepository;

}
