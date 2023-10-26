package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineRequest;
import kitchenpos.dto.request.OrderStatusChangeRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
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
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

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
        void 존재하지_않는_메뉴가_포함되어_있으면_예외() {
            // given
            OrderCreateRequest request = new OrderCreateRequest(1L,
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                List.of(
                    new OrderLineRequest(1L, 10L)
                ));

            given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(
                    OrderTableFixture.builder().build()
                ));

            given(menuRepository.findAllByIdIn(anyList()))
                .willReturn(Collections.emptyList());

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

            given(orderTableRepository.findById(anyLong()))
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

            given(menuRepository.findAllByIdIn(anyList()))
                .willReturn(List.of(
                    MenuFixture.builder()
                        .withId(1L)
                        .withPrice(0L)
                        .build()
                ));

            given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(orderTable));

            // when && then
            assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 테이블은 비어있습니다.");
        }

        @Test
        void 생성_성공() {
            // given
            OrderLineRequest firstOrderLineRequest = new OrderLineRequest(1L, 10L);
            OrderCreateRequest request = new OrderCreateRequest(1L,
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                List.of(firstOrderLineRequest));

            OrderTable orderTable = OrderTableFixture.builder()
                .withEmpty(false)
                .build();

            given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(orderTable));

            given(menuRepository.findAllByIdIn(anyList()))
                .willReturn(List.of(
                    MenuFixture.builder()
                        .withId(1L)
                        .withPrice(0L)
                        .build()
                ));

            long orderId = 1;
            given(orderRepository.save(any())).willReturn(
                OrderFixture.builder()
                    .withId(orderId)
                    .withOrderStatus(OrderStatus.COOKING.name())
                    .withOrderedTime(LocalDateTime.now())
                    .build()
            );

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

    @Test
    void 주문_목록_조회() {
        // given
        long orderId = 1L;
        Order order = OrderFixture.builder()
            .withId(orderId)
            .build();
        given(orderRepository.findAll())
            .willReturn(List.of(
                order
            ));

        // when
        List<OrderResponse> actual = orderService.list();

        // then
        assertThat(actual).hasSize(1);
    }

    @Nested
    class 주문_상태_변경 {

        private long orderId = 1;

        @Test
        void 존재하지_않는_주문이면_예외() {
            // given
            OrderStatusChangeRequest request = new OrderStatusChangeRequest(
                OrderStatus.COMPLETION.name());
            given(orderRepository.findById(anyLong()))
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

            given(orderRepository.findById(anyLong()))
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

            given(orderRepository.findById(anyLong()))
                .willReturn(Optional.of(savedOrder));

            // when
            OrderResponse actual = orderService.changeOrderStatus(orderId, request);

            // then
            assertThat(actual.getOrderStatus()).isEqualTo(completionStatus);
        }
    }
}
