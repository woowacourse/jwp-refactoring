package kitchenpos;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public abstract class ServiceTestDeprecate {

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected OrderTableRepository orderTableRepository;

    @Autowired
    protected TableGroupRepository tableGroupRepository;


}
