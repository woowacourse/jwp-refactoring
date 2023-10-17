package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.spy;
import static org.mockito.BDDMockito.when;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    MenuDao menuDao;

    @Mock
    OrderDao orderDao;

    @Mock
    OrderLineItemDao orderLineItemDao;

    @Mock
    OrderTableDao orderTableDao;

    @Nested
    class Create {

        @Test
        void 주문을_생성할_수_있다() {
            // given
            final OrderLineItem wooDong = new OrderLineItem(1L, 1L, 1);
            final OrderLineItem frenchFries = new OrderLineItem(1L, 2L, 1);
            final List<OrderLineItem> orderLineItems = List.of(wooDong, frenchFries);
            final Order order = new Order(1L, COOKING.name(), LocalDateTime.now(), orderLineItems);
            given(menuDao.countByIdIn(anyList())).willReturn((long) orderLineItems.size());

            final Order spyOrder = spy(new Order(order.getOrderTableId(), order.getOrderStatus(), order.getOrderedTime(), new ArrayList<>()));
            given(orderDao.save(any(Order.class))).willReturn(spyOrder);

            final OrderTable orderTable = new OrderTable(6, false);
            final OrderTable spyOrderTable = spy(orderTable);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.ofNullable(spyOrderTable));

            given(orderLineItemDao.save(any(OrderLineItem.class)))
                    .willReturn(wooDong)
                    .willReturn(frenchFries);

            final long orderTableId = 1L;
            given(spyOrderTable.getId()).willReturn(orderTableId);

            final long orderId = 1L;
            given(spyOrder.getId()).willReturn(orderId);

            // when
            final Order actual = orderService.create(order);

            // then
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getOrderTableId()).isEqualTo(orderTableId),
                    () -> assertThat(actual.getOrderStatus()).isEqualTo(COOKING.name()),
                    () -> assertThat(actual.getOrderLineItems())
                            .usingRecursiveFieldByFieldElementComparator()
                            .containsExactly(wooDong, frenchFries)
            );
        }

        @Test
        void 주문_항목이_없으면_예외가_발생한다() {
            // given
            final Order order = new Order(OrderStatus.COMPLETION.name(), LocalDateTime.now(), emptyList());

            // when, then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_항목의_총합과_메뉴의_총합이_다르면_예외를_발생한다() {
            // given
            final OrderLineItem wooDong = new OrderLineItem(1L, 1L, 1);
            final OrderLineItem frenchFries = new OrderLineItem(1L, 2L, 1);
            final Order order = new Order(OrderStatus.COMPLETION.name(), LocalDateTime.now(), List.of(wooDong, frenchFries));

            // when
            final long incorrectMenuSize = order.getOrderLineItems().size() - 1;
            when(menuDao.countByIdIn(anyList())).thenReturn(incorrectMenuSize);

            // then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문에_있는_주문_테이블이_없으면_예외가_발생한다() {
            // given
            final OrderLineItem wooDong = new OrderLineItem(1L, 1L, 1);
            final OrderLineItem frenchFries = new OrderLineItem(1L, 2L, 1);
            final List<OrderLineItem> orderLineItems = List.of(wooDong, frenchFries);
            final Order order = spy(new Order(OrderStatus.COMPLETION.name(), LocalDateTime.now(), orderLineItems));

            given(menuDao.countByIdIn(anyList())).willReturn((long) orderLineItems.size());
            given(order.getOrderTableId()).willReturn(1L);

            // when
            when(orderTableDao.findById(anyLong())).thenReturn(Optional.empty());

            // then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문에_있는_주문_테이블이_비어있으면_예외가_발생한다() {
            // given
            final OrderLineItem wooDong = new OrderLineItem(1L, 1L, 1);
            final OrderLineItem frenchFries = new OrderLineItem(1L, 2L, 1);
            final List<OrderLineItem> orderLineItems = List.of(wooDong, frenchFries);
            final Order order = spy(new Order(OrderStatus.COMPLETION.name(), LocalDateTime.now(), orderLineItems));

            given(menuDao.countByIdIn(anyList())).willReturn((long) orderLineItems.size());

            final long orderTableId = 1L;
            given(order.getOrderTableId()).willReturn(orderTableId);

            // when
            final OrderTable emptyOrderTable = new OrderTable(6, true);
            when(orderTableDao.findById(anyLong())).thenReturn(Optional.ofNullable(emptyOrderTable));

            // then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class FindAll {

        @Test
        void 주문을_전체_조회_할_수_있다() {
            // given
            final long orderId = 1L;
            final OrderLineItem wooDong = new OrderLineItem(orderId, 1L, 1);
            final OrderLineItem frenchFries = new OrderLineItem(orderId, 2L, 1);
            final List<OrderLineItem> orderLineItems = List.of(wooDong, frenchFries);

            given(orderLineItemDao.findAllByOrderId(orderId)).willReturn(orderLineItems);

            final Order spyOrder = spy(new Order(OrderStatus.COMPLETION.name(), LocalDateTime.now(), new ArrayList<>()));
            given(spyOrder.getId()).willReturn(orderId);
            given(orderDao.findAll()).willReturn(List.of(spyOrder));

            // when
            final List<Order> actual = orderService.list();

            // then
            assertAll(
                    () -> assertThat(actual).hasSize(1)
                            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "orderLineItems")
                            .containsExactly(spyOrder),
                    () -> assertThat(actual.get(0).getOrderLineItems())
                            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("seq")
                            .containsExactly(wooDong, frenchFries)
            );

        }
    }

    @Nested
    class ChangeOrderStatus {

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        void 주문_상태를_변경할_수_있다(final OrderStatus validedOrderStatus) {
            // given
            final long orderId = 1;
            final OrderLineItem wooDong = new OrderLineItem(orderId, 1L, 1);
            final OrderLineItem frenchFries = new OrderLineItem(orderId, 2L, 1);
            final List<OrderLineItem> orderLineItems = new ArrayList<>(List.of(wooDong, frenchFries));

            final Order order = new Order(validedOrderStatus.name(), LocalDateTime.now(), orderLineItems);
            given(orderDao.findById(orderId)).willReturn(Optional.ofNullable(order));

            // when
            final Order expected = new Order(OrderStatus.COMPLETION.name(), LocalDateTime.now(), orderLineItems);
            final Order actual = orderService.changeOrderStatus(orderId, expected);

            // then
            assertThat(actual.getOrderStatus()).isEqualTo(expected.getOrderStatus());
        }

        @Test
        void 주문_상태가_완료_상태라면_상태를_변경할_수_없다() {
            // given
            final long orderId = 1;
            final OrderLineItem wooDong = new OrderLineItem(orderId, 1L, 1);
            final OrderLineItem frenchFries = new OrderLineItem(orderId, 2L, 1);
            final List<OrderLineItem> orderLineItems = List.of(wooDong, frenchFries);

            final Order order = new Order(OrderStatus.COMPLETION.name(), LocalDateTime.now(), orderLineItems);
            given(orderDao.findById(orderId)).willReturn(Optional.ofNullable(order));

            // when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
