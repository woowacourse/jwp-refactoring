package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.anyList;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.common.domain.ValidResult;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class OrderTableUngroupOrderValidatorTest {

    @InjectMocks
    OrderTableUngroupOrderValidator validator;

    @Mock
    OrderRepository orderRepository;

    @EnumSource(value = OrderStatus.class, mode = Mode.EXCLUDE, names = "COMPLETION")
    @ParameterizedTest
    void 계산_완료_상태가_아닌_주문이_있으면_failure(OrderStatus orderStatus) {
        // given
        LocalDateTime orderedTime = LocalDateTime.parse("2023-10-15T22:40:00");
        OrderTable orderTable = new OrderTable(1L, false, 0);
        given(orderRepository.findAllByOrderTableIdIn(anyList()))
            .willReturn(List.of(
                new Order(1L, OrderStatus.COMPLETION, orderedTime, orderTable),
                new Order(2L, orderStatus, orderedTime, orderTable)
            ));

        // when
        ValidResult result = validator.validate(List.of(1L, 2L));

        // then
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    void 계산_완료_상태인_주문이면_success() {
        // given
        LocalDateTime orderedTime = LocalDateTime.parse("2023-10-15T22:40:00");
        OrderTable orderTable = new OrderTable(1L, false, 0);
        given(orderRepository.findAllByOrderTableIdIn(anyList()))
            .willReturn(List.of(
                new Order(1L, OrderStatus.COMPLETION, orderedTime, orderTable),
                new Order(2L, OrderStatus.COMPLETION, orderedTime, orderTable)
            ));

        // when
        ValidResult result = validator.validate(List.of(1L, 2L));

        // then
        assertThat(result.isFailure()).isFalse();
    }
}
