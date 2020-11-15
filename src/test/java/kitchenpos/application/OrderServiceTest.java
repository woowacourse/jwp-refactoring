package kitchenpos.application;

import static kitchenpos.KitchenposTestHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.IsolatedTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

class OrderServiceTest extends IsolatedTest {

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

    @Autowired
    private OrderDao orderDao;

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrderByValidInput() {
        BigDecimal price = BigDecimal.valueOf(10000L);
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "한마리 치킨"));
        Product product = productDao.save(createProduct(null, "후라이드 치킨", price));
        MenuProduct menuProduct = createMenuProduct(null, null, product.getId(), 1);
        Menu menu = menuDao.save(createMenu(null, "후라이드 치킨", price, menuGroup.getId(),
            Collections.singletonList(menuProduct)));
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 0, false));
        OrderLineItem orderLineItem1 = createOrderLineItem(null, null, menu.getId(), 1);
        Order order = orderService.create(
            createOrder(null, orderTable.getId(), null, null, Arrays.asList(orderLineItem1)));

        assertAll(
            () -> assertThat(order.getId()).isNotNull(),
            () -> assertThat(order.getOrderTableId()).isEqualTo(orderTable.getId()),
            () -> assertThat(order.getOrderStatus()).isEqualTo("COOKING"),
            () -> assertThat(order.getOrderLineItems()).size().isEqualTo(1)
        );
    }

    @DisplayName("선택된 메뉴 없이 주문은 생성 불가능하다.")
    @Test
    void createOrderByInvalidInputWithEmptyMenu() {
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 0, false));

        assertThatThrownBy(
            () -> orderService.create(createOrder(null, orderTable.getId(), null, null, Collections.EMPTY_LIST)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 메뉴로 주문은 불가능하다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createOrderByInvalidInputWithNotExistingMenu(String value) {
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 0, false));
        OrderLineItem orderLineItem1 = createOrderLineItem(null, null, Objects.isNull(value) ? null : 1L, 1);

        assertThatThrownBy(
            () -> orderService.create(createOrder(null, orderTable.getId(), null, null, Arrays.asList(orderLineItem1))))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 없으면 주문이 불가능하다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createOrderByInvalidInputWithNotExistingTable(String value) {
        BigDecimal price = BigDecimal.valueOf(10000L);
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "한마리 치킨"));
        Product product = productDao.save(createProduct(null, "후라이드 치킨", price));
        MenuProduct menuProduct = createMenuProduct(null, null, product.getId(), 1);
        Menu menu = menuDao.save(createMenu(null, "후라이드 치킨", price, menuGroup.getId(),
            Collections.singletonList(menuProduct)));
        OrderLineItem orderLineItem1 = createOrderLineItem(null, null, menu.getId(), 1);

        assertThatThrownBy(() -> orderService.create(
            createOrder(null, Objects.isNull(value) ? null : 1L, null, null, Arrays.asList(orderLineItem1))))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 비어있으면 주문이 불가능하다.")
    @Test
    void createOrderByInvalidInputWithEmptyTable() {
        BigDecimal price = BigDecimal.valueOf(10000L);
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "한마리 치킨"));
        Product product = productDao.save(createProduct(null, "후라이드 치킨", price));
        MenuProduct menuProduct = createMenuProduct(null, null, product.getId(), 1);
        Menu menu = menuDao.save(createMenu(null, "후라이드 치킨", price, menuGroup.getId(),
            Collections.singletonList(menuProduct)));
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 0, true));
        OrderLineItem orderLineItem1 = createOrderLineItem(null, null, menu.getId(), 1);

        assertThatThrownBy(
            () -> orderService.create(createOrder(null, orderTable.getId(), null, null, Arrays.asList(orderLineItem1))))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 모두 불러올 수 있다.")
    @Test
    void findAll() {
        BigDecimal price = BigDecimal.valueOf(10000L);
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "한마리 치킨"));
        Product product = productDao.save(createProduct(null, "후라이드 치킨", price));
        MenuProduct menuProduct = createMenuProduct(null, null, product.getId(), 1);
        Menu menu = menuDao.save(createMenu(null, "후라이드 치킨", price, menuGroup.getId(),
            Collections.singletonList(menuProduct)));
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 0, false));
        OrderLineItem orderLineItem1 = createOrderLineItem(null, null, menu.getId(), 1);
        orderService.create(createOrder(null, orderTable.getId(), null, null, Arrays.asList(orderLineItem1)));

        List<Order> orders = orderService.list();

        assertThat(orders).size().isEqualTo(1);
    }

    @DisplayName("주문의 상태를 변경할 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = {"MEAL", "COMPLETION"})
    void changeOrderStatusByValidInput(String value) {
        BigDecimal price = BigDecimal.valueOf(10000L);
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "한마리 치킨"));
        Product product = productDao.save(createProduct(null, "후라이드 치킨", price));
        MenuProduct menuProduct = createMenuProduct(null, null, product.getId(), 1);
        Menu menu = menuDao.save(createMenu(null, "후라이드 치킨", price, menuGroup.getId(),
            Collections.singletonList(menuProduct)));
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 0, false));
        OrderLineItem orderLineItem1 = createOrderLineItem(null, null, menu.getId(), 1);
        Order savedOrder = orderService.create(
            createOrder(null, orderTable.getId(), null, null, Arrays.asList(orderLineItem1)));

        orderService.changeOrderStatus(savedOrder.getId(),
            createOrder(null, savedOrder.getOrderTableId(), value, null, savedOrder.getOrderLineItems()));

        Order foundOrder = orderDao.findById(savedOrder.getId()).orElseThrow(IllegalArgumentException::new);

        assertThat(foundOrder.getOrderStatus()).isEqualTo(value);

    }

    @DisplayName("주문 완료 상태에서는 더이상 주문 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatusByInValidInputWithOrderStatus() {
        BigDecimal price = BigDecimal.valueOf(10000L);
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "한마리 치킨"));
        Product product = productDao.save(createProduct(null, "후라이드 치킨", price));
        MenuProduct menuProduct = createMenuProduct(null, null, product.getId(), 1);
        Menu menu = menuDao.save(createMenu(null, "후라이드 치킨", price, menuGroup.getId(),
            Collections.singletonList(menuProduct)));
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 0, false));
        OrderLineItem orderLineItem1 = createOrderLineItem(null, null, menu.getId(), 1);
        Order savedOrder = orderService.create(
            createOrder(null, orderTable.getId(), null, null, Arrays.asList(orderLineItem1)));

        orderService.changeOrderStatus(savedOrder.getId(),
            createOrder(null, savedOrder.getOrderTableId(), "COMPLETION", null, savedOrder.getOrderLineItems()));

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(),
            createOrder(null, savedOrder.getOrderTableId(), "COOKING", null, savedOrder.getOrderLineItems())))
            .isInstanceOf(IllegalArgumentException.class);

    }
}