package kitchenpos.application;

import kitchenpos.menuGroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tableGroup.domain.TableGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@ActiveProfiles("test")
@Sql("/data.sql")
public abstract class ServiceBaseTest {

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected OrderTableRepository orderTableRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected TableGroupRepository tableGroupRepository;
}
