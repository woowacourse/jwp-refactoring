package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.dto.OrderChangeOrderStatusRequest;
import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        // given
        final List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(1L, 1L),
                                                           new OrderLineItem(2L, 2L));
        final Order order = Order.forSave(OrderStatus.COOKING, orderLineItems);

        given(menuRepository.existsById(any()))
            .willReturn(true)
            .willReturn(true);
        given(orderLineItemRepository.findAllById(any()))
            .willReturn(orderLineItems);
        given(orderRepository.save(any()))
            .willReturn(new Order(1L, OrderStatus.COOKING, orderLineItems));

        // when
        final Order created = orderService.create(new OrderCreateRequest(1L, "COOKING", List.of(1L, 2L)));

        // then
        assertThat(created.getId()).isEqualTo(1L);
        assertThat(created.getOrderStatus()).isEqualTo(order.getOrderStatus());
        assertThat(created.getOrderedTime()).isNotNull();
    }

    @DisplayName("주문의 주문 항목이 없으면 예외가 발생한다.")
    @Test
    void create_emptyOrderLineItems() {
        // given
        final OrderCreateRequest cooking = new OrderCreateRequest(1L, "COOKING", Collections.emptyList());

        // when
        // then
        assertThatThrownBy(() -> {
            orderService.create(cooking);
        })
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 개수가 저장된 메뉴의 개수와 다르면 예외가 발생한다.")
    @Test
    void create_differentMenuSize() {
        // given
        given(menuRepository.existsById(any()))
            .willReturn(true)
            .willReturn(false);

        // when
        // then
        assertThatThrownBy(() -> orderService.create(new OrderCreateRequest(1L, "COOKING", List.of(1L, 2L))))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재하지 않으면 예외가 발생한다.")
    @Test
    void create_failNotExistOrderTable() {
        // given
        given(menuRepository.existsById(any()))
            .willReturn(true)
            .willReturn(true);
        given(orderTableRepository.findById(0L))
            .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> orderService.create(new OrderCreateRequest(0L, "COOKING", List.of(1L, 2L))))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어 있으면 예외가 발생한다.")
    @Test
    void create_failEmptyOrderTable() {
        // given
        given(menuRepository.existsById(any()))
            .willReturn(true)
            .willReturn(true);
        given(orderTableRepository.findById(any()))
            .willReturn(Optional.of(new OrderTable(1L, 10, true, Collections.emptyList(), null)));

        // when
        // then
        assertThatThrownBy(() -> orderService.create(new OrderCreateRequest(1L, "COOKING", List.of(1L, 2L))))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        final Order order = new Order(1L, OrderStatus.COOKING, List.of(
            new OrderLineItem(1L, 1L),
            new OrderLineItem(2L, 2L)
        ));

        given(orderRepository.findById(any()))
            .willReturn(Optional.of(order));

        // when
        final Order changed = orderService.changeOrderStatus(order.getId(),
                                                             new OrderChangeOrderStatusRequest("COMPLETION"));

        // then
        assertThat(changed.getId()).isEqualTo(order.getId());
        assertThat(changed.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @DisplayName("주문이 존재하지 않으면 예외가 발생한다.")
    @Test
    void changeOrderStatus_failNotExistOrder() {
        // given
        final Long orderId = 0L;
        given(orderRepository.findById(orderId))
            .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, new OrderChangeOrderStatusRequest("COOKING")))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 COMPLETION 이면 예외가 발생한다.")
    @Test
    void changeOrderStatus_failStatusIsCompletion() {
        // given
        final Long orderTableId = 1L;
        final Order order = new Order(1L, OrderStatus.COMPLETION,
                                      List.of(new OrderLineItem(1L, 1L)));

        given(orderRepository.findById(order.getId()))
            .willReturn(Optional.of(order));

        // when
        // then
        assertThatThrownBy(
            () -> orderService.changeOrderStatus(orderTableId, new OrderChangeOrderStatusRequest("COMPLETION")))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
