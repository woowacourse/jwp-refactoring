package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.supports.MenuFixture;
import kitchenpos.order.domain.model.Order;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderLineItemRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.order.supports.OrderFixture;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.supports.OrderTableFixture;
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
class OrderServiceTest {

    @Mock
    MenuRepository menuRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderTableRepository orderTableRepository;

    @InjectMocks
    OrderService orderService;

    @Nested
    class 주문_생성 {

        @Test
        void 주문_항목의_메뉴들은_전부_DB에_존재해야한다() {
            // given
            OrderCreateRequest request = new OrderCreateRequest(1L, List.of(
                new OrderLineItemRequest(1L, 1L),
                new OrderLineItemRequest(2L, 2L)
            ));

            given(orderTableRepository.findByIdOrThrow(request.getOrderTableId()))
                .willReturn(OrderTableFixture.fixture().build());
            given(orderRepository.save(any(Order.class)))
                .willReturn(OrderFixture.fixture().build());
            given(menuRepository.findAllById(eq(List.of(1L, 2L))))
                .willReturn(List.of(MenuFixture.fixture().id(1L).build()));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올바르지 않은 메뉴입니다.");
        }

        @Test
        void 주문을_생성한다() {
            // given
            OrderCreateRequest request = new OrderCreateRequest(1L, List.of(
                new OrderLineItemRequest(1L, 1L),
                new OrderLineItemRequest(2L, 2L)
            ));

            given(orderTableRepository.findByIdOrThrow(request.getOrderTableId()))
                .willReturn(OrderTableFixture.fixture().build());
            given(orderRepository.save(any(Order.class)))
                .willReturn(OrderFixture.fixture().id(1L).build());
            given(menuRepository.findAllById(eq(List.of(1L, 2L))))
                .willReturn(List.of(
                    MenuFixture.fixture().id(1L).build(),
                    MenuFixture.fixture().id(2L).build()));

            // when
            OrderResponse actual = orderService.create(request);

            // then
            assertThat(actual.getId()).isEqualTo(1L);
        }
    }

    @Nested
    class 주문_전체_조회 {

        @Test
        void 전체_주문을_주문_항목과_함께_조회한다() {
            given(orderRepository.findAllWithFetch())
                .willReturn(List.of(
                    OrderFixture.fixture().id(1L).build(),
                    OrderFixture.fixture().id(2L).build()
                ));

            // when
            List<OrderResponse> actual = orderService.list();

            // then
            assertThat(actual.stream().map(OrderResponse::getId))
                .containsExactly(1L, 2L);
        }
    }
}
