package kitchenpos.domain;

import static kitchenpos.fixtures.TestFixtures.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    void groupTables() {
        // given
        final OrderTable orderTable1 = 주문_테이블_생성(null, 5, true);
        final OrderTable orderTable2 = 주문_테이블_생성(null, 5, true);

        // when
        final TableGroup tableGroup = TableGroup.groupTables(List.of(orderTable1, orderTable2), LocalDateTime.now());

        // then
        assertAll(
                () -> assertThat(tableGroup.getOrderTables()).hasSize(2),
                () -> assertThat(tableGroup.getOrderTables()).extracting("empty")
                        .containsExactly(false, false)
        );

    }
}
