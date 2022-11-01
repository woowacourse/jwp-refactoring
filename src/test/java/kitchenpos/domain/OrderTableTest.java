package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    @DisplayName("주문 테이블의 주문 상태가 완료됐는지 확인한다.")
    void isNotCompleted() {
        // given
        final OrderTable orderTable = new OrderTable(10, false);

        // when
        final boolean actual = orderTable.isNotCompleted();

        // then
        assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("주문 테이블의 테이블 그룹을 해제한다.")
    void releaseGroup() {
        // given
        final OrderTable orderTable1 = new OrderTable(1, true);
        final OrderTable orderTable2 = new OrderTable(1, true);
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2));

        // when
        orderTable1.releaseGroup();

        // then
        assertAll(

                () -> assertThat(tableGroup.getOrderTables()).hasSize(2),
                () -> assertThat(orderTable1.getTableGroupId()).isNull()
        );
    }
}
