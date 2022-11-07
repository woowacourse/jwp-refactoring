package kitchenpos.support;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.support.repository.OrderMenuRepository;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("classpath:truncate.sql")
@SpringBootTest
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
    protected ProductRepository productRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected OrderTableRepository orderTableRepository;

    @Autowired
    protected TableGroupRepository tableGroupRepository;

    @Autowired
    protected OrderMenuRepository orderMenuRepository;
}
