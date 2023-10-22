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
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineRequest;
import kitchenpos.dto.request.OrderStatusChangeRequest;
import kitchenpos.dto.response.OrderResponse;
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
        void 주문_항목이_없으면_예외() {
            // given
            OrderCreateRequest request = new OrderCreateRequest(1L,
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                null);

            // when && then
            assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 같은_메뉴에_대한_주문_항목이_있으면_예외() {
            // given
            OrderCreateRequest request = new OrderCreateRequest(1L,
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                List.of(
                    new OrderLineRequest(1L, 10L),
                    new OrderLineRequest(1L, 5L)
                ));

            given(menuDao.countByIdIn(anyList()))
                .willReturn(1L);

            // when && then
            assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_주문_테이블이면_예외() {
            // given
            OrderCreateRequest request = new OrderCreateRequest(1L,
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                List.of(
                    new OrderLineRequest(1L, 10L),
                    new OrderLineRequest(1L, 5L)
                ));

            given(menuDao.countByIdIn(anyList()))
                .willReturn(2L);

            given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.empty());

            // when && then
            assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 비어있는_주문_테이블을_보내면_예외() {
            // given
            OrderCreateRequest request = new OrderCreateRequest(1L,
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                List.of(new OrderLineRequest(1L, 10L)));

            OrderTable orderTable = OrderTableFixture.builder()
                .withEmpty(true)
                .build();

            given(menuDao.countByIdIn(anyList()))
                .willReturn(1L);

            given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(orderTable));

            // when && then
            assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 테이블은 비어있습니다.");
        }

        @Test
        void 생성_성공() {
            {
                // given
                OrderLineRequest firstOrderLineRequest = new OrderLineRequest(1L, 10L);
                OrderLineRequest secondOrderLineRequest = new OrderLineRequest(2L, 5L);
                OrderCreateRequest request = new OrderCreateRequest(1L,
                    OrderStatus.COOKING.name(),
                    LocalDateTime.now(),
                    List.of(
                        firstOrderLineRequest,
                        secondOrderLineRequest
                    ));

                OrderTable orderTable = OrderTableFixture.builder()
                    .withEmpty(false)
                    .build();

                given(menuDao.countByIdIn(anyList()))
                    .willReturn(2L);

                given(orderTableDao.findById(anyLong()))
                    .willReturn(Optional.of(orderTable));

                long orderId = 1L;
                given(orderDao.save(any())).willReturn(
                    OrderFixture.builder()
                        .withId(orderId)
                        .withOrderStatus(OrderStatus.COOKING.name())
                        .withOrderedTime(LocalDateTime.now())
                        .build()
                );

                given(orderLineItemDao.save(any()))
                    .willReturn(toEntity(1L, orderId, firstOrderLineRequest),
                        toEntity(2L, orderId, secondOrderLineRequest));

                // when
                OrderResponse actual = orderService.create(request);

                // then
                assertSoftly(softAssertions -> {
                    assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
                    assertThat(actual.getOrderedTime()).isNotNull();
                    assertThat(actual.getOrderLineItems())
                        .allMatch(orderLineItem -> orderLineItem.getOrderId().equals(orderId));
                });
            }
        }

        private OrderLineItem toEntity(Long seq, Long orderId, OrderLineRequest orderLineRequest) {
            return new OrderLineItem(seq, orderLineRequest.getMenuId(), new Order(orderId, null, null, null, null), orderLineRequest.getQuantity());
        }
    }

    @Test
    void 주문_목록_조회() {
        // given
        long orderId = 1L;
        Order order = OrderFixture.builder()
            .withId(orderId)
            .build();
        given(orderDao.findAll())
            .willReturn(List.of(
                order
            ));

        OrderLineItem orderLineItem = OrderLineItemFixture.builder()
            .withOrderId(orderId)
            .build();
        given(orderLineItemDao.findAllByOrderId(anyLong()))
            .willReturn(
                List.of(orderLineItem)
            );

        // when
        List<OrderResponse> actual = orderService.list();

        // then
        assertSoftly(softAssertions -> {
            assertThat(actual).hasSize(1);
            assertThat(actual).allMatch(od -> od.getOrderLineItems().size() == 1);
        });
    }

    @Nested
    class 주문_상태_변경 {

        private long orderId = 1;

        @Test
        void 존재하지_않는_주문이면_예외() {
            // given
            OrderStatusChangeRequest request = new OrderStatusChangeRequest(
                OrderStatus.COMPLETION.name());
            given(orderDao.findById(anyLong()))
                .willReturn(Optional.empty());

            // when && then
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문이_완료된_상태라면_예외() {
            // given
            OrderStatusChangeRequest request = new OrderStatusChangeRequest(
                OrderStatus.COMPLETION.name());
            Order savedOrder = OrderFixture.builder()
                .withOrderStatus(OrderStatus.COMPLETION.name())
                .build();

            given(orderDao.findById(anyLong()))
                .willReturn(Optional.of(savedOrder));

            // when && then
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 변경_성공() {
            // given
            String completionStatus = OrderStatus.COMPLETION.name();
            OrderStatusChangeRequest request = new OrderStatusChangeRequest(
                completionStatus);
            Order savedOrder = OrderFixture.builder()
                .withOrderStatus(OrderStatus.COOKING.name())
                .build();

            given(orderDao.findById(anyLong()))
                .willReturn(Optional.of(savedOrder));

            given(orderDao.save(any()))
                .willReturn(OrderFixture.builder()
                    .withOrderStatus(completionStatus)
                    .build());

            given(orderLineItemDao.findAllByOrderId(anyLong()))
                .willReturn(List.of(OrderLineItemFixture.builder().build()));

            // when
            OrderResponse actual = orderService.changeOrderStatus(orderId, request);

            // then
            assertThat(actual.getOrderStatus()).isEqualTo(completionStatus);
        }
    }
}
