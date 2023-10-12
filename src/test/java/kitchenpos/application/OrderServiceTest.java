package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import kitchenpos.annotation.MockTest;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;

@MockTest
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

    @Nested
    class 주문_저장 {

        @Test
        void 주문_항목이_없으면_예외가_발생한다() {
            // given
            Order order = new Order();

            // then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);

        }

        @Test
        void 요청된_주문에_메뉴가_DB에_저장되어_있지_않으면_예외를_발생한다() {
            // given
            Order order = new Order();
            order.setOrderLineItems(List.of(new OrderLineItem(), new OrderLineItem()));

            when(menuDao.countByIdIn(anyList())).thenReturn(1L);

            // then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청된_주문_테이블이_DB에_저장되어_있지_않으면_예외를_발생한다() {
            // given
            Order order = new Order();
            order.setOrderTableId(10L);
            order.setOrderLineItems(List.of(new OrderLineItem(), new OrderLineItem()));
            when(menuDao.countByIdIn(anyList())).thenReturn(2L);

            when(orderTableDao.findById(order.getOrderTableId())).thenReturn(Optional.empty());

            // then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_비어있는_상태이면_예외를_발생한다() {
            // given
            Order order = new Order();
            order.setOrderTableId(10L);
            order.setOrderLineItems(List.of(new OrderLineItem(), new OrderLineItem()));
            when(menuDao.countByIdIn(anyList())).thenReturn(2L);

            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);
            when(orderTableDao.findById(order.getOrderTableId())).thenReturn(Optional.of(orderTable));

            // then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_상태를_COOKING으로_변경한다() {
            // given
            Order order = new Order();
            order.setOrderTableId(10L);
            order.setOrderLineItems(List.of(new OrderLineItem(), new OrderLineItem()));
            when(menuDao.countByIdIn(anyList())).thenReturn(2L);

            OrderTable orderTable = new OrderTable();
            orderTable.setId(1L);
            when(orderTableDao.findById(order.getOrderTableId())).thenReturn(Optional.of(orderTable));

            order.setId(1L);
            when(orderDao.save(any(Order.class))).thenReturn(order);

            // when
            orderService.create(order);

            // then
            assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        }

        // TODO: 현재는 LocalDateTime.now() 호출로 Mocking 불가. 프로덕션 코드 수정 후 테스트 적용
        @Test
        @Disabled
        void 주문_시간은_현재_시각으로_설정이다() {
        }

        @Test
        void 주문_항목에_주문_ID를_설정하고_DB에_저장한다() {
            // given
            Order order = new Order();
            order.setOrderTableId(10L);
            order.setOrderLineItems(List.of(new OrderLineItem(), new OrderLineItem()));
            when(menuDao.countByIdIn(anyList())).thenReturn(2L);

            OrderTable orderTable = new OrderTable();
            orderTable.setId(1L);
            when(orderTableDao.findById(order.getOrderTableId())).thenReturn(Optional.of(orderTable));

            order.setId(1L);
            when(orderDao.save(any(Order.class))).thenReturn(order);

            // when
            orderService.create(order);

            // then
            assertThat(order.getOrderTableId()).isEqualTo(orderTable.getId());
        }
    }

    @Nested
    class 모든_주문을_조회한다 {

        @Test
        void 모든_주문을_성공적으로_조회한다() {
            // when
            orderService.list();

            // then
            verify(orderDao).findAll();
        }
    }

    @Nested
    class 주문_상태를_변경한다 {

        @Test
        void 주문이_저장되어_있지_않으면_예외가_발생한다() {
            // given
            when(orderDao.findById(anyLong())).thenReturn(Optional.empty());

            // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new Order()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문의_상태가_이미_계산완료면_예외가_발생한다() {
            // given
            Order order = new Order();
            order.setId(1L);
            order.setOrderStatus(OrderStatus.COMPLETION.name());

            when(orderDao.findById(anyLong())).thenReturn(Optional.of(order));

            // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
