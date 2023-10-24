package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void 단체지정을_해제한다() {
        // given
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable = new OrderTable(tableGroup, 0, true);

        // when
        orderTable.ungroup();

        // then
        assertThat(orderTable.getTableGroup()).isNull();
    }

}
