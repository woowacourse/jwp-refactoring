package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.dto.OrderChangeOrderStatusRequest;
import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.application.dto.OrderLineItemCreateRequest;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Price;
import kitchenpos.repository.MenuRepository;
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
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        // given
        final Menu menu1 = new Menu(1L, "후라이드", Collections.emptyList());
        final Menu menu2 = new Menu(2L, "피자", Collections.emptyList());
        final List<OrderLineItem> orderLineItems = List.of(
            new OrderLineItem(1L, 1L, "치킨", new Price(BigDecimal.TEN), menu1),
            new OrderLineItem(2L, 2L, "피자", new Price(BigDecimal.TEN), menu2));

        final Order order = new Order(1L, OrderStatus.COOKING, orderLineItems);
        final OrderTable orderTable = new OrderTable(1L, 10, false, Collections.emptyList());

        given(menuRepository.countByIds(any()))
            .willReturn(2L);
        given(menuRepository.getById(any()))
            .willReturn(menu1, menu2);
        given(orderTableRepository.getById(any()))
            .willReturn(orderTable);
        given(orderRepository.save(any()))
            .willReturn(order);

        // when
        final OrderResponse created = orderService.create(new OrderCreateRequest(orderTable.getId(), List.of(
            new OrderLineItemCreateRequest(1L, 1L),
            new OrderLineItemCreateRequest(2L, 2L)
        )));

        // then
        assertThat(created.getId()).isEqualTo(order.getId());
        assertThat(created.getOrderStatus()).isEqualTo(order.getOrderStatus().name());
    }

    @DisplayName("주문의 주문 항목이 없으면 예외가 발생한다.")
    @Test
    void create_emptyOrderLineItems() {
        // given
        final OrderCreateRequest cooking = new OrderCreateRequest(1L, Collections.emptyList());

        // when
        // then
        assertThatThrownBy(() -> orderService.create(cooking)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 개수가 저장된 메뉴의 개수와 다르면 예외가 발생한다.")
    @Test
    void create_differentMenuSize() {
        // given
        given(menuRepository.countByIds(any()))
            .willReturn(1L);

        // when
        // then
        assertThatThrownBy(() -> orderService.create(new OrderCreateRequest(1L, List.of(
            new OrderLineItemCreateRequest(1L, 1L),
            new OrderLineItemCreateRequest(2L, 2L)
        )))).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재하지 않으면 예외가 발생한다.")
    @Test
    void create_failNotExistOrderTable() {
        // given
        final long notExistOrderTable = 0L;
        given(menuRepository.countByIds(any()))
            .willReturn(2L);
        given(orderTableRepository.getById(notExistOrderTable))
            .willThrow(IllegalArgumentException.class);

        // when
        // then
        assertThatThrownBy(() -> orderService.create(new OrderCreateRequest(notExistOrderTable, List.of(
            new OrderLineItemCreateRequest(1L, 1L),
            new OrderLineItemCreateRequest(2L, 2L)
        )))).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어 있으면 예외가 발생한다.")
    @Test
    void create_failEmptyOrderTable() {
        // given
        given(orderTableRepository.getById(any()))
            .willReturn(new OrderTable(1L, 10, true, Collections.emptyList()));
        given(menuRepository.countByIds(any()))
            .willReturn(2L);
        given(menuRepository.getById(any()))
            .willReturn(new Menu(1L, "후라이드", Collections.emptyList()))
            .willReturn(new Menu(2L, "양념", Collections.emptyList()));

        // when
        // then
        assertThatThrownBy(() -> orderService.create(new OrderCreateRequest(1L, List.of(
            new OrderLineItemCreateRequest(1L, 1L),
            new OrderLineItemCreateRequest(2L, 2L)
        )))).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        final Order order = new Order(1L, OrderStatus.COOKING, List.of(
            new OrderLineItem(1L, 1L, "치킨", new Price(BigDecimal.TEN), null),
            new OrderLineItem(2L, 2L, "치킨", new Price(BigDecimal.TEN), null)
        ));

        given(orderRepository.getById(any()))
            .willReturn(order);

        // when
        final OrderResponse changed = orderService.changeOrderStatus(order.getId(),
                                                                     new OrderChangeOrderStatusRequest("COMPLETION"));

        // then
        assertThat(changed.getId()).isEqualTo(order.getId());
        assertThat(changed.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @DisplayName("주문이 존재하지 않으면 예외가 발생한다.")
    @Test
    void changeOrderStatus_failNotExistOrder() {
        // given
        final Long orderId = 0L;
        given(orderRepository.getById(orderId))
            .willThrow(IllegalArgumentException.class);

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
                                      List.of(new OrderLineItem(1L, 1L, "치킨", new Price(BigDecimal.TEN), null)));

        given(orderRepository.getById(order.getId()))
            .willReturn(order);

        // when
        // then
        assertThatThrownBy(
            () -> orderService.changeOrderStatus(orderTableId, new OrderChangeOrderStatusRequest("COMPLETION")))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
