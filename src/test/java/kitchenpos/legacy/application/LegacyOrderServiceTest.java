package kitchenpos.legacy.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyList;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderStatus;
import kitchenpos.legacy.dao.MenuDao;
import kitchenpos.legacy.dao.OrderDao;
import kitchenpos.legacy.dao.OrderLineItemDao;
import kitchenpos.legacy.dao.OrderTableDao;
import kitchenpos.legacy.domain.Order;
import kitchenpos.legacy.domain.OrderLineItem;
import kitchenpos.legacy.domain.OrderTable;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class LegacyOrderServiceTest {

    @InjectMocks
    LegacyOrderService orderService;

    @Mock
    MenuDao menuDao;

    @Mock
    OrderDao orderDao;

    @Mock
    OrderLineItemDao orderLineItemDao;

    @Mock
    OrderTableDao orderTableDao;

    @Nested
    class create {

        @Test
        void 주문_항목_목록이_비어있으면_예외() {
            // given
            Order order = new Order();

            // when
            order.setOrderLineItems(Collections.emptyList());

            // then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_항목_목록의_메뉴_식별자의_갯수와_주문_항목_목록의_갯수가_맞지_않으면_예외() {
            // given
            Order order = new Order();
            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(1L);
            order.setOrderLineItems(List.of(orderLineItem));

            // when
            given(menuDao.countByIdIn(anyList()))
                .willReturn(0L);

            // then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블_식별자에_대한_주문_테이블이_없으면_예외() {
            // given
            Order order = new Order();
            order.setOrderTableId(1L);
            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(1L);
            order.setOrderLineItems(List.of(orderLineItem));
            given(menuDao.countByIdIn(anyList()))
                .willReturn(1L);

            // when
            given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.empty());

            // then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 성공() {
            // given
            Order order = new Order();
            order.setOrderTableId(1L);
            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(1L);
            order.setOrderLineItems(List.of(orderLineItem));
            OrderTable orderTable = new OrderTable();
            orderTable.setId(1L);

            given(menuDao.countByIdIn(anyList()))
                .willReturn(1L);
            given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(orderTable));
            Order savedOrder = new Order();
            savedOrder.setId(1L);
            given(orderDao.save(any(Order.class)))
                .willReturn(savedOrder);
            OrderLineItem savedOrderLineItem = new OrderLineItem();
            given(orderLineItemDao.save(any(OrderLineItem.class)))
                .willReturn(savedOrderLineItem);

            // when & then
            assertThatNoException()
                .isThrownBy(() -> orderService.create(order));
        }
    }

    @Nested
    class findAll {

        @Test
        void 성공() {
            // given
            Order order = new Order();
            order.setId(1L);
            given(orderDao.findAll())
                .willReturn(List.of(order));
            given(orderLineItemDao.findAllByOrderId(1L))
                .willReturn(List.of(new OrderLineItem(), new OrderLineItem()));

            // when
            List<Order> actual = orderService.findAll();

            // then
            assertThat(actual).hasSize(1);
            assertThat(actual.get(0).getOrderLineItems()).hasSize(2);
        }
    }

    @Nested
    class changeOrderStatus {

        @Test
        void 주문_식별자에_대한_주문이_없으면_예외() {
            // given
            Order order = new Order();
            Long orderId = 1L;

            // when
            given(orderDao.findById(anyLong()))
                .willReturn(Optional.empty());

            // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, order))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 찾은_주문이_COMPLETION_상태이면_예외() {
            // given
            Order order = new Order();
            Long orderId = 1L;
            Order savedOrder = new Order();
            given(orderDao.findById(1L))
                .willReturn(Optional.of(savedOrder));

            // when
            savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());

            // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, order))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 성공() {
            // given
            Order order = new Order();
            order.setOrderStatus(OrderStatus.COMPLETION.name());
            Long orderId = 1L;
            Order savedOrder = new Order();
            savedOrder.setOrderStatus(OrderStatus.COOKING.name());
            given(orderDao.findById(1L))
                .willReturn(Optional.of(savedOrder));
            given(orderLineItemDao.findAllByOrderId(1L))
                .willReturn(List.of(new OrderLineItem()));

            // when
            Order actual = orderService.changeOrderStatus(orderId, order);

            // then
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
        }
    }
}
