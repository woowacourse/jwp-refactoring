package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderFactory;
import kitchenpos.domain.OrderLineItemFactory;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTableFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest {

    private final MenuDao menuDao = mock(MenuDao.class);
    private final OrderDao orderDao = mock(OrderDao.class);
    private final OrderTableDao orderTableDao = mock(OrderTableDao.class);
    private final OrderLineItemDao orderLineItemDao = mock(OrderLineItemDao.class);

    @Nested
    class 주문_등록시 {

        @Test
        void 주문한_메뉴가_하나도_없다면_에외가_발생한다() {
            // given
            final var order = OrderFactory.createOrderOf(1L, 1L);
            final var orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

            // when
            final ThrowingCallable throwingCallable = () -> orderService.create(order);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문한_메뉴가_등록되지_않은_메뉴라면_예외가_발생한다() {
            // given
            final var order = OrderFactory.createOrderOf(1L, 1L, OrderLineItemFactory.createOrderLineItemOf(1L, 1L, 1L, 1L));
            final var orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
            when(menuDao.countByIdIn(List.of(1L))).thenReturn(0L);

            // when
            final ThrowingCallable throwingCallable = () -> orderService.create(order);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문에_사용된_테이블이_등록되있지_않으면_예외가_발생한다() {
            // given
            final var order = OrderFactory.createOrderOf(1L, 1L, OrderLineItemFactory.createOrderLineItemOf(1L, 1L, 1L, 1L));
            final var orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
            when(menuDao.countByIdIn(List.of(1L))).thenReturn(1L);
            when(orderTableDao.findById(1L)).thenReturn(Optional.empty());

            // when
            final ThrowingCallable throwingCallable = () -> orderService.create(order);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문에_사용된_테이블이_비어있다면_예외가_발생한다() {
            // given
            final var order = OrderFactory.createOrderOf(1L, 1L, OrderLineItemFactory.createOrderLineItemOf(1L, 1L, 1L, 1L));
            final var orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
            when(menuDao.countByIdIn(List.of(1L))).thenReturn(1L);
            when(orderTableDao.findById(1L)).thenReturn(Optional.of(OrderTableFactory.createOrderTableOf(0, true)));

            // when
            final ThrowingCallable throwingCallable = () -> orderService.create(order);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_비어있지_않고_메뉴가_존재하면_정상적으로_등록된다() {
            // given
            final var order = OrderFactory.createOrderOf(1L, 1L, OrderLineItemFactory.createOrderLineItemOf(1L, 1L, 1L, 1L));
            final var orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
            when(menuDao.countByIdIn(List.of(1L))).thenReturn(1L);
            when(orderTableDao.findById(1L)).thenReturn(Optional.of(OrderTableFactory.createOrderTableOf(0, false)));
            when(orderLineItemDao.save(order.getOrderLineItems().get(0))).thenReturn(order.getOrderLineItems().get(0));
            when(orderDao.save(order)).thenReturn(order);

            // when
            final var savedOrder = orderService.create(order);

            // then
            assertAll(
                    () -> assertThat(savedOrder.getOrderStatus()).isEqualTo("COOKING"),
                    () -> assertThat(savedOrder.getOrderedTime()).isNotNull(),
                    () -> assertThat(savedOrder.getOrderLineItems()).hasSize(1)
            );
        }
    }

    @Nested
    class 주문_상태_변경시 {

        @Test
        void 계산완료_상태일_경우_더_이상_상태를_변경할_수_없다() {
            // given
            final var orderLineItem = OrderLineItemFactory.createOrderLineItemOf(1L, 1L, 1L, 1L);
            final var previousOrder = OrderFactory.createOrderOf(1L, 1L, orderLineItem);
            previousOrder.setOrderStatus(OrderStatus.COMPLETION.name());
            final var nextOrder = OrderFactory.createOrderOf(1L, 1L, orderLineItem);
            nextOrder.setOrderStatus(OrderStatus.MEAL.name());

            final var orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
            when(orderDao.findById(1L)).thenReturn(Optional.of(previousOrder));
            when(orderLineItemDao.findAllByOrderId(1L)).thenReturn(List.of(orderLineItem));
            when(orderDao.save(previousOrder)).thenReturn(previousOrder);

            // when
            final ThrowingCallable throwingCallable = () -> orderService.changeOrderStatus(1L, nextOrder);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 식사중일_경우_조리중으로_변경할_수_없다() {
            // given
            final var orderLineItem = OrderLineItemFactory.createOrderLineItemOf(1L, 1L, 1L, 1L);
            final var previousOrder = OrderFactory.createOrderOf(1L, 1L, orderLineItem);
            previousOrder.setOrderStatus(OrderStatus.MEAL.name());
            final var nextOrder = OrderFactory.createOrderOf(1L, 1L, orderLineItem);
            nextOrder.setOrderStatus(OrderStatus.COOKING.name());

            final var orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
            when(orderDao.findById(1L)).thenReturn(Optional.of(previousOrder));
            when(orderLineItemDao.findAllByOrderId(1L)).thenReturn(List.of(orderLineItem));
            when(orderDao.save(previousOrder)).thenReturn(previousOrder);

            // when
            final ThrowingCallable throwingCallable = () -> orderService.changeOrderStatus(1L, nextOrder);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @CsvSource(value = {"COOKING: MEAL", "MEAL:COMPLETION"}, delimiter = ':')
        void 변경_가능한_상태일경우_정상적으로_변경한다(OrderStatus previous, OrderStatus next) {
            // given
            final var orderLineItem = OrderLineItemFactory.createOrderLineItemOf(1L, 1L, 1L, 1L);
            final var previousOrder = OrderFactory.createOrderOf(1L, 1L);
            previousOrder.setOrderStatus(previous.name());
            final var nextOrder = OrderFactory.createOrderOf(1L, 1L);
            nextOrder.setOrderStatus(next.name());

            final var orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
            when(orderDao.findById(1L)).thenReturn(Optional.of(previousOrder));
            when(orderLineItemDao.findAllByOrderId(1L)).thenReturn(List.of(orderLineItem));
            when(orderDao.save(previousOrder)).thenReturn(previousOrder);

            // when
            final var savedOrder = orderService.changeOrderStatus(1L, nextOrder);

            // then
            assertThat(savedOrder.getOrderStatus()).isEqualTo(next.name());
        }
    }
}
