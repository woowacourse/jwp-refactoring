package kitchenpos.supports;

import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.service.MenuGroupService;
import kitchenpos.service.MenuService;
import kitchenpos.service.OrderService;
import kitchenpos.service.ProductService;
import kitchenpos.service.TableGroupService;
import kitchenpos.service.TableService;
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
public abstract class ServiceTestContext {

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
