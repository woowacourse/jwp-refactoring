package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.service.OrderValidator;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.order.supports.OrderFixture;
import kitchenpos.table.domain.repository.OrderTableRepository;
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
    OrderRepository orderRepository;

    @Mock
    OrderTableRepository orderTableRepository;

    @Mock
    OrderValidator orderValidator;

    @InjectMocks
    OrderService orderService;

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
