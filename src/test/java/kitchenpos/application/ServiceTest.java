package kitchenpos.application;

import static kitchenpos.fixture.dto.OrderDtoFixture.forUpdateStatus;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.dto.response.TableGroupResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Qualifier("menuRepository")
    @Autowired
    protected MenuDao menuDao;

    @Autowired
    protected MenuGroupDao menuGroupDao;

    @Autowired
    protected MenuProductDao menuProductDao;

    @Qualifier("orderRepository")
    @Autowired
    protected OrderDao orderDao;

    @Autowired
    protected OrderLineItemDao orderLineItemDao;

    @Autowired
    protected OrderTableDao orderTableDao;

    @Autowired
    protected ProductDao productDao;

    @Qualifier("tableGroupRepository")
    @Autowired
    protected TableGroupDao tableGroupDao;

    protected Product 상품등록(final Product product) {
        return productDao.save(product);
    }

    protected MenuGroup 메뉴그룹등록(final MenuGroup menuGroup) {
        return menuGroupDao.save(menuGroup);
    }

    protected MenuResponse 메뉴등록(final MenuRequest menu) {
        return menuService.create(menu);
    }

    protected OrderTable 테이블등록(final OrderTable orderTable) {
        return orderTableDao.save(orderTable);
    }

    protected TableGroupResponse 단체지정(final TableGroupRequest tableGroup) {
        return tableGroupService.create(tableGroup);
    }

    protected OrderResponse 주문등록(final OrderRequest order) {
        return orderService.create(order);
    }

    protected void 주문상태변경(final Long orderId, final OrderStatus orderStatus) {
        orderService.changeOrderStatus(orderId, forUpdateStatus(orderStatus));
    }
}
