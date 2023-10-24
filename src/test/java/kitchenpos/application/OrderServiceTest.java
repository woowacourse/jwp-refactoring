package kitchenpos.application;

import kitchenpos.application.dto.request.CreateOrderRequest;
import kitchenpos.application.dto.request.UpdateOrderStatusRequest;
import kitchenpos.application.dto.response.CreateOrderResponse;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.mapper.OrderMapper;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static kitchenpos.fixture.OrderFixture.*;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
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

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @Nested
    class 주문_생성 {

        @Test
        void 주문을_생성한다() {
            // given
            Order order = ORDER.주문_요청_조리중();
            OrderLineItem orderLineItem = ORDER_LINE_ITEM.주문_항목_아이템_1개(order.getId());
            CreateOrderRequest request = REQUEST.주문_생성_요청_주문항목(List.of(orderLineItem.getSeq()));
            given(orderTableRepository.findById(request.getOrderTableId()))
                    .willReturn(Optional.of(ORDER_TABLE.주문_테이블_1_비어있는가(false)));

            given(menuRepository.countByIdIn(anyList()))
                    .willReturn((long) request.getOrderLineItemIds().size());

            given(orderLineItemRepository.findById(anyLong()))
                    .willReturn(Optional.of(orderLineItem));

            given(orderMapper.toOrder(any(CreateOrderRequest.class), anyList()))
                    .willReturn(order);

            given(orderRepository.save(any(Order.class)))
                    .willReturn(order);

            given(orderLineItemRepository.save(any(OrderLineItem.class)))
                    .willReturn(orderLineItem);

            // when
            CreateOrderResponse result = orderService.create(request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(result.getId()).isEqualTo(order.getId());
                softly.assertThat(result.getOrderStatus()).isEqualTo(order.getOrderStatus().name());
                softly.assertThat(result.getOrderedTime()).isEqualTo(order.getOrderedTime());
                softly.assertThat(result.getOrderTableId()).isEqualTo(order.getOrderTable().getId());
                softly.assertThat(result.getOrderLineItems().get(0).getMenuId()).isEqualTo(orderLineItem.getMenu().getId());
                softly.assertThat(result.getOrderLineItems().get(0).getQuantity()).isEqualTo(orderLineItem.getQuantity());
            });
        }

        @Test
        void 주문_항목이_비어있으면_예외() {
            // given
            CreateOrderRequest request = REQUEST.주문_생성_요청_비어있는_주문항목();

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_항목의_개수와_메뉴의_개수가_일치하지_않으면_예외() {
            // given
            CreateOrderRequest request = REQUEST.주문_생성_요청();

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_존재하지_않으면_예외() {
            // given
            CreateOrderRequest request = REQUEST.주문_생성_요청();

            given(orderTableRepository.findById(request.getOrderTableId()))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문에_빈_테이블이_포함되어있면_예외() {
            // given
            CreateOrderRequest request = REQUEST.주문_생성_요청();

            given(orderTableRepository.findById(request.getOrderTableId()))
                    .willReturn(Optional.of(ORDER_TABLE.주문_테이블_1_비어있는가(true)));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 주문_조회 {

        @Test
        void 주문_목록을_조회한다() {
            // given
            Order order = ORDER.주문_요청_조리중();
            given(orderRepository.findAll())
                    .willReturn(List.of(order));

            // when
            List<OrderResponse> result = orderService.list();

            // then
            assertSoftly(
                    softly -> {
                        softly.assertThat(result.get(0).getOrderLineItems()).hasSize(order.getOrderLineItems().size());
                        softly.assertThat(result.get(0).getOrderStatus()).isEqualTo(order.getOrderStatus().name());
                        softly.assertThat(result.get(0).getOrderedTime()).isEqualTo(order.getOrderedTime().toString());
                        softly.assertThat(result.get(0).getOrderTableId()).isEqualTo(order.getOrderTable().getId());
                    }
            );
        }
    }

    @Nested
    class 주문_상태_변경 {

        @Test
        void 주문_상태를_변경한다() {
            // given
            Order order = ORDER.주문_요청_조리중();
            UpdateOrderStatusRequest request = REQUEST.주문_상태_변경_요청(OrderStatus.COMPLETION.name());
            given(orderRepository.findById(anyLong()))
                    .willReturn(Optional.of(order));
            given(orderRepository.save(any(Order.class)))
                    .willReturn(order.updateStatus(OrderStatus.valueOf(request.getOrderStatus())));

            // when
            OrderResponse result = orderService.changeOrderStatus(order.getId(), request);

            // then
            SoftAssertions.assertSoftly(
                    softly -> {
                        softly.assertThat(result.getOrderStatus()).isEqualTo(request.getOrderStatus());
                        softly.assertThat(result.getOrderedTime()).isEqualTo(order.getOrderedTime().toString());
                        softly.assertThat(result.getOrderTableId()).isEqualTo(order.getOrderTable().getId());
                    }
            );
        }

        @Test
        void 주문_상태가_올바르지_않으면_예외() {
            // given
            Order order = ORDER.주문_요청_계산_완료();
            UpdateOrderStatusRequest request = REQUEST.주문_상태_변경_요청(OrderStatus.COMPLETION.name());
            given(orderRepository.findById(anyLong()))
                    .willReturn(Optional.of(order));

            // when & then
            Long id = order.getId();
            assertThatThrownBy(() -> orderService.changeOrderStatus(id, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest(name = "{0} -> {1}이면 예외")
        @CsvSource(value = {"COMPLETION,MEAL", "COMPLETION,COOKING"}, delimiter = ',')
        void 계산_완료_상태에서_주문을_변경하면_예외(OrderStatus previous, OrderStatus current) {
            // given
            Order order = ORDER.주문_요청_현재상태는(previous);
            UpdateOrderStatusRequest request = REQUEST.주문_상태_변경_요청(current.name());
            given(orderRepository.findById(anyLong()))
                    .willReturn(Optional.of(order));

            // when & then
            Long id = order.getId();
            assertThatThrownBy(() -> orderService.changeOrderStatus(id, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
