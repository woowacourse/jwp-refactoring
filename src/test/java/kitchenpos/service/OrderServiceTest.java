package kitchenpos.service;

import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.*;
import kitchenpos.fixture.*;
import org.aspectj.apache.bcel.generic.TABLESWITCH;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Sql(scripts = "/clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DisplayName("OrderService 테스트")
class OrderServiceTest {

    private static final long ID = 1L;
    private static final long ORDER_TABLE_ID = 1L;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private MenuService menuService;
    @Autowired
    private OrderTableDao orderTableDao;

    private MenuGroup menuGroup;
    private Product product;
    private Menu menu;
    private OrderTable orderTable;
    private List<OrderLineItem> orderLineItems;

    @BeforeEach
    void setUp() {
        menuGroup = MenuGroupFixture.create();
        menuGroupDao.save(menuGroup);

        product = ProductFixture.create();
        productDao.save(product);

        orderTable = OrderTableFixture.create();
        orderTableDao.save(orderTable);

        menu = MenuFixture.create();
        menuService.create(menu);

        orderLineItems = OrderFixture.orderLineItems();
    }

    @DisplayName("주문 추가")
    @Test
    void create() {
        Order savedOrder = orderService.create(OrderFixture.create());

        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(savedOrder.getOrderLineItems().get(0).getSeq()).isNotNull();
    }

    @DisplayName("주문 추가 - 실패 - 주문 항목이 없는 경우")
    @Test
    void createFailureWhenNoOrderLineItem() {
        Order order = OrderFixture.create(ID, ORDER_TABLE_ID, OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 추가 - 실패 - 존재하지 않는 메뉴가 포함된 경우")
    @Test
    void createFailureWhenNotExistMenu() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(ID);
        orderLineItem.setOrderId(ID);
        orderLineItem.setMenuId(999L);
        orderLineItem.setQuantity(1);

        Order order = OrderFixture.create(ID, ORDER_TABLE_ID, OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.singletonList(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 추가 - 실패 - 존재하지 않는 테이블인 경우")
    @Test
    void createFailureWhenNotExistTable() {
        Order order = OrderFixture.create(ID, ORDER_TABLE_ID + 99L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 추가 - 실패 - 빈 테이블인 경우")
    @Test
    void createFailureWhenEmptyTable() {
        OrderTable emptyTable = OrderTableFixture.create();
        emptyTable.setEmpty(true);
        orderTableDao.save(emptyTable);

        Order order = OrderFixture.create(ID, emptyTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 전체 조회")
    @Test
    void list() {
        Order order = OrderFixture.create();
        Order order2 = OrderFixture.create(order.getId() + 1L, ORDER_TABLE_ID, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        orderService.create(order);
        orderService.create(order2);

        List<Order> list = orderService.list();
        assertThat(list).hasSize(2);
    }

    @DisplayName("주문 상태 변경 - 성공")
    @Test
    void changeOrderStatus() {
        Order savedOrder = orderService.create(OrderFixture.create());

        Order completionOrder = OrderFixture.create(3L, ORDER_TABLE_ID, OrderStatus.COMPLETION.name(), LocalDateTime.now(), orderLineItems);
        Order changeOrderStatus = orderService.changeOrderStatus(savedOrder.getId(), completionOrder);

        assertThat(changeOrderStatus.getOrderStatus()).isEqualTo(completionOrder.getOrderStatus());
    }

    @DisplayName("주문 상태 변경 - 실패 - 이미 완료된 주문인 경우")
    @Test
    void changeOrderStatus_With_CompletedOrder() {
        Order order = OrderFixture.create(ID, ORDER_TABLE_ID, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        Order completionOrder = OrderFixture.create(ID + 1L, order.getOrderTableId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(), orderLineItems);

        Order savedOrder = orderService.create(order);
        Order savedCompletedOrder = orderService.create(completionOrder);

        orderService.changeOrderStatus(savedOrder.getId(), savedCompletedOrder);

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), savedCompletedOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
