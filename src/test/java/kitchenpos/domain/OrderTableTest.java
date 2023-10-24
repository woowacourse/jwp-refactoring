package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    void 빈_테이블로_설정_할때_단체_지정이_되어있는_경우_예외가_발생한다() {
        // given
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable = new OrderTable(tableGroup, 0, false);

        // when, then
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
