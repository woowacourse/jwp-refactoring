package kitchenpos.service;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.utils.TestObjectUtils.NOT_EXIST_VALUE;
import static kitchenpos.utils.TestObjectUtils.createOrder;
import static kitchenpos.utils.TestObjectUtils.createOrderLineItem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collections;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderServiceTest extends ServiceTest {

    private static final Long NOT_EMPTY_ORDER_TABLE_ID = 2L;
    private static final Long EMPTY_ORDER_TABLE_ID = 1L;

    @Autowired
    private OrderService orderService;

    private Long menuId;
    private long quantity;
    private List<OrderLineItem> orderLineItems;

    @BeforeEach
    void setUp() {
        menuId = 1L;
        quantity = 1L;
        orderLineItems = Collections.singletonList(createOrderLineItem(menuId, quantity));
    }

    @DisplayName("주문 생성 - 성공")
    @Test
    void create_SuccessToCreate() {
        Order order = createOrder(NOT_EMPTY_ORDER_TABLE_ID, orderLineItems);

        Order savedOrder = orderService.create(order);

        assertAll(() -> {
            assertThat(savedOrder.getId()).isNotNull();
            assertThat(savedOrder.getOrderTableId()).isEqualTo(2L);
            assertThat(savedOrder.getOrderStatus()).isEqualTo(COOKING.name());
            assertThat(savedOrder.getOrderedTime()).isNotNull();
            assertThat(savedOrder.getOrderLineItems()).hasSize(orderLineItems.size());
        });
    }

    @DisplayName("주문 생성 - 예외, 주문 항목이 null인 경우")
    @Test
    void create_OrderLineItemsIsNull_ThrownException() {
        Order order = createOrder(NOT_EMPTY_ORDER_TABLE_ID, null);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 - 예외, 주문 항목이 빈 경우")
    @Test
    void create_OrderLineItemsIsEmpty_ThrownException() {
        Order order = createOrder(NOT_EMPTY_ORDER_TABLE_ID, Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 - 예외, 주문 항목의 메뉴를 찾을 수 없는 경우")
    @Test
    void create_NotFoundOrderLineItemMenu_ThrownException() {
        menuId = NOT_EXIST_VALUE;
        orderLineItems = Collections.singletonList(createOrderLineItem(menuId, quantity));
        Order order = createOrder(NOT_EMPTY_ORDER_TABLE_ID, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 - 예외, 주문 테이블을 찾을 수 없는 경우")
    @Test
    void create_NotFoundOrderTable_ThrownException() {
        orderLineItems = Collections.singletonList(createOrderLineItem(menuId, quantity));
        Order order = createOrder(NOT_EXIST_VALUE, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 - 예외, 주문 테이블이 비어있는 경우")
    @Test
    void create_OrderTableIsEmpty_ThrownException() {
        orderLineItems = Collections.singletonList(createOrderLineItem(menuId, quantity));
        Order order = createOrder(EMPTY_ORDER_TABLE_ID, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 조회 - 성공")
    @Test
    void list_SuccessToFindAll() {
        List<Order> orders = orderService.list();

        assertThat(orders).isNotEmpty();
    }

    @DisplayName("주문 상태 변경 - 성공")
    @Test
    void changeOrderStatus_SuccessToChange() {
        Order order = createOrder(NOT_EMPTY_ORDER_TABLE_ID, orderLineItems);
        Order savedOrder = orderService.create(order);
        savedOrder.setOrderStatus(MEAL.name());

        Order changedOrder = orderService.changeOrderStatus(NOT_EMPTY_ORDER_TABLE_ID, savedOrder);

        assertAll(() -> {
            assertThat(changedOrder.getId()).isNotNull();
            assertThat(changedOrder.getOrderTableId()).isEqualTo(NOT_EMPTY_ORDER_TABLE_ID);
            assertThat(changedOrder.getOrderStatus()).isEqualTo(MEAL.name());
            assertThat(changedOrder.getOrderedTime()).isNotNull();
            assertThat(changedOrder.getOrderLineItems()).hasSize(orderLineItems.size());
        });
    }

    @DisplayName("주문 상태 변경 - 예외, 주문을 찾을 수 없는 경우")
    @Test
    void changeOrderStatus_NotFoundOrder_ThrownException() {
        Order order = createOrder(NOT_EMPTY_ORDER_TABLE_ID, orderLineItems);
        Order savedOrder = orderService.create(order);
        savedOrder.setOrderStatus(MEAL.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(NOT_EXIST_VALUE, savedOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 변경 - 예외, 주문 상태가 완료인 경우")
    @Test
    void changeOrderStatus_OrderStatusIsCompletion_ThrownException() {
        Order order = createOrder(NOT_EMPTY_ORDER_TABLE_ID, orderLineItems);
        Order savedOrder = orderService.create(order);
        savedOrder.setOrderStatus(COMPLETION.name());
        Order changedOrder = orderService.changeOrderStatus(NOT_EMPTY_ORDER_TABLE_ID, savedOrder);
        changedOrder.setOrderStatus(MEAL.name());

        assertThatThrownBy(
            () -> orderService.changeOrderStatus(NOT_EMPTY_ORDER_TABLE_ID, savedOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
