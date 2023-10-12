package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

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
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    MenuDao menuDao;

    @Mock
    OrderDao orderDao;

    @Mock
    OrderLineItemDao orderLineItemDao;

    @Mock
    OrderTableDao orderTableDao;

    @InjectMocks
    OrderService orderService;

    @Nested
    class 주문_생성_테스트 {

        @Test
        void 주문을_정상_생성한다() {
            // given
            Order order = new Order();
            order.setOrderTableId(1L);

            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(2L);
            order.setOrderLineItems(List.of(orderLineItem));

            OrderTable orderTable = new OrderTable();
            orderTable.setId(1L);
            orderTable.setEmpty(false);

            given(menuDao.countByIdIn(anyList())).willReturn(1L);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
            given(orderDao.save(any(Order.class))).willReturn(order);
            given(orderLineItemDao.save(any(OrderLineItem.class))).willReturn(orderLineItem);

            // when
            Order savedOrder = orderService.create(order);

            // then
            SoftAssertions.assertSoftly(softly -> {
                assertThat(savedOrder.getOrderTableId()).isEqualTo(orderTable.getId());
                assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
                assertThat(savedOrder.getOrderedTime()).isNotNull();
                assertThat(savedOrder.getOrderLineItems()).hasSize(1);
            });
        }

        @Test
        void 주문의_주문_항목_정보가_없으면_예외를_반환한다() {
            // given
            Order order = new Order();

            // when, then
            assertThrows(IllegalArgumentException.class, () -> orderService.create(order));
        }

        @Test
        void 주문의_주문_항목에_포함된_메뉴_id가_존재하지_않으면_예외를_반환한다() {
            // given
            Order order = new Order();

            OrderLineItem orderLineItem = new OrderLineItem();
            order.setOrderLineItems(List.of(orderLineItem));

            given(menuDao.countByIdIn(anyList())).willReturn(0L);

            // when, then
            assertThrows(IllegalArgumentException.class, () -> orderService.create(order));
        }

        @Test
        void 주문의_주문_테이블_id가_존재하지_않으면_예외를_반환한다() {
            // given
            Order order = new Order();
            order.setOrderTableId(1L);

            OrderLineItem orderLineItem = new OrderLineItem();
            order.setOrderLineItems(List.of(orderLineItem));

            given(menuDao.countByIdIn(anyList())).willReturn(1L);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

            // when, then
            assertThrows(IllegalArgumentException.class, () -> orderService.create(order));
        }

        @Test
        void 주문의_주문_테이블이_빈_테이블이면_예외를_반환한다() {
            // given
            Order order = new Order();
            order.setOrderTableId(1L);

            OrderLineItem orderLineItem = new OrderLineItem();
            order.setOrderLineItems(List.of(orderLineItem));

            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);

            given(menuDao.countByIdIn(anyList())).willReturn(1L);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));

            // when, then
            assertThrows(IllegalArgumentException.class, () -> orderService.create(order));
        }
    }

    @Test
    void 주문을_전체_조회한다() {
        // given
        Order order = new Order();
        order.setId(1L);

        OrderLineItem orderLineItem = new OrderLineItem();

        given(orderDao.findAll()).willReturn(List.of(order));
        given(orderLineItemDao.findAllByOrderId(anyLong())).willReturn(List.of(orderLineItem));

        // when
        List<Order> orders = orderService.list();

        // then
        SoftAssertions.assertSoftly(softly -> {
            assertThat(orders).isNotEmpty();
            assertThat(orders.get(0).getOrderLineItems()).isNotEmpty();
        });
    }

    @Nested
    class 주문_상태_변경_테스트 {

        @Test
        void 주문의_상태를_정상_변경한다() {
            // given
            Order originalOrder = new Order();
            originalOrder.setId(1L);
            originalOrder.setOrderStatus(OrderStatus.COOKING.name());

            Order newOrder = new Order();
            newOrder.setId(1L);
            newOrder.setOrderStatus(OrderStatus.MEAL.name());

            given(orderDao.findById(anyLong())).willReturn(Optional.of(originalOrder));

            // when
            Order changedOrder = orderService.changeOrderStatus(newOrder.getId(), newOrder);

            // then
            assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
        }

        @Test
        void 변경하려는_주문_id에_해당하는_주문이_존재하지_않으면_예외를_반환한다() {
            // given
            given(orderDao.findById(anyLong())).willReturn(Optional.empty());

            // when, then
            assertThrows(IllegalArgumentException.class,
                    () -> orderService.changeOrderStatus(1L, new Order()));
        }

        @Test
        void 변경하려는_주문의_주문_상태가_계산_완료인_경우_예외를_반환한다() {
            // given
            Order originalOrder = new Order();
            originalOrder.setOrderStatus(OrderStatus.COMPLETION.name());

            given(orderDao.findById(anyLong())).willReturn(Optional.of(originalOrder));

            // when, then
            assertThrows(IllegalArgumentException.class,
                    () -> orderService.changeOrderStatus(1L, new Order()));
        }
    }
}
