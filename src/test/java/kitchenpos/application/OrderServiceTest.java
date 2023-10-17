package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
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
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
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

    @Captor
    ArgumentCaptor<Order> orderArgumentCaptor;

    @Captor
    ArgumentCaptor<OrderLineItem> orderLineItemArgumentCaptor;

    @InjectMocks
    private OrderService orderService;

    @Nested
    class 주문_생성 {

        @Test
        void 주문_항목이_없으면_예() {
            // given
            Order order = OrderFixture.builder()
                .build();

            // when && then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 같은_메뉴에_대한_주문_항목이_있으면_예외() {
            // given
            OrderLineItem firstOderLineItem = OrderLineItemFixture.builder()
                .withMenuId(1L)
                .build();

            OrderLineItem secondOrderLineItem = OrderLineItemFixture.builder()
                .withMenuId(1L)
                .build();

            Order order = OrderFixture.builder()
                .withOrderLineItems(List.of(firstOderLineItem, secondOrderLineItem))
                .build();

            given(menuDao.countByIdIn(anyList()))
                .willReturn(1L);

            // when && then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_주문_테이블이면_예외() {
            // given
            OrderLineItem firstOderLineItem = OrderLineItemFixture.builder()
                .withMenuId(1L)
                .build();

            OrderLineItem secondOrderLineItem = OrderLineItemFixture.builder()
                .withMenuId(2L)
                .build();

            long notExistenceOrderTable = 1L;
            Order order = OrderFixture.builder()
                .withOrderTableId(notExistenceOrderTable)
                .withOrderLineItems(List.of(firstOderLineItem, secondOrderLineItem))
                .build();

            given(menuDao.countByIdIn(anyList()))
                .willReturn(2L);

            given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.empty());

            // when && then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 비어있는_주문_테이블을_보내면_예외() {
            // given
            OrderLineItem firstOderLineItem = OrderLineItemFixture.builder()
                .withMenuId(1L)
                .build();

            OrderLineItem secondOrderLineItem = OrderLineItemFixture.builder()
                .withMenuId(2L)
                .build();

            long emptyOrderTable = 1L;
            Order order = OrderFixture.builder()
                .withOrderTableId(emptyOrderTable)
                .withOrderLineItems(List.of(firstOderLineItem, secondOrderLineItem))
                .build();

            OrderTable orderTable = OrderTableFixture.builder()
                .withEmpty(true)
                .build();

            given(menuDao.countByIdIn(anyList()))
                .willReturn(2L);

            given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(orderTable));

            // when && then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 테이블은 비어있습니다.");
        }
    }

    @Test
    void 생성_성공() {
        {
            // given
            OrderLineItem firstOderLineItem = OrderLineItemFixture.builder()
                .withSeq(1L)
                .withMenuId(1L)
                .build();

            OrderLineItem secondOrderLineItem = OrderLineItemFixture.builder()
                .withSeq(2L)
                .withMenuId(2L)
                .build();

            long orderId = 1L;
            OrderFixture orderFixture = OrderFixture.builder()
                .withId(orderId)
                .withOrderTableId(1L)
                .withOrderLineItems(List.of(firstOderLineItem, secondOrderLineItem));
            Order order = orderFixture
                .build();

            OrderTable orderTable = OrderTableFixture.builder()
                .withEmpty(false)
                .build();

            given(menuDao.countByIdIn(anyList()))
                .willReturn(2L);

            given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(orderTable));

            given(orderDao.save(any())).willReturn(
                orderFixture
                    .withOrderStatus(OrderStatus.COOKING.name())
                    .withOrderedTime(LocalDateTime.now())
                    .build()
            );

            given(orderLineItemDao.save(any()))
                .willReturn(firstOderLineItem, secondOrderLineItem);

            // when
            Order actual = orderService.create(order);

            // then
            assertSoftly(softAssertions -> {
                assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
                assertThat(actual.getOrderedTime()).isNotNull();
                assertThat(actual.getOrderLineItems())
                    .allMatch(orderLineItem -> orderLineItem.getOrderId().equals(orderId));
            });
        }
    }

    @Test
    void 주문_목록_조회() {
        // given
        Order order = OrderFixture.builder()
            .withId(1L)
            .build();
        given(orderDao.findAll())
            .willReturn(List.of(
                order,
                order
            ));

        OrderLineItem orderLineItem = OrderLineItemFixture.builder().build();
        given(orderLineItemDao.findAllByOrderId(anyLong()))
            .willReturn(
                List.of(orderLineItem, orderLineItem),
                List.of(orderLineItem, orderLineItem)
            );

        // when
        List<Order> orders = orderService.list();

        // then
        assertSoftly(softAssertions -> {
            assertThat(orders).hasSize(2);
            assertThat(orders).allMatch(od -> od.getOrderLineItems().size() != 0);
        });
    }

    @Nested
    class 주문_상태_변경 {

        private long orderId = 1;

        @Test
        void 존재하지_않는_주문이면_예외() {
            // given
            Order requestOrder = OrderFixture.builder().build();
            given(orderDao.findById(anyLong()))
                .willReturn(Optional.empty());

            // when && then
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, requestOrder))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문이_완료된_상태라면_예외() {
            // given
            Order requestOrder = OrderFixture.builder().build();
            Order savedOrder = OrderFixture.builder()
                .withOrderStatus(OrderStatus.COMPLETION.name())
                .build();

            given(orderDao.findById(anyLong()))
                .willReturn(Optional.of(savedOrder));

            // when && then
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, requestOrder))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 변경_성공() {
            // given
            String changeOrderStatus = OrderStatus.MEAL.name();
            Order requestOrder = OrderFixture.builder()
                .withOrderStatus(changeOrderStatus)
                .build();
            Order savedOrder = OrderFixture.builder()
                .withOrderStatus(OrderStatus.COOKING.name())
                .build();

            given(orderDao.findById(anyLong()))
                .willReturn(Optional.of(savedOrder));

            given(orderDao.save(any()))
                .willReturn(requestOrder);

            given(orderLineItemDao.findAllByOrderId(anyLong()))
                .willReturn(List.of(OrderLineItemFixture.builder().build()));

            // when
            Order actual = orderService.changeOrderStatus(orderId, requestOrder);

            // then
            assertThat(actual.getOrderStatus()).isEqualTo(changeOrderStatus);
        }
    }
}
