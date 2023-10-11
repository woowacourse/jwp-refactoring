package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;
    @Mock
    private MenuDao menuDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;

    @Nested
    class CreateTest {
        @Test
        @DisplayName("orderLineItems가 비어있으면 예외가 발생한다.")
        void emptyOrderLineItems() {
            // given
            final Order order = new Order(1L, 1L, "orderStatus", LocalDateTime.now(), Collections.emptyList());

            // when, then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("요청한 메뉴가 존재하지 않으면 예외가 발생한다.")
        void doesNotMatchMenuSize() {
            // given
            final List<OrderLineItem> orderLineItems = List.of(
                    new OrderLineItem(1L, 1L, 1L, 2),
                    new OrderLineItem(2L, 1L, 2L, 3),
                    new OrderLineItem(3L, 1L, 3L, 4)
            );
            final Order order = new Order(1L, 1L, "orderStatus", LocalDateTime.now(), orderLineItems);
            given(menuDao.countByIdIn(any())).willReturn(999L);

            // when, then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("요청에 해당하는 orderTable을 찾지 못하면 예외가 발생한다.")
        void cannotFindOrderTable() {
            // given
            final List<OrderLineItem> orderLineItems = List.of(
                    new OrderLineItem(1L, 1L, 1L, 2),
                    new OrderLineItem(2L, 1L, 2L, 3),
                    new OrderLineItem(3L, 1L, 3L, 4)
            );
            final Order order = new Order(1L, 1L, "orderStatus", LocalDateTime.now(), orderLineItems);
            given(menuDao.countByIdIn(any())).willReturn(3L);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("요청에 해당하는 orderTable이 비어있으면(empty) 예외가 발생한다.")
        void emptyOrderTable() {
            // given
            final List<OrderLineItem> orderLineItems = List.of(
                    new OrderLineItem(1L, 1L, 1L, 2),
                    new OrderLineItem(2L, 1L, 2L, 3),
                    new OrderLineItem(3L, 1L, 3L, 4)
            );
            final Order order = new Order(1L, 1L, "orderStatus", LocalDateTime.now(), orderLineItems);
            final OrderTable orderTable = new OrderTable(1L, 2L, 0, true);
            given(menuDao.countByIdIn(any())).willReturn(3L);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));

            // when, then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문을 생성한다.")
        void createOrder() {
            // given
            final OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 1L, 2);
            final OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1L, 2L, 3);
            final OrderLineItem orderLineItem3 = new OrderLineItem(3L, 1L, 3L, 4);
            final List<OrderLineItem> orderLineItems = List.of(orderLineItem1, orderLineItem2, orderLineItem3);

            final Order order = new Order(1L, 1L, "orderStatus", LocalDateTime.now(), orderLineItems);
            final Order savedOrder = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
            final OrderTable orderTable = new OrderTable(1L, 2L, 3, false);
            given(menuDao.countByIdIn(any())).willReturn(3L);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
            given(orderDao.save(any())).willReturn(savedOrder);

            given(orderLineItemDao.save(orderLineItem1)).willReturn(orderLineItem1);
            given(orderLineItemDao.save(orderLineItem2)).willReturn(orderLineItem2);
            given(orderLineItemDao.save(orderLineItem3)).willReturn(orderLineItem3);

            // when
            final Order result = orderService.create(order);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(savedOrder);
        }
    }

    @Test
    @DisplayName("주문을 조회한다.")
    void list() {
        // given
        final List<OrderLineItem> orderLineItems = List.of(
                new OrderLineItem(1L, 1L, 1L, 2),
                new OrderLineItem(2L, 1L, 2L, 3),
                new OrderLineItem(3L, 1L, 3L, 4)
        );
        final List<Order> orders = List.of(
                new Order(1L, 1L, "orderStatus", LocalDateTime.now(), orderLineItems)
        );
        given(orderDao.findAll()).willReturn(orders);

        // when
        final List<Order> result = orderService.list();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(orders);
    }

    @Test
    @DisplayName("이미 orderStatus가 COMPLETION으로 되었다면 예외가 발생한다.")
    void changeOrderStatus() {
        // given
        final Order order = mock(Order.class);
        given(orderDao.findById(anyLong())).willReturn(Optional.of(order));
        given(order.getOrderStatus()).willReturn("COMPLETION");

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
