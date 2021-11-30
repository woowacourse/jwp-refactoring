package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.service.TableValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable(3, false);
    }

    @Test
    void updateEmpty() {
        TableValidator validator = mock(TableValidator.class);
        doNothing().when(validator).validateUpdateEmpty(any());

        orderTable.updateEmpty(validator, true);
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    void updateEmptyFalse() {
        TableValidator validator = mock(TableValidator.class);
        doNothing().when(validator).validateUpdateEmpty(any());

        orderTable.updateEmpty(validator, false);
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    void updateNumberOfGuests() {
        orderTable.updateNumberOfGuests(4);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(4);
    }

    @Test
    void updateNumberOfGuestsToNegative() {
        assertThatThrownBy(() -> orderTable.updateNumberOfGuests(-4))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateNumberOfGuestsOfEmptyTable() {
        orderTable = new OrderTable(3, true);

        assertThatThrownBy(() -> orderTable.updateNumberOfGuests(4))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
