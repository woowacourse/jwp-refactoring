package kitchenpos.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderLineItemRequest;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.OrderStatusRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        OrderTable orderTableRequest = OrderTable.of(4, false);
        orderTable = orderTableRepository.save(orderTableRequest);
    }

    @Test
    @DisplayName("정상적으로 주문한다.")
    void order() {
        // given
        final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), Arrays.asList(
                new OrderLineItemRequest(1L, 1L)
        ));

        // when
        final Long orderId = orderService.order(request);

        // then
        final Order savedOrder = orderRepository.findById(orderId).get();

        assertThat(savedOrder.getId()).isEqualTo(orderId);
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(savedOrder.getOrderLineItems()).hasSize(1);
    }

    @Test
    @DisplayName("없는 메뉴를 주문했을때 에러를 반환한다.")
    void order_non_men() {
        // given
        final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), Arrays.asList());

        // when
        // then
        assertThatThrownBy(
                () -> orderService.order(request)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문한 메뉴가 없을때 에러를 반환한다.")
    void order_non_existent_menu() {
        // given
        final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), Arrays.asList(
                new OrderLineItemRequest(999L, 1L)
        ));

        // when & then
        assertThatThrownBy(
                () -> orderService.order(request)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("정상적으로 주문상태를 변경한다.")
    void changeOrderStatus() {
        // given
        final OrderCreateRequest orderRequest = new OrderCreateRequest(orderTable.getId(), Arrays.asList(
                new OrderLineItemRequest(1L, 1L)
        ));
        final Long orderId = orderService.order(orderRequest);
        final OrderStatusRequest statusRequest = new OrderStatusRequest(OrderStatus.MEAL);

        // when
        final OrderResponse response = orderService.changeOrderStatus(orderId, statusRequest);

        // then
        final Order updatedOrder = orderRepository.findById(orderId).get();

        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
        assertThat(updatedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }
}
