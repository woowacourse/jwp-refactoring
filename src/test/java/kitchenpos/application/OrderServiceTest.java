package kitchenpos.application;

import static kitchenpos.KitchenposTestHelper.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

class OrderServiceTest extends ServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void create() {
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 2, false));
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "한마리치킨"));
        Product product = productDao.save(
            createProduct(null, "후라이드치킨", BigDecimal.valueOf(18_000)));
        MenuProduct menuProductRequest =
            createMenuProduct(null, null, product.getId(), 1);
        Menu menu = menuDao.save(
            createMenu(null, "후라이드치킨", BigDecimal.valueOf(18_000), menuGroup.getId(),
                Collections.singletonList(menuProductRequest)));
        OrderLineItem orderLineItemRequest =
            createOrderLineItem(null, null, menu.getId(), 1);
        Order orderRequest = createOrder(null, orderTable.getId(), null, null,
            Collections.singletonList(orderLineItemRequest));

        Order order = orderService.create(orderRequest);

        assertThat(order.getId()).isNotNull();
    }

    @DisplayName("존재하지 않는 테이블은 주문할 수 없다.")
    @ParameterizedTest
    @ValueSource(longs = 1)
    @NullSource
    void create_WithNonExistingTable_ThrownException(Long tableId) {
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "한마리치킨"));
        Product product = productDao.save(
            createProduct(null, "후라이드치킨", BigDecimal.valueOf(18_000)));
        MenuProduct menuProductRequest =
            createMenuProduct(null, null, product.getId(), 1);
        Menu menu = menuDao.save(
            createMenu(null, "후라이드치킨", BigDecimal.valueOf(18_000), menuGroup.getId(),
                Collections.singletonList(menuProductRequest)));
        OrderLineItem orderLineItemRequest =
            createOrderLineItem(null, null, menu.getId(), 1);
        Order orderRequest = createOrder(null, tableId, null, null,
            Collections.singletonList(orderLineItemRequest));

        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블은 주문할 수 없다.")
    @Test
    void create_WithEmptyTable_ThrownException() {
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 0, true));
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "한마리치킨"));
        Product product = productDao.save(
            createProduct(null, "후라이드치킨", BigDecimal.valueOf(18_000)));
        MenuProduct menuProductRequest =
            createMenuProduct(null, null, product.getId(), 1);
        Menu menu = menuDao.save(
            createMenu(null, "후라이드치킨", BigDecimal.valueOf(18_000), menuGroup.getId(),
                Collections.singletonList(menuProductRequest)));
        OrderLineItem orderLineItemRequest =
            createOrderLineItem(null, null, menu.getId(), 1);
        Order orderRequest = createOrder(null, orderTable.getId(), null, null,
            Collections.singletonList(orderLineItemRequest));

        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록은 하나 이상이어야 한다.")
    @Test
    void create_WithZeroOrderList_ThrownException() {
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 2, false));
        Order orderRequest = createOrder(null, orderTable.getId(), null, null,
            Collections.EMPTY_LIST);

        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 메뉴로는 주문할 수 없다.")
    @ParameterizedTest
    @ValueSource(longs = 1)
    @NullSource
    void create__ThrownException(Long menuId) {
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 2, false));
        OrderLineItem orderLineItemRequest =
            createOrderLineItem(null, null, menuId, 1);
        Order orderRequest = createOrder(null, orderTable.getId(), null, null,
            Collections.singletonList(orderLineItemRequest));

        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("주문의 목록을 조회할 수 있다.")
    @Test
    void list() {
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 2, false));
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "한마리치킨"));
        Product product = productDao.save(
            createProduct(null, "후라이드치킨", BigDecimal.valueOf(18_000)));
        MenuProduct menuProductRequest =
            createMenuProduct(null, null, product.getId(), 1);
        Menu menu = menuDao.save(
            createMenu(null, "후라이드치킨", BigDecimal.valueOf(18_000), menuGroup.getId(),
                Collections.singletonList(menuProductRequest)));
        OrderLineItem orderLineItemRequest =
            createOrderLineItem(null, null, menu.getId(), 1);
        Order orderRequest = createOrder(null, orderTable.getId(), null, null,
            Collections.singletonList(orderLineItemRequest));
        orderService.create(orderRequest);

        List<Order> list = orderService.list();

        assertThat(list).hasSize(1);
    }
}