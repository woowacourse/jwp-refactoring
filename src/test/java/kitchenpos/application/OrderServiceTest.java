package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static kitchenpos.fixture.FixtureFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql(value = "/truncate.sql")
class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("주문 생성")
    @Test
    void create() {
        Product savedProduct = productDao.save(createProduct(null, "터틀치킨", BigDecimal.valueOf(16_000L)));
        MenuGroup savedMenuGroup = menuGroupDao.save(createMenuGroup(null, "치킨"));
        Menu savedMenu = menuDao.save(createMenu(null, savedProduct.getName(), savedProduct.getPrice(), savedMenuGroup.getId(),
                Collections.singletonList(createMenuProduct(null, savedProduct.getId(), 1L))));
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 0, false));
        Order orderRequest = createOrder(null, orderTable.getId(), null, null,
                Collections.singletonList(createOrderLineItem(null, savedMenu.getId(), 1)));

        Order savedOrder = orderService.create(orderRequest);

        assertAll(
                () -> assertThat(savedOrder.getId()).isNotNull(),
                () -> assertThat(savedOrder.getOrderTableId()).isEqualTo(orderRequest.getOrderTableId()),
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(savedOrder.getOrderedTime()).isNotNull(),
                () -> assertThat(savedOrder.getOrderLineItems().size()).isEqualTo(orderRequest.getOrderLineItems().size())
        );
    }

    @DisplayName("주문 항목이 1개 이상 존재하지 않을 경우 예외 발생")
    @Test
    void create_exception1() {
        List<OrderLineItem> invalidOrderLineItems = Collections.emptyList();
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 0, false));
        Order orderRequest = createOrder(null, orderTable.getId(), null, null,
                invalidOrderLineItems);

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목의 메뉴가 존재하지 않을 경우 예외 발생")
    @Test
    void create_exception2() {
        Long invalidMenuId = 0L;
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 0, false));
        Order orderRequest = createOrder(null, orderTable.getId(), null, null,
                Collections.singletonList(createOrderLineItem(null, invalidMenuId, 1)));

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 속한 주문 테이블이 존재하지 않을 경우 예외 발생")
    @Test
    void create_exception3() {
        Product savedProduct = productDao.save(createProduct(null, "터틀치킨", BigDecimal.valueOf(16_000L)));
        MenuGroup savedMenuGroup = menuGroupDao.save(createMenuGroup(null, "치킨"));
        Menu savedMenu = menuDao.save(createMenu(null, savedProduct.getName(), savedProduct.getPrice(), savedMenuGroup.getId(),
                Collections.singletonList(createMenuProduct(null, savedProduct.getId(), 1L))));
        Long invalidOrderTableId = 0L;
        Order orderRequest = createOrder(null, invalidOrderTableId, null, null,
                Collections.singletonList(createOrderLineItem(null, savedMenu.getId(), 1)));

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 속한 주문테이블이 빈 테이블인 경우 예외 발생")
    @Test
    void create_exception4() {
        Product savedProduct = productDao.save(createProduct(null, "터틀치킨", BigDecimal.valueOf(16_000L)));
        MenuGroup savedMenuGroup = menuGroupDao.save(createMenuGroup(null, "치킨"));
        Menu savedMenu = menuDao.save(createMenu(null, savedProduct.getName(), savedProduct.getPrice(), savedMenuGroup.getId(),
                Collections.singletonList(createMenuProduct(null, savedProduct.getId(), 1L))));
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 0, true));
        Order orderRequest = createOrder(null, orderTable.getId(), null, null,
                Collections.singletonList(createOrderLineItem(null, savedMenu.getId(), 1)));

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 조회")
    @Test
    void list() {
        Product savedProduct = productDao.save(createProduct(null, "터틀치킨", BigDecimal.valueOf(16_000L)));
        MenuGroup savedMenuGroup = menuGroupDao.save(createMenuGroup(null, "치킨"));
        Menu savedMenu = menuDao.save(createMenu(null, savedProduct.getName(), savedProduct.getPrice(), savedMenuGroup.getId(),
                Collections.singletonList(createMenuProduct(null, savedProduct.getId(), 1L))));
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 0, false));
        Order orderRequest = createOrder(null, orderTable.getId(), null, null,
                Collections.singletonList(createOrderLineItem(null, savedMenu.getId(), 1)));
        orderService.create(orderRequest);

        List<Order> savedOrders = orderService.list();

        assertThat(savedOrders.size()).isEqualTo(1);
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        Product savedProduct = productDao.save(createProduct(null, "터틀치킨", BigDecimal.valueOf(16_000L)));
        MenuGroup savedMenuGroup = menuGroupDao.save(createMenuGroup(null, "치킨"));
        Menu savedMenu = menuDao.save(createMenu(null, savedProduct.getName(), savedProduct.getPrice(), savedMenuGroup.getId(),
                Collections.singletonList(createMenuProduct(null, savedProduct.getId(), 1L))));
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 0, false));
        Order orderRequest = createOrder(null, orderTable.getId(), null, null,
                Collections.singletonList(createOrderLineItem(null, savedMenu.getId(), 1)));
        Order savedOrder = orderService.create(orderRequest);
        Order changingOrder = createOrder(null, null, OrderStatus.COOKING.name(), null, null);

        Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), changingOrder);

        assertThat(changedOrder.getOrderStatus()).isEqualTo(changingOrder.getOrderStatus());
    }

    @DisplayName("계산 완료인 상태를 변경할 경우 예외 발생")
    @Test
    void changeOrderStatus_exception1() {
        OrderStatus invalidOrderStatus = OrderStatus.COMPLETION;
        Product savedProduct = productDao.save(createProduct(null, "터틀치킨", BigDecimal.valueOf(16_000L)));
        MenuGroup savedMenuGroup = menuGroupDao.save(createMenuGroup(null, "치킨"));
        Menu savedMenu = menuDao.save(createMenu(null, savedProduct.getName(), savedProduct.getPrice(), savedMenuGroup.getId(),
                Collections.singletonList(createMenuProduct(null, savedProduct.getId(), 1L))));
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 0, false));
        Order orderRequest = createOrder(null, orderTable.getId(), null, null,
                Collections.singletonList(createOrderLineItem(null, savedMenu.getId(), 1)));
        Order savedOrder = orderService.create(orderRequest);
        Order invalidChangingOrder = createOrder(null, null, invalidOrderStatus.name(), null, null);

        orderService.changeOrderStatus(savedOrder.getId(), invalidChangingOrder);

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), savedOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }
}