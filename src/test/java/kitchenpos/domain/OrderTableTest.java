package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void validateHasGroupId_fail_when_has_groupId() {
        final OrderTable orderTable = new OrderTable(1L, 1L, 5, false);

        assertThatThrownBy(orderTable::validateHasGroupId)
                .isInstanceOf(IllegalArgumentException.class);
    }
}