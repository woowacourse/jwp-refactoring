package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class OrderTableTest {

    @DisplayName("[SUCCESS] 주문 테이블이 빈 상태를 변경한다.")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void success_changeOrderTableFull(final boolean expected) {
        // given
        final OrderTable orderTable = new OrderTable(null, 5, true);

        // when
        orderTable.changeOrderTableEmpty(expected);

        // then
        assertThat(orderTable.isEmpty()).isEqualTo(expected);
    }

    @DisplayName("[EXCEPTION] 단체 지정이 되어있을 경우 주문 테이블을 비어있는 상태로 변경할 수 없다.")
    @Test
    void throwException_changeOrderTableEmpty_when_tableGroupIsNotNull() {
        // given
        final OrderTable orderTable = new OrderTable(
                TableGroup.emptyOrderTables(),
                5,
                false
        );

        // expect
        assertThatThrownBy(() -> orderTable.changeOrderTableEmpty(true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[SUCCESS] 단체 지정이 되어있지 않음을 확인한다.")
    @Test
    void success_isGrouped_isFalse() {
        // given
        final OrderTable orderTable = new OrderTable(null, 10, true);

        // when
        final boolean actual = orderTable.isGrouped();

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("[SUCCESS] 단체 지정이 되었음을 확인한다.")
    @Test
    void success_isGrouped_isTrue() {
        // given
        final OrderTable orderTable = new OrderTable(null, 10, true);
        final TableGroup tableGroup = TableGroup.emptyOrderTables();
        tableGroup.addOrderTablesAndChangeEmptyFull(new OrderTables(List.of(orderTable)));

        // when
        final boolean actual = orderTable.isGrouped();

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("[SUCCESS] 단체 지정을 제거하고 비어있는 상태를 false 로 변경한다.")
    @Test
    void success_ungroupTableGroup() {
        // given
        final OrderTable orderTable = new OrderTable(null, 5, true);
        final TableGroup tableGroup = TableGroup.emptyOrderTables();
        tableGroup.addOrderTablesAndChangeEmptyFull(new OrderTables(List.of(orderTable)));

        // when
        orderTable.deassignTableGroup();

        // then
        assertSoftly(softly -> {
            softly.assertThat(orderTable.isGrouped()).isFalse();
            softly.assertThat(orderTable.getTableGroup()).isNull();
        });
    }
}
