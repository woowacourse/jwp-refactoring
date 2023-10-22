package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class TableGroupTest {

    @DisplayName("단체 지정 생성")
    @Nested
    class CreateNestedClass {

        @DisplayName("[SUCCESS] 생성한다.")
        @Test
        void success_create() {
            assertThatCode(() -> TableGroup.withOrderTables(List.of(
                    new OrderTable(null, 10, true),
                    new OrderTable(null, 10, true)
            ))).doesNotThrowAnyException();
        }

        @DisplayName("[EXCEPTION] 주문 테이블 개수가 2 미만일 경우 예외가 발생한다.")
        @Test
        void throwException_create_when_orderTables_sizeIsLessThan2() {
            assertThatThrownBy(() -> TableGroup.withOrderTables(List.of(
                    new OrderTable(null, 10, true)
            ))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("[EXCEPTION] 주문 테이블이 이미 단체 지정되어 있는 경우 예외가 발생한다.")
        @Test
        void throwException_create_when_orderTable_isAlreadyAssigned() {
            // given
            final TableGroup tableGroup = TableGroup.withOrderTables(List.of(
                    new OrderTable(null, 10, true),
                    new OrderTable(null, 10, true)
            ));

            // when
            final OrderTable alreadyAssignedOrderTable = new OrderTable(null, 10, true);
            alreadyAssignedOrderTable.assignTableGroup(tableGroup);
            final OrderTable orderTable = new OrderTable(null, 10, true);

            // then
            assertThatThrownBy(() -> TableGroup.withOrderTables(List.of(
                    alreadyAssignedOrderTable,
                    orderTable
            ))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("[EXCEPTION] 주문 테이블이 비어있지 않은 경우 예외가 발생한다.")
        @Test
        void throwException_create_when_orderTable_isNotEmpty() {
            assertThatThrownBy(() -> TableGroup.withOrderTables(List.of(
                    new OrderTable(null, 10, false),
                    new OrderTable(null, 10, false)
            ))).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("[SUCCESS] 주문 테이블을 단체 지정에 주입할 때 주문 테이블의 비어있는 상태를 false 로 수정한다.")
    @Test
    void success_addOrderTablesAndChangeEmptyFull() {
        // given
        final OrderTable orderTableOne = new OrderTable(null, 10, true);
        final OrderTable orderTableTwo = new OrderTable(null, 10, true);
        final TableGroup tableGroup = TableGroup.withOrderTables(List.of(
                orderTableOne,
                orderTableTwo
        ));

        // then
        assertSoftly(softly -> {
            final List<OrderTable> actualOrderTables = tableGroup.getOrderTables().getOrderTableItems();
            softly.assertThat(actualOrderTables).hasSize(2);

            final OrderTable actualOrderTableOne = actualOrderTables.get(0);
            final OrderTable actualOrderTableTwo = actualOrderTables.get(1);

            softly.assertThat(actualOrderTableOne.getTableGroup()).isEqualTo(tableGroup);
            softly.assertThat(actualOrderTableOne.isEmpty()).isFalse();
            softly.assertThat(actualOrderTableTwo.getTableGroup()).isEqualTo(tableGroup);
            softly.assertThat(actualOrderTableTwo.isEmpty()).isFalse();
        });
    }
}
