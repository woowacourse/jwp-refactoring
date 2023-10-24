package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Nested
    class addOrderTables {

        @Test
        void 테이블그룹에_테이블을_추가할_수_있다() {
            // given
            final var table1 = new OrderTable(3, true);
            final var table2 = new OrderTable(5, true);

            final var tableGroup = new TableGroup();

            // when
            tableGroup.addOrderTables(List.of(table1, table2));

            // then
            assertThat(tableGroup.getOrderTables().size()).isEqualTo(2);
        }

        @Test
        void 대상이_되는_테이블이_2개이상이어야_한다() {
            // given
            final var tableGroup = new TableGroup();

            // when & then
            assertThatThrownBy(() -> tableGroup.addOrderTables(List.of()));
        }
    }
}
