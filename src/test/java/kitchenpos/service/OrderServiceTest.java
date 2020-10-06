package kitchenpos.service;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.utils.TestObjectUtils.NOT_EXIST_VALUE;
import static kitchenpos.utils.TestObjectUtils.createOrder;
import static kitchenpos.utils.TestObjectUtils.createOrderLineItem;
import static kitchenpos.utils.TestObjectUtils.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collections;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.application.TableService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    private Long menuId;
    private long quantity;
    private List<OrderLineItem> orderLineItems;
    private OrderTable emptyOrderTable;
    private OrderTable notEmptyOrderTable;

    @BeforeEach
    void setUp() {
        menuId = 1L;
        quantity = 1L;
        orderLineItems = Collections.singletonList(createOrderLineItem(menuId, quantity));
        emptyOrderTable = tableService.create(createOrderTable(0, true));
        notEmptyOrderTable = tableService.create(createOrderTable(4, false));
    }

    @DisplayName("주문 생성 - 성공")
    @Test
    void create_SuccessToCreate() {
        Order order = createOrder(notEmptyOrderTable.getId(), null, orderLineItems);

        Order savedOrder = orderService.create(order);

        assertAll(() -> {
            assertThat(savedOrder.getId()).isNotNull();
            assertThat(savedOrder.getOrderTableId()).isEqualTo(notEmptyOrderTable.getId());
            assertThat(savedOrder.getOrderStatus()).isEqualTo(COOKING.name());
            assertThat(savedOrder.getOrderedTime()).isNotNull();
            assertThat(savedOrder.getOrderLineItems()).hasSize(orderLineItems.size());
        });
    }

    @DisplayName("주문 생성 - 예외, 주문 항목이 null인 경우")
    @Test
    void create_OrderLineItemsIsNull_ThrownException() {
        Order order = createOrder(notEmptyOrderTable.getId(), null, null);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 - 예외, 주문 항목이 빈 경우")
    @Test
    void create_OrderLineItemsIsEmpty_ThrownException() {
        Order order = createOrder(notEmptyOrderTable.getId(), null, Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 - 예외, 주문 항목의 메뉴를 찾을 수 없는 경우")
    @Test
    void create_NotFoundOrderLineItemMenu_ThrownException() {
        menuId = NOT_EXIST_VALUE;
        orderLineItems = Collections.singletonList(createOrderLineItem(menuId, quantity));
        Order order = createOrder(notEmptyOrderTable.getId(), null, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 - 예외, 주문 테이블을 찾을 수 없는 경우")
    @Test
    void create_NotFoundOrderTable_ThrownException() {
        orderLineItems = Collections.singletonList(createOrderLineItem(menuId, quantity));
        Order order = createOrder(NOT_EXIST_VALUE, null, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 - 예외, 주문 테이블이 비어있는 경우")
    @Test
    void create_OrderTableIsEmpty_ThrownException() {
        orderLineItems = Collections.singletonList(createOrderLineItem(menuId, quantity));
        Order order = createOrder(emptyOrderTable.getId(), null, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 조회 - 성공")
    @Test
    void list_SuccessToFindAll() {
        Order order = createOrder(notEmptyOrderTable.getId(), null, orderLineItems);
        orderService.create(order);
        List<Order> orders = orderService.list();

        assertThat(orders).isNotEmpty();
    }

    @DisplayName("주문 상태 변경 - 성공")
    @Test
    void changeOrderStatus_SuccessToChange() {
        Order order = createOrder(notEmptyOrderTable.getId(), null, orderLineItems);
        Order savedOrder = orderService.create(order);
        savedOrder.setOrderStatus(MEAL.name());

        Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        assertAll(() -> {
            assertThat(changedOrder.getId()).isNotNull();
            assertThat(changedOrder.getOrderTableId()).isEqualTo(notEmptyOrderTable.getId());
            assertThat(changedOrder.getOrderStatus()).isEqualTo(MEAL.name());
            assertThat(changedOrder.getOrderedTime()).isNotNull();
            assertThat(changedOrder.getOrderLineItems()).hasSize(orderLineItems.size());
        });
    }

    @DisplayName("주문 상태 변경 - 예외, 주문을 찾을 수 없는 경우")
    @Test
    void changeOrderStatus_NotFoundOrder_ThrownException() {
        Order order = createOrder(notEmptyOrderTable.getId(), null, orderLineItems);
        Order savedOrder = orderService.create(order);
        savedOrder.setOrderStatus(MEAL.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(NOT_EXIST_VALUE, savedOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 변경 - 예외, 주문 상태가 완료인 경우")
    @Test
    void changeOrderStatus_OrderStatusIsCompletion_ThrownException() {
        Order order = createOrder(notEmptyOrderTable.getId(), null, orderLineItems);
        Order savedOrder = orderService.create(order);
        savedOrder.setOrderStatus(COMPLETION.name());
        Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), savedOrder);
        changedOrder.setOrderStatus(MEAL.name());

        assertThatThrownBy(
            () -> orderService.changeOrderStatus(notEmptyOrderTable.getId(), savedOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
