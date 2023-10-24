package kitchenpos.application;

import javax.transaction.Transactional;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.repository.TableGroupRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
@Transactional
@SpringBootTest
@DisplayNameGeneration(ReplaceUnderscores.class)
public class ServiceTest {

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected OrderTableRepository orderTableRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected MenuProductRepository menuProductRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected OrderLineItemRepository orderLineItemRepository;

    @Autowired
    protected TableGroupRepository tableGroupRepository;

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected TableService tableService;

    @Autowired
    protected TableGroupService tableGroupService;
}
