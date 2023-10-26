package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Optional;
import kitchenpos.common.domain.ValidResult;
import kitchenpos.common.exception.KitchenPosException;
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
class OrderTableChangeEmptyOrderValidatorTest {

    @InjectMocks
    OrderTableChangeEmptyOrderValidator validator;

    @Mock
    OrderRepository orderRepository;

    @Test
    void 주문_테이블_식별자에_대한_주문이_없으면_예외() {
        // given
        given(orderRepository.findByOrderTableId(anyLong()))
            .willReturn(Optional.empty());

        assertThatThrownBy(() -> validator.validate(4885L))
            .isInstanceOf(KitchenPosException.class)
            .hasMessage("해당 주문이 없습니다. orderTableId=4885");
    }

    @EnumSource(value = OrderStatus.class, mode = Mode.EXCLUDE, names = "COMPLETION")
    @ParameterizedTest
    void 계산_완료_상태가_아닌_주문이_있으면_failure(OrderStatus orderStatus) {
        // given
        LocalDateTime orderedTime = LocalDateTime.parse("2023-10-15T22:40:00");
        OrderTable orderTable = new OrderTable(1L, false, 0);
        Order order = new Order(1L, orderStatus, orderedTime, orderTable);
        given(orderRepository.findByOrderTableId(anyLong()))
            .willReturn(Optional.of(order));

        // when
        ValidResult result = validator.validate(1L);

        // then
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    void 계산_완료_상태인_주문이면_success() {
        // given
        LocalDateTime orderedTime = LocalDateTime.parse("2023-10-15T22:40:00");
        OrderTable orderTable = new OrderTable(1L, false, 0);
        Order order = new Order(1L, OrderStatus.COMPLETION, orderedTime, orderTable);
        given(orderRepository.findByOrderTableId(anyLong()))
            .willReturn(Optional.of(order));

        // when
        ValidResult result = validator.validate(1L);

        // then
        assertThat(result.isFailure()).isFalse();
    }
}
