package kitchenpos.order.domain;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderValidator;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.exception.InvalidOrderException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@DisplayName("")
@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderValidator orderValidator;

    @DisplayName("[실패] 빈 OrderLineItems 일 때 예외 발생")
    @Test
    void validate_EmptyOrderLineItems_ExceptionThrown() {
        // given
        List<OrderLineItem> orderLineItems = Collections.emptyList();
        Order order = new Order(1L, orderLineItems);

        // when
        // then
        Assertions.assertThatThrownBy(() -> orderValidator.validate(order))
            .isInstanceOf(InvalidOrderException.class);
    }

    @DisplayName("[실패] OrderTable 이 empty 상태라면 생성 불가")
    @Test
    void validate_EmptyOrderTable_ExceptionThrown() {
        // given
        BDDMockito.given(orderTableRepository.findById(ArgumentMatchers.any()))
            .willReturn(Optional.of(new OrderTable(0, true)));

        Order order = new Order(
            1L,
            Collections.singletonList(new OrderLineItem(1L, 1L))
        );

        // when
        // then
        Assertions.assertThatThrownBy(() -> orderValidator.validate(order))
            .isInstanceOf(InvalidOrderException.class);
    }
}
