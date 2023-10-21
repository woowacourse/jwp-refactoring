package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        // given
        final LocalDateTime savedTime = LocalDateTime.now();
        final List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(1L, 1L, 1L, 1L),
                                                           new OrderLineItem(2L, 1L, 2L, 2L));
        final Order order = Order.forSave(1L, OrderStatus.COOKING.name(), savedTime, orderLineItems);

        given(menuDao.countByIdIn(any()))
            .willReturn(2L);
        given(orderTableDao.findById(any()))
            .willReturn(Optional.of(new OrderTable(1L, 1L, 10, false)));
        given(orderDao.save(any()))
            .willReturn(new Order(1L, 1L, OrderStatus.COOKING.name(), savedTime,
                                  orderLineItems));

        // when
        final Order created = orderService.create(order);

        // then
        assertThat(created.getId()).isEqualTo(1L);
        assertThat(created.getOrderTableId()).isEqualTo(order.getOrderTableId());
        assertThat(created.getOrderStatus()).isEqualTo(order.getOrderStatus());
        assertThat(created.getOrderedTime()).isNotNull();
    }

    @DisplayName("주문의 주문 항목이 없으면 예외가 발생한다.")
    @Test
    void create_emptyOrderLineItems() {
        // given
        final Order order = Order.forSave(1L, OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.emptyList());

        // when
        // then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 개수가 저장된 메뉴의 개수와 다르면 예외가 발생한다.")
    @Test
    void create_differentMenuSize() {
        // given
        final Order order = Order.forSave(1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                                          List.of(new OrderLineItem(1L, 1L, 1L, 1L),
                                                  new OrderLineItem(2L, 1L, 2L, 2L)));
        given(menuDao.countByIdIn(any()))
            .willReturn(1L);

        // when
        // then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재하지 않으면 예외가 발생한다.")
    @Test
    void create_failNotExistOrderTable() {
        // given
        final long notExistedOrderTable = 0L;
        final Order order = Order.forSave(notExistedOrderTable, OrderStatus.COOKING.name(), LocalDateTime.now(),
                                          List.of(new OrderLineItem(1L, 1L, 1L, 1L),
                                                  new OrderLineItem(2L, 1L, 2L, 2L)));

        given(menuDao.countByIdIn(any()))
            .willReturn(2L);
        given(orderTableDao.findById(any()))
            .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어 있으면 예외가 발생한다.")
    @Test
    void create_failEmptyOrderTable() {
        // given
        final Order order = Order.forSave(1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                                          List.of(new OrderLineItem(1L, 1L, 1L, 1L),
                                                  new OrderLineItem(2L, 1L, 2L, 2L)));

        given(menuDao.countByIdIn(any()))
            .willReturn(2L);
        given(orderTableDao.findById(any()))
            .willReturn(Optional.of(new OrderTable(1L, 1L, 10, true)));

        // when
        // then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        final Long orderTableId = 1L;
        final Order order = new Order(orderTableId, orderTableId, OrderStatus.COOKING.name(), LocalDateTime.now());

        given(orderDao.findById(order.getId()))
            .willReturn(Optional.of(order));

        // when
        final Order changed = orderService.changeOrderStatus(orderTableId, order);

        // then
        assertThat(changed.getId()).isEqualTo(order.getId());
        assertThat(changed.getOrderTableId()).isEqualTo(order.getOrderTableId());
        assertThat(changed.getOrderStatus()).isEqualTo(order.getOrderStatus());
    }

    @DisplayName("주문이 존재하지 않으면 예외가 발생한다.")
    @Test
    void changeOrderStatus_failNotExistOrder() {
        // given
        final Long orderTableId = 1L;
        final Order order = new Order(1L, orderTableId, OrderStatus.COOKING.name(), LocalDateTime.now());

        given(orderDao.findById(order.getId()))
            .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderTableId, order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 COMPLETION 이면 예외가 발생한다.")
    @Test
    void changeOrderStatus_failStatusIsCompletion() {
        // given
        final Long orderTableId = 1L;
        final Order order = new Order(1L, orderTableId, OrderStatus.COMPLETION.name(), LocalDateTime.now());

        given(orderDao.findById(order.getId()))
            .willReturn(Optional.of(order));

        // when
        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderTableId, order))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
