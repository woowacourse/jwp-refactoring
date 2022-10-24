package kitchenpos.application;

import static kitchenpos.fixture.OrderFixture.forUpdateStatus;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
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
    protected MenuDao menuDao;

    @Autowired
    protected MenuGroupDao menuGroupDao;

    @Autowired
    protected MenuProductDao menuProductDao;

    @Autowired
    protected OrderDao orderDao;

    @Autowired
    protected OrderLineItemDao orderLineItemDao;

    @Autowired
    protected OrderTableDao orderTableDao;

    @Autowired
    protected ProductDao productDao;

    @Autowired
    protected TableGroupDao tableGroupDao;

    protected Product 상품등록(final Product product) {
        return productDao.save(product);
    }

    protected MenuGroup 메뉴그룹등록(final MenuGroup menuGroup) {
        return menuGroupDao.save(menuGroup);
    }

    protected Menu 메뉴등록(final Menu menu) {
        return menuService.create(menu);
    }

    protected OrderTable 주문테이블등록(final OrderTable orderTable) {
        return orderTableDao.save(orderTable);
    }

    protected TableGroup 테이블그룹등록(final TableGroup tableGroup) {
        return tableGroupService.create(tableGroup);
    }

    protected Order 주문등록(final Order order) {
        return orderService.create(order);
    }

    protected void 주문상태변경(final Order order, final OrderStatus orderStatus) {
        orderService.changeOrderStatus(order.getId(), forUpdateStatus(orderStatus));
    }
}
