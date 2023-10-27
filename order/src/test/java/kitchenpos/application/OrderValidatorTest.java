package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderValidator orderValidator;

    @Test
    void 주문하려는_테이블이_존재하지_않으면_예외발생() {
        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        Long invalidOrderTableId = 9999999L;
        assertThatThrownBy(() -> orderValidator.validate(invalidOrderTableId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문하려는_테이블이_빈_테이블이면_예외발생() {
        OrderTable notEmptyTable = new OrderTable(1L, 3, true);
        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(notEmptyTable));

        assertThatThrownBy(() -> orderValidator.validate(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
