package kitchenpos.application;

import kitchenpos.application.menu.MenuGroupService;
import kitchenpos.application.menu.MenuService;
import kitchenpos.application.order.OrderService;
import kitchenpos.application.product.ProductService;
import kitchenpos.application.table.TableGroupService;
import kitchenpos.application.table.TableService;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.TableGroupRepository;
import org.assertj.core.data.Percentage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ServiceTest {

    protected static final Percentage HUNDRED_PERCENT = Percentage.withPercentage(100);

    @Autowired
    protected ProductService productService;

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected TableService tableService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected TableGroupService tableGroupService;

    @Autowired
    protected OrderTableRepository orderTableRepository;

    @Autowired
    protected TableGroupRepository tableGroupRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected MenuRepository menuRepository;
}
