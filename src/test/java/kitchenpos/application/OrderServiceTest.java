package kitchenpos.application;

import kitchenpos.application.dto.OrderLineItemDto;
import kitchenpos.application.dto.OrderStatusDto;
import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.domain.*;
import kitchenpos.persistence.MenuRepository;
import kitchenpos.persistence.OrderLineItemRepository;
import kitchenpos.persistence.OrderRepository;
import kitchenpos.persistence.OrderTableRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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

    @InjectMocks
    private OrderService orderService;

    @Nested
    class 주문_생성 {
        @Test
        void 주문을_생성한다() {
            // given
            final OrderTable savedOrderTable = new OrderTable(1L, null, 0, false);
            final Order savedOrder = new Order(1L, savedOrderTable, OrderStatus.COOKING);
            final Menu savedMenu = new Menu();
            final OrderLineItem savedOrderLineItem = new OrderLineItem();

            when(menuRepository.countByIdIn(any()))
                    .thenReturn(2L);
            when(orderTableRepository.findById(anyLong()))
                    .thenReturn(Optional.of(savedOrderTable));
            when(orderRepository.save(any(Order.class)))
                    .thenReturn(savedOrder);
            when(menuRepository.findById(anyLong()))
                    .thenReturn(Optional.of(savedMenu));
            when(orderLineItemRepository.saveAll(any()))
                    .thenReturn(List.of(savedOrderLineItem));

            // when
            final OrderCreateRequest request = new OrderCreateRequest(1L,
                    List.of(new OrderLineItemDto(1L, 1L), new OrderLineItemDto(2L, 1L)));

            final Order result = orderService.create(request);

            // then
            assertThat(result.getId()).isEqualTo(1);
        }

        @Test
        void 주문을_생성할_때_주문_항목이_없다면_실패한다() {
            // given
            final OrderCreateRequest request = new OrderCreateRequest(1L, Collections.emptyList());

            // when, then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문을_생성할_때_전달한_주문_항목이_DB에_존재하지_않으면_실패한다() {
            // given
            when(menuRepository.countByIdIn(any()))
                    .thenReturn(1L);

            // when
            final OrderCreateRequest request = new OrderCreateRequest(1L,
                    List.of(new OrderLineItemDto(1L, 1L), new OrderLineItemDto(2L, 1L)));

            // then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문을_생성할_때_전달한_주문_테이블이_DB에_존재하지_않으면_실패한다() {
            // given
            when(menuRepository.countByIdIn(any()))
                    .thenReturn(2L);
            when(orderTableRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            // when
            final OrderCreateRequest request = new OrderCreateRequest(1L,
                    List.of(new OrderLineItemDto(1L, 1L), new OrderLineItemDto(2L, 1L)));

            // then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문을_생성할_때_주문_테이블이_빈_상태면_실패한다() {
            // given
            final OrderTable savedOrderTable = new OrderTable(null, null, 0, true);

            when(menuRepository.countByIdIn(any()))
                    .thenReturn(2L);
            when(orderTableRepository.findById(anyLong()))
                    .thenReturn(Optional.of(savedOrderTable));

            // when
            final OrderCreateRequest request = new OrderCreateRequest(1L,
                    List.of(new OrderLineItemDto(1L, 1L), new OrderLineItemDto(2L, 1L)));

            // then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 전체_주문_목록을_조회한다() {
        // given
        final Order savedOrder = new Order();

        when(orderRepository.findAll())
                .thenReturn(List.of(savedOrder));

        // when
        final List<Order> result = orderService.list();

        // then
        assertThat(result).hasSize(1);
    }

    @Nested
    class 주문_상태_변경 {
        @Test
        void 주문_상태를_변경한다() {
            // given
            final OrderTable orderTable = new OrderTable(1L, null, 0, false);
            final Order savedOrder = new Order(1L, orderTable, OrderStatus.MEAL);
            final Order updatedOrder = new Order(1L, orderTable, OrderStatus.COMPLETION);

            when(orderRepository.findById(anyLong()))
                    .thenReturn(Optional.of(savedOrder));
            when(orderRepository.save(any(Order.class)))
                    .thenReturn(updatedOrder);

            // when
            final Order result = orderService.changeOrderStatus(1L, new OrderStatusDto("COMPLETION"));

            // then
            assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
        }

        @Test
        void 주문_상태_변경_시_전달한_주문_아이디가_DB에_존재하지_않으면_실패한다() {
            // given
            when(orderRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new OrderStatusDto("COMPLETION")))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_상태_변경_시_변경하려는_주문의_상태가_COMPLETION이면_실패한다() {
            // given
            final OrderTable orderTable = new OrderTable(1L, null, 0, false);
            final Order savedOrder = new Order(1L, orderTable, OrderStatus.COMPLETION);

            when(orderRepository.findById(anyLong()))
                    .thenReturn(Optional.of(savedOrder));

            // when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new OrderStatusDto("COMPLETION")))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}