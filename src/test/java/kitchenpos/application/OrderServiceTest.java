package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderChangeRequest;
import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderLineItemDto;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Nested
    class create는 {

        private final Long id = 1L;
        private final Long orderTableId = 11L;
        private final OrderStatus orderStatus = COOKING;
        private final LocalDateTime orderedTime = LocalDateTime.now();
        private final OrderLineItem orderLineItem = new OrderLineItem(1L, 3L);
        private final List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);

        @Test
        void order_line_itmes가_비어있으면_예외를_반환한다() {
            OrderCreateRequest request = 주문_생성_dto를_만든다(id, orderTableId, orderStatus, orderedTime,
                    Arrays.asList(new OrderLineItem(1L, 3L)));
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void order_line_itmes_크기와_메뉴_ID의_개수가_맞지_않으면_예외를_반환한다() {
            // given
            OrderCreateRequest request = 주문_생성_dto를_만든다(id, orderTableId, orderStatus, orderedTime, orderLineItems);
            when(menuRepository.countByIdIn(any())).thenReturn(3L);

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void order를_생성할_수_있다() {
            // given
            OrderCreateRequest request = 주문_생성_dto를_만든다(id, orderTableId, orderStatus, orderedTime, orderLineItems);
            when(menuRepository.countByIdIn(any())).thenReturn(Long.valueOf(orderLineItems.size()));

            Order order = request.toOrder();
            when(orderRepository.save(any(Order.class))).thenReturn(order);

            // when
            OrderResponse response = orderService.create(request);

            // then
            Assertions.assertAll(
                    () -> assertThat(response.getOrderTableId()).isEqualTo(order.getOrderTableId()),
                    () -> assertThat(response.getOrderStatus()).isEqualTo(order.getOrderStatus().name()),
                    () -> assertThat(response.getOrderedTime()).isEqualTo(order.getOrderedTime())
            );
        }

        private OrderCreateRequest 주문_생성_dto를_만든다(final Long id,
                                                  final Long orderTableId,
                                                  final OrderStatus orderStatus,
                                                  final LocalDateTime orderedTime,
                                                  final List<OrderLineItem> orderLineItems) {
            return new OrderCreateRequest(
                    orderTableId,
                    orderLineItems.stream()
                            .map(OrderLineItemDto::new)
                            .collect(Collectors.toList()));
        }
    }

    @Nested
    class list는 {

        private final OrderLineItem orderLineItem = new OrderLineItem(1L, 3L);
        private final List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);

        @Test
        void order_목록을_조회할_수_있다() {
            // given
            Order order = new Order(1L, orderLineItems);
            when(orderRepository.findAllWithOrderLineItems()).thenReturn(Arrays.asList(order));

            // when
            List<OrderResponse> responses = orderService.list();

            // then
            assertThat(responses).hasSize(1);
        }
    }

    @Nested
    class change_status는 {
        @Test
        void 일치하는_order_id가_없을_경우_예외를_반환한다() {
            // given
            Long notExistOrderId = 1234L;
            when(orderRepository.findById(notExistOrderId)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(notExistOrderId, new OrderChangeRequest()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 이미_완료상태이면_예외를_반환한다() {
            // given
            Order order = order_객체를_생성한다(1L, Arrays.asList(new OrderLineItem(1L, 3L)));
            Long orderId = 1L;
            order.changeOrderStatus(COMPLETION);
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, new OrderChangeRequest(COOKING.name())))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void aorder_status를_변경할_수_있다() {
            // given
            Order order = order_객체를_생성한다(1L, Arrays.asList(new OrderLineItem(1L, 3L)));
            when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

            // when
            OrderResponse response = orderService.changeOrderStatus(1L, new OrderChangeRequest("COMPLETION"));

            // then
            assertThat(response.getOrderStatus()).isEqualTo(COMPLETION.name());
        }

        private Order order_객체를_생성한다(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
            return new Order(orderTableId, orderLineItems);
        }
    }
}
