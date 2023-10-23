package kitchenpos.supports;

import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.service.MenuService;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.menugroup.service.MenuGroupService;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.service.OrderService;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.product.service.ProductService;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.service.TableService;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import kitchenpos.tablegroup.service.TableGroupService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

@DisplayNameGeneration(ReplaceUnderscores.class)
@TestConstructor(autowireMode = AutowireMode.ALL)
@SpringBootTest
@TestExecutionListeners(
        value = DatabaseCleaner.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@RecordApplicationEvents
public class ServiceTestContext {

    @Autowired
    protected MenuService menuService;
    @Autowired
    protected MenuGroupService menuGroupService;
    @Autowired
    protected OrderService orderService;
    @Autowired
    protected ProductService productService;
    @Autowired
    protected TableService tableService;
    @Autowired
    protected TableGroupService tableGroupService;

    @Autowired
    protected ProductRepository productRepository;
    @Autowired
    protected MenuRepository menuRepository;
    @Autowired
    protected MenuGroupRepository menuGroupRepository;
    @Autowired
    protected OrderRepository orderRepository;
    @Autowired
    protected TableGroupRepository tableGroupRepository;
    @Autowired
    protected OrderTableRepository orderTableRepository;

    @Autowired
    protected ApplicationEvents applicationEvents;
}
