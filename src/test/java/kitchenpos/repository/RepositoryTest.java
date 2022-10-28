package kitchenpos.repository;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RepositoryTest {

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected TableGroupRepository tableGroupRepository;

    @Autowired
    protected TableRepository tableRepository;

    @Autowired
    protected MenuGroupDao menuGroupDao;

    @Autowired
    protected MenuDao menuDao;

    @Autowired
    protected OrderDao orderDao;

    @Autowired
    protected OrderTableDao orderTableDao;

    @Autowired
    protected ProductDao productDao;

    @Autowired
    protected TableGroupDao tableGroupDao;
}
