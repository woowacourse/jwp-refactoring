package kitchenpos.application;

import javax.transaction.Transactional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
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
    protected OrderTableDao orderTableDao;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected MenuProductRepository menuProductRepository;

    @Autowired
    protected OrderDao orderDao;

    @Autowired
    protected OrderLineItemDao orderLineItemDao;

    @Autowired
    protected TableGroupDao tableGroupDao;

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
