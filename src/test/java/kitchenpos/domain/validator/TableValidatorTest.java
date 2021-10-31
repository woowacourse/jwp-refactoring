package kitchenpos.domain.validator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

@MockitoSettings
class TableValidatorTest {
    @InjectMocks
    private TableValidator tableValidator;
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
    void validateUpdateEmptyWhenGrouped() {
        OrderTable other = new OrderTable(2L, null, 3, true);
        TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable, other));

        orderTable.setTableGroup(tableGroup);
        assertThatThrownBy(() -> tableValidator.validateUpdateEmpty(orderTable))
                .isInstanceOf(IllegalArgumentException.class);
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
