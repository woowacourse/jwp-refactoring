package kitchenpos.application;

import kitchenpos.persistence.MenuGroupRepository;
import kitchenpos.persistence.MenuProductRepository;
import kitchenpos.persistence.MenuRepository;
import kitchenpos.persistence.OrderLineItemRepository;
import kitchenpos.persistence.OrderRepository;
import kitchenpos.persistence.OrderTableRepository;
import kitchenpos.persistence.ProductRepository;
import kitchenpos.persistence.TableGroupRepository;
import kitchenpos.supports.DatabaseCleaner;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.TestExecutionListeners;

@DisplayNameGeneration(ReplaceUnderscores.class)
@TestConstructor(autowireMode = AutowireMode.ALL)
@SpringBootTest
@TestExecutionListeners(
        value = DatabaseCleaner.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
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
    protected MenuProductRepository menuProductRepository;
    @Autowired
    protected OrderRepository orderRepository;
    @Autowired
    protected OrderLineItemRepository orderLineItemRepository;
    @Autowired
    protected TableGroupRepository tableGroupRepository;
    @Autowired
    protected OrderTableRepository orderTableRepository;
}
