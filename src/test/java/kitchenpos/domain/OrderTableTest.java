package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void validateHasGroupId_fail_when_has_groupId() {
        final OrderTable orderTable = new OrderTable(1L, 1L, 5, false);

        assertThatThrownBy(orderTable::validateHasGroupId)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void validateHasGroupId_fail_when_numberOfGuests_is_smaller_than_zero() {
        assertThatThrownBy(() -> new OrderTable(1L, 1L, -1, false))
                .isInstanceOf(IllegalArgumentException.class);
    }
}