package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void orderTable이_tableGroupId를_갖는지_확인한다() {
        // given
        final OrderTable orderTable = new OrderTable(null, 1L, 0, false);

        // when
        final boolean actual = orderTable.hasTableGroupId();

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void orderTable이_tableGroupId를_안갖는지_확인한다() {
        // given
        final OrderTable orderTable = new OrderTable(null, null, 0, false);

        // when
        final boolean actual = orderTable.hasTableGroupId();

        // then
        assertThat(actual).isFalse();
    }
}
