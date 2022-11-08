package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("단체 지정에 포함 여부를 반환한다. - false")
    @Test
    void isBelongToGroup_false() {
        final OrderTable table = new OrderTable(5, false);

        final boolean result = table.isBelongToGroup();

        assertThat(result).isFalse();
    }

    @DisplayName("단체 지정에 포함 여부를 반환한다. - true")
    @Test
    void isBelongToGroup_true() {
        final OrderTable table = new OrderTable(null, 1L, 5, false);

        final boolean result = table.isBelongToGroup();

        assertThat(result).isTrue();
    }
}
