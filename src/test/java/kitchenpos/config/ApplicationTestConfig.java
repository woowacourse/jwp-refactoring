package kitchenpos.config;

import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.OrderMapper;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.table.domain.OrderTableValidator;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.domain.TableGroupValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
@Import({JpaAuditingConfig.class, OrderValidator.class, OrderMapper.class, OrderTableValidator.class, TableGroupValidator.class})
@DataJpaTest
public abstract class ApplicationTestConfig {

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected MenuProductRepository menuProductRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected OrderValidator orderValidator;

    @Autowired
    protected OrderMapper orderMapper;

    @Autowired
    protected OrderLineItemRepository orderLineItemRepository;

    @Autowired
    protected OrderTableRepository orderTableRepository;

    @Autowired
    protected OrderTableValidator orderTableValidator;

    @Autowired
    protected TableGroupRepository tableGroupRepository;

    @Autowired
    protected TableGroupValidator tableGroupValidator;

    @Autowired
    protected ProductRepository productRepository;
}
