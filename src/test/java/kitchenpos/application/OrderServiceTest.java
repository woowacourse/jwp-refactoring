package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
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
            final Order order = spy(new Order(OrderStatus.COMPLETION.name(), LocalDateTime.now(), orderLineItems));

            given(menuDao.countByIdIn(anyList())).willReturn((long) orderLineItems.size());

            final OrderTable orderTable = new OrderTable(6, false);
            final long orderTableId = 1L;
            given(order.getOrderTableId()).willReturn(orderTableId);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.ofNullable(orderTable));
            given(orderDao.save(order)).willReturn(order);

            // when
            orderService.create(order);

            // then
            assertAll(
                    () -> assertThat(order.getOrderTableId()).isEqualTo(orderTableId),
                    () -> assertThat(order.getOrderStatus()).isEqualTo(COOKING.name())
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
            Order order = spy(new Order(OrderStatus.COMPLETION.name(), LocalDateTime.now(), orderLineItems));

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

            final OrderTable emptyOrderTable = new OrderTable(6, true);
            final long orderTableId = 1L;
            given(order.getOrderTableId()).willReturn(orderTableId);

            // when
            when(orderTableDao.findById(anyLong())).thenReturn(Optional.ofNullable(emptyOrderTable));

            // then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    // TODO: 10/12/23 list
    @Disabled
    @Nested
    class FindAll {

        @Test
        void list() {
            // given
            final OrderLineItem wooDong = new OrderLineItem(1L, 1L, 1);
            final OrderLineItem frenchFries = new OrderLineItem(1L, 2L, 1);
            final List<OrderLineItem> orderLineItems = List.of(wooDong, frenchFries);
            final Order order = spy(new Order(OrderStatus.COMPLETION.name(), LocalDateTime.now(), null));
        }
    }

    // TODO: 10/12/23 changeOrderStatus
    @Disabled
    @Nested
    class ChangeOrderStatus {
        
        
    }
}
