package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class OrderTableTest {

    @DisplayName("생성한다.")
    @Nested
    class CreateNestedClass {

        @DisplayName("[SUCCESS] 주문 테이블이 빈 상태를 변경한다.")
        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        void success_changeOrderTableFull(final boolean expected) {
            // given
            final OrderTable orderTable = new OrderTable(null, 5, false);

            // when
            orderTable.changeOrderTableEmpty(expected);

            // then
            assertThat(orderTable.isEmpty()).isEqualTo(expected);
        }

        @DisplayName("[EXCEPTION] 단체 지정이 되어있을 경우 주문 테이블을 비어있는 상태로 변경할 수 없다.")
        @Test
        void throwException_changeOrderTableEmpty_when_tableGroupIsNotNull() {
            // given
            final TableGroup tableGroup = TableGroup.withOrderTables(List.of(
                    new OrderTable(null, 10, true),
                    new OrderTable(null, 10, true)
            ));

            // when
            final OrderTable orderTable = new OrderTable(null, 10, true);
            orderTable.assignTableGroup(tableGroup);

            // then
            assertThatThrownBy(() -> orderTable.changeOrderTableEmpty(true))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("단체 지정 여부 확인")
    @Nested
    class IsGroupedNestedClass {
        @DisplayName("[SUCCESS] 단체 지정이 되어있지 않음을 확인한다.")
        @Test
        void success_isGrouped_isFalse() {
            // given
            final OrderTable orderTable = new OrderTable(null, 10, false);
            ;

            // when
            final boolean actual = orderTable.isGrouped();

            // then
            assertThat(actual).isFalse();
        }

        @DisplayName("[SUCCESS] 단체 지정이 되었음을 확인한다.")
        @Test
        void success_isGrouped_isTrue() {
            // given
            final OrderTable orderTableOne = new OrderTable(null, 10, true);
            final OrderTable orderTableTwo = new OrderTable(null, 10, true);
            ;
            TableGroup.withOrderTables(List.of(
                    orderTableOne,
                    orderTableTwo
            ));

            // when
            final boolean actual = orderTableOne.isGrouped();

            // then
            assertThat(actual).isTrue();
        }
    }

    @DisplayName("단체 지정 등록")
    @Nested
    class AssignTableGroupNestedTest {

        @DisplayName("[SUCCESS] 단체 지정을 한다.")
        @Test
        void success_assignTableGroup() {
            // given
            final TableGroup tableGroup = TableGroup.withOrderTables(List.of(
                    new OrderTable(null, 5, true),
                    new OrderTable(null, 5, true)
            ));

            final OrderTable orderTableOne = new OrderTable(null, 5, true);

            // when
            orderTableOne.assignTableGroup(tableGroup);

            // then
            assertSoftly(softly -> {
                softly.assertThat(orderTableOne.isGrouped()).isTrue();
                softly.assertThat(orderTableOne.getTableGroup()).isEqualTo(tableGroup);
            });
        }

        @DisplayName("[SUCCESS] 이미 단체 지정되어 있는 주문 테이블을 단체 지정할 경우 예외가 발생한다.")
        @Test
        void throwException_assignTableGroup_when_tableGroup_isAlreadyAssigned() {
            // given
            final OrderTable orderTableOne = new OrderTable(null, 5, true);
            final OrderTable orderTableTwo = new OrderTable(null, 5, true);
            final TableGroup tableGroup = TableGroup.withOrderTables(List.of(
                    orderTableOne,
                    orderTableTwo
            ));

            // expect
            assertSoftly(softly -> {
                softly.assertThatThrownBy(() -> orderTableOne.assignTableGroup(tableGroup))
                        .isInstanceOf(IllegalArgumentException.class);

                softly.assertThatThrownBy(() -> orderTableTwo.assignTableGroup(tableGroup))
                        .isInstanceOf(IllegalArgumentException.class);
            });
        }
    }

    @DisplayName("단체 지정 해제")
    @Nested
    class DeassignTableGroupNestedClass {

        @DisplayName("[SUCCESS] 단체 지정을 제거하고 비어있는 상태를 false 로 변경한다.")
        @Test
        void success_ungroupTableGroup() {
            // given
            final OrderTable orderTableOne = new OrderTable(null, 5, true);
            final OrderTable orderTableTwo = new OrderTable(null, 5, true);
            TableGroup.withOrderTables(List.of(
                    orderTableOne,
                    orderTableTwo
            ));

            // when
            orderTableOne.deassignTableGroup();
            orderTableTwo.deassignTableGroup();

            // then
            assertSoftly(softly -> {
                softly.assertThat(orderTableOne.isGrouped()).isFalse();
                softly.assertThat(orderTableOne.getTableGroup()).isNull();
                softly.assertThat(orderTableTwo.isGrouped()).isFalse();
                softly.assertThat(orderTableTwo.getTableGroup()).isNull();
            });
        }
    }
}
