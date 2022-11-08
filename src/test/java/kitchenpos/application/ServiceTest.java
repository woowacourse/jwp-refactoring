package kitchenpos.application;

import java.util.List;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public abstract class ServiceTest {

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
    protected TableGroupDao tableGroupDao;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected MenuGroupDao menuGroupDao;

    @Autowired
    protected MenuProductDao menuProductDao;

    @Autowired
    protected OrderLineItemDao orderLineItemDao;

    @Autowired
    protected OrderTableDao orderTableDao;

    @Autowired
    protected ProductDao productDao;

    protected Product 상품등록(final Product product) {
        return productDao.save(product);
    }

    protected MenuGroup 메뉴그룹등록(final MenuGroup menuGroup) {
        return menuGroupDao.save(menuGroup);
    }

    protected Menu 메뉴등록(final Menu menu) {
        return menuRepository.save(menu);
    }

    protected OrderTable 테이블등록(final OrderTable orderTable) {
        return orderTableDao.save(orderTable);
    }

    protected TableGroup 단체지정(final TableGroup tableGroup) {
        final List<OrderTable> orderTables = tableGroup.getOrderTables();
        final TableGroup group = tableGroupDao.save(tableGroup)
                .group(orderTables);

        orderTableDao.updateAll(group.getOrderTables());
        return group;
    }

    protected Order 주문등록(final Order order) {
        final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow();
        orderTableDao.save(orderTable.order());
        return orderRepository.save(order);
    }
}
