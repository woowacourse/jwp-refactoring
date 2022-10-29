package kitchenpos.application;

import static kitchenpos.fixture.dto.OrderDtoFixture.forUpdateStatus;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
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
    protected MenuRepository menuRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected TableGroupRepository tableGroupRepository;

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
        return tableGroupRepository.save(tableGroup);
    }

    protected Order 주문등록(final Order order) {
        return orderRepository.save(order);
    }

    protected void 주문상태변경(final Long orderId, final OrderStatus orderStatus) {
        orderService.changeOrderStatus(orderId, forUpdateStatus(orderStatus));
    }
}
