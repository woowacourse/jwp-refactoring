package kitchenpos.domain.table;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTablesTest {

    @Test
    @DisplayName("테이블의 아이디 목록을 반환한다")
    void getOrderTableIds() {
        // given
        final OrderTable orderTable1 = new OrderTable(4, false);
        ReflectionTestUtils.setField(orderTable1, "id", 1L);
        final OrderTable orderTable2 = new OrderTable(4, false);
        ReflectionTestUtils.setField(orderTable2, "id", 2L);
        final OrderTable orderTable3 = new OrderTable(4, false);
        ReflectionTestUtils.setField(orderTable3, "id", 3L);

        final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2, orderTable3));

        // when
        final List<Long> actual = orderTables.getOrderTableIds();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual.get(0)).isEqualTo(1L);
            softAssertions.assertThat(actual.get(1)).isEqualTo(2L);
            softAssertions.assertThat(actual.get(2)).isEqualTo(3L);
        });
    }

    @Test
    @DisplayName("테이블들이 모두 같은 테이블 그룹에 속해있으면 테이블 그룹 해제를 할 수 있다.")
    void ungroupOrderTables() {
        // given
        final TableGroup tableGroup = new TableGroup();
        ReflectionTestUtils.setField(tableGroup, "id", 1L);

        final OrderTable orderTable1 = new OrderTable(4, true);
        orderTable1.groupBy(tableGroup.getId());
        final OrderTable orderTable2 = new OrderTable(4, true);
        orderTable2.groupBy(tableGroup.getId());
        final OrderTable orderTable3 = new OrderTable(4, true);
        orderTable3.groupBy(tableGroup.getId());

        final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2, orderTable3));

        // when
        orderTables.ungroupOrderTables();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(orderTable1.getTableGroupId()).isNull();
            softAssertions.assertThat(orderTable2.getTableGroupId()).isNull();
            softAssertions.assertThat(orderTable3.getTableGroupId()).isNull();
        });
    }

    @Test
    @DisplayName("테이블들이 서로 다른 테이블 그룹에 속해있으면 테이블 그룹 해제를 할 수 없다.")
    void ungroupOrderTables_differentGroup() {
        // given
        final TableGroup tableGroup = new TableGroup();
        ReflectionTestUtils.setField(tableGroup, "id", 1L);
        final TableGroup differentTableGroup = new TableGroup();
        ReflectionTestUtils.setField(differentTableGroup, "id", 2L);

        final OrderTable orderTable1 = new OrderTable(4, true);
        orderTable1.groupBy(tableGroup.getId());
        final OrderTable orderTable2 = new OrderTable(4, true);
        orderTable2.groupBy(tableGroup.getId());
        final OrderTable orderTable3 = new OrderTable(4, true);
        orderTable3.groupBy(differentTableGroup.getId());

        final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2, orderTable3));

        // when & then
        assertThatThrownBy(() -> orderTables.ungroupOrderTables())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("다른 테이블 그룹에 속한 테이블이 포함되어 있습니다.");
    }
}
