import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Optional;
import kitchenpos.menu.domain.Order;
import kitchenpos.menu.domain.OrderPlacedEvent;
import kitchenpos.menu.domain.OrderTable;
import kitchenpos.menu.domain.OrderTableOrderedEventHandler;
import kitchenpos.menu.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderTableOrderedEventHandlerTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderTableOrderedEventHandler orderTableOrderedEventHandler;

    @DisplayName("빈 상태 테이블 검증")
    @Test
    void validate() {
        Mockito.when(orderTableRepository.findById(ArgumentMatchers.any())).thenReturn(
            Optional.of(new OrderTable(
                1L, null, 4, true
            ))
        );

        assertThatIllegalArgumentException().isThrownBy(
            () -> orderTableOrderedEventHandler.validateOrderTable(
                new OrderPlacedEvent(new Order(1L)))
        );
    }

    @DisplayName("비어있지 않은 테이블 검증")
    @Test
    void validateWithNotEmptyTable() {
        Mockito.when(orderTableRepository.findById(ArgumentMatchers.any())).thenReturn(
            Optional.of(new OrderTable(
                1L, null, 4, false
            ))
        );

        assertThatCode(
            () -> orderTableOrderedEventHandler.validateOrderTable(
                new OrderPlacedEvent(new Order(1L)))
        ).doesNotThrowAnyException();
    }
}
