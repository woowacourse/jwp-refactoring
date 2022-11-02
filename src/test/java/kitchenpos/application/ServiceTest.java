package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Sql("classpath:truncate.sql")
@Transactional
public class ServiceTest {

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected TableGroupService tableGroupService;

    @Autowired
    protected TableService tableService;

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected OrderDao orderDao;

    @Autowired
    protected OrderTableDao orderTableDao;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected TableGroupDao tableGroupDao;
}
