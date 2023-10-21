package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.table.OrderTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public abstract class ServiceTest {

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected ProductDao productDao;

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected OrderTableRepository orderTableRepository;

    @Autowired
    protected TableGroupDao tableGroupDao;


}
