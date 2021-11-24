package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import kitchenpos.order.domain.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("테이블 생성시 order 상태를 유효성 검사한다")
@ExtendWith(MockitoExtension.class)
class TableValidatorTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TableValidator tableValidator;

    @DisplayName("주문 테이블의 주문이 있고, 주문 상태가 `COOKING`, `MEAL`인 것이 있다면 예외가 발생한다")
    @Test
    void validate() {
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(1L,
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

        assertThatThrownBy(() -> tableValidator.validate(1L)).isInstanceOf(IllegalArgumentException.class);
    }
}
