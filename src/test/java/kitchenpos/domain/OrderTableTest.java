package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OrderTableTest {

    @Test
    void invalidGuestNumber() {
        assertThatThrownBy(() -> new OrderTable(-1)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님의 수는 음수일 수 없습니다.");
    }

    @Test
    void changeOccupied() {
        OrderTable orderTable = new OrderTable(4);

        OrderTable occupiedTable = orderTable.changeOccupied();

        assertThat(occupiedTable.canOccupied()).isFalse();
    }

    @Test
    void changeGroupId() {
        OrderTable orderTable = new OrderTable(4);

        OrderTable groupedTable = orderTable.changeGroupTo(1L);

        assertAll(
                () -> assertThat(groupedTable.getTableGroupId()).isEqualTo(1L),
                () -> assertThat(groupedTable.canOccupied()).isFalse()
        );
    }
}
