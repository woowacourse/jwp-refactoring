package kitchenpos.domain.validator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import kitchenpos.order.domain.Order.OrderStatus;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.service.TableValidatorImpl;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

@MockitoSettings
class TableValidatorImpTest {
    @InjectMocks
    private TableValidatorImpl tableValidator;

    @Mock
    private OrderRepository orderRepository;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        this.orderTable = new OrderTable(1L, null, 5, true);
    }

    @Test
    void validateUpdateEmpty() {
        assertDoesNotThrow(() -> tableValidator.validateUpdateEmpty(orderTable));
    }

    @Test
    void validateUpdateEmptyWhenContainsMealOrCompletedOrder() {
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(
                any(), eq(Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)))
        ).thenReturn(true);

        assertThatThrownBy(() -> tableValidator.validateUpdateEmpty(orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
