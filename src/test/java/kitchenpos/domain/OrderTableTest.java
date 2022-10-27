package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("손님의 수는 음수가 될 수 없다")
    @Test
    void numberOfGuestUnderZero_throwsException() {
        final var invalidNumberOfGuests = -1;

        assertThatThrownBy(
                () -> new OrderTable(invalidNumberOfGuests)
        );
    }

    @DisplayName("그룹 id를 갖고 있다면, 비어있는지 여부를 true로 변경할 수 없다")
    @Test
    void hasTableGroupId_changeEmptyToTrue_throwsException() {
        final var table = new OrderTable(2);
        table.setTableGroupId(1L);

        assertThatThrownBy(
                () -> table.changeEmptyTo(true)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
