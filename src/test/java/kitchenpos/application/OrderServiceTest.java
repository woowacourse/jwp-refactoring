package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.exception.OrderServiceException.EmptyOrderLineItemsException;
import kitchenpos.application.exception.OrderServiceException.EmptyOrderTableException;
import kitchenpos.application.exception.OrderServiceException.NotExistsMenuException;
import kitchenpos.application.exception.OrderServiceException.NotExistsOrderException;
import kitchenpos.application.exception.OrderServiceException.NotExistsOrderTableException;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.exception.OrderException.CompletionOrderException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;


    private OrderLineItem orderLineItem1 = new OrderLineItem();
    private OrderLineItem orderLineItem2 = new OrderLineItem();
    private OrderTable orderTable = new OrderTable();
    private Order order = Order.of(orderTable, List.of(orderLineItem1, orderLineItem2));

    @BeforeEach
    void init() {
        orderLineItem1.setMenuId(1L);
        orderLineItem2.setMenuId(2L);

        orderTable.setId(1L);
        orderTable.setEmpty(false);
    }

    @Test
    @DisplayName("주문을 생성할 수 있다.")
    void create_success() {
        when(menuRepository.countByIdIn(List.of(1L, 2L))).thenReturn(2L);
        when(orderTableRepository.findById(order.getOrderTableId())).thenReturn(Optional.ofNullable(orderTable));
        when(orderRepository.save(order)).thenReturn(order);

        orderService.create(order);
        LocalDateTime end = LocalDateTime.now();

        assertAll(
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
                () -> assertThat(order.getOrderedTime()).isBefore(end)
        );
    }

    @Test
    @DisplayName("주문을 생성할 때 주문 항목의 메뉴가 db에 존재하지 않으면 예외가 발생한다.")
    void create_fail_orderLineItems_no_menu() {
        when(menuRepository.countByIdIn(List.of(1L, 2L))).thenReturn(1L);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(NotExistsMenuException.class);
    }

    @Test
    @DisplayName("주문을 생성할 때 주문 테이블 번호가 db에 존재하지 않으면 예외가 발생한다.")
    void create_fail_no_orderTable() {
        when(menuRepository.countByIdIn(List.of(1L, 2L))).thenReturn(2L);
        when(orderTableRepository.findById(order.getOrderTableId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(NotExistsOrderTableException.class);
    }

    @Test
    @DisplayName("주문을 생성할 때 주문 테이블이 빈 테이블이면 예외가 발생한다.")
    void create_fail_empty_orderTable() {
        orderTable.setEmpty(true);
        when(menuRepository.countByIdIn(List.of(1L, 2L))).thenReturn(2L);
        when(orderTableRepository.findById(order.getOrderTableId())).thenReturn(Optional.ofNullable(orderTable));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(EmptyOrderTableException.class);
    }

    @Test
    @DisplayName("주문 목록을 조회할 수 있다. - 0개의 주문 목록")
    void list_success1() {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        orderService.list();

        assertAll(
                () -> verify(orderRepository, times(1)).findAll()
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 4})
    @DisplayName("주문 목록을 조회할 수 있다. - n개의 주문 목록")
    void list_success2(int n) {
        when(orderRepository.findAll()).thenReturn(getOrdersByNTimes(n));

        orderService.list();

        assertAll(
                () -> verify(orderRepository, times(1)).findAll()
        );
    }

    @Test
    @DisplayName("현재 주문 상태를 변경할 수 있다.")
    void changeOrderStatus_success() {
        OrderStatus nowOrderStatus = OrderStatus.COOKING;
        OrderStatus newOrderStatus = OrderStatus.MEAL;
        order.changeOrderStatus(nowOrderStatus);
        Order newOrder = new Order();
        newOrder.changeOrderStatus(newOrderStatus);
        Long orderId = order.getId();

        when(orderRepository.findById(orderId)).thenReturn(Optional.ofNullable(order));

        Order savedOrder = orderService.changeOrderStatus(orderId, newOrder);

        assertThat(savedOrder.getOrderStatus()).isEqualTo(newOrderStatus);
    }

    @Test
    @DisplayName("주문이 db에 저장되어있지 않으면 예외가 발생한다.")
    void changeOrderStatus_fail_no_order() {
        Long orderId = order.getId();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, new Order()))
                .isInstanceOf(NotExistsOrderException.class);
    }

    @Test
    @DisplayName("주문이 현재 주문 상태가 COMPLETION이면 예외가 발생한다.")
    void changeOrderStatus_fail_completion() {
        order.changeOrderStatus(OrderStatus.COMPLETION);
        Long orderId = order.getId();

        when(orderRepository.findById(orderId)).thenReturn(Optional.ofNullable(order));

        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, new Order()))
                .isInstanceOf(CompletionOrderException.class);
    }

    private List<Order> getOrdersByNTimes(int n) {
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            orders.add(order);
        }
        return orders;
    }
}
