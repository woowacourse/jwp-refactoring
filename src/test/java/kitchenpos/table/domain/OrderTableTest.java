package kitchenpos.table.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
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

    @DisplayName("주문 테이블 생성")
    @Nested
    class CreateNestedTest {

        @DisplayName("[SUCCESS] 단체 지정이 되지 않은 주문 테이블을 생성한다.")
        @Test
        void success_withoutTableGroup() {
            // given
            final OrderTable actual = OrderTable.withoutTableGroup(5, true);

            // expect
            assertSoftly(softly -> {
                softly.assertThat(actual.getTableGroup()).isNull();
                softly.assertThat(actual.getNumberOfGuests()).isEqualTo(5);
                softly.assertThat(actual.isEmpty()).isTrue();
            });
        }
    }

    @DisplayName("빈 상태 변경")
    @Nested
    class ChangeOrderTableEmptyNestedTest {

        @DisplayName("[SUCCESS] 주문 테이블이 빈 상태를 변경한다.")
        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        void success_changeOrderTableFull(final boolean expected) {
            // given
            final OrderTable orderTable = OrderTable.withoutTableGroup(5, false);

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
                    OrderTable.withoutTableGroup(10, true),
                    OrderTable.withoutTableGroup(10, true)
            ));

            // when
            final OrderTable orderTable = OrderTable.withoutTableGroup(10, true);
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
            final OrderTable orderTable = OrderTable.withoutTableGroup(10, false);

            // when
            final boolean actual = orderTable.isGrouped();

            // then
            assertThat(actual).isFalse();
        }

        @DisplayName("[SUCCESS] 단체 지정이 되었음을 확인한다.")
        @Test
        void success_isGrouped_isTrue() {
            // given
            final OrderTable orderTableOne = OrderTable.withoutTableGroup(10, true);
            final OrderTable orderTableTwo = OrderTable.withoutTableGroup(10, true);
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
                    OrderTable.withoutTableGroup(5, true),
                    OrderTable.withoutTableGroup(5, true)
            ));

            final OrderTable orderTableOne = OrderTable.withoutTableGroup(5, true);

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
            final OrderTable orderTableOne = OrderTable.withoutTableGroup(5, true);
            final OrderTable orderTableTwo = OrderTable.withoutTableGroup(5, true);
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
            final OrderTable orderTableOne = OrderTable.withoutTableGroup(5, true);
            final OrderTable orderTableTwo = OrderTable.withoutTableGroup(5, true);
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

    @DisplayName("손님 수 변경")
    @Nested
    class ChangeNumberOfGuests {

        @DisplayName("[SUCCESS] 손님 수를 변경한다.")
        @Test
        void success_changeNumberOfGuests() {
            // given
            final OrderTable orderTable = OrderTable.withoutTableGroup(5, false);

            // when
            orderTable.changeNumberOfGuests(10);

            // then
            assertThat(orderTable)
                    .usingRecursiveComparison()
                    .isEqualTo(
                            OrderTable.withoutTableGroup(10, false)
                    );
        }

        @DisplayName("[EXCEPTION] 주문 테이블이 비어있는 경우 예외가 발생한다.")
        @Test
        void throwException_changeNumberOfGuests_when_isEmpty() {
            // given
            final boolean isEmpty = true;

            // when
            final OrderTable orderTable = OrderTable.withoutTableGroup(5, isEmpty);

            // then
            assertThatThrownBy(() -> orderTable.changeNumberOfGuests(10))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("[EXCEPTION] 변경할 손님 수가 음수일 경우 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(ints = {-1, -2, -10, -1000000})
        void throwException_changeNumberOfGuests_when_negative(final int negativeNumberOfGuests) {
            // given
            final OrderTable orderTable = OrderTable.withoutTableGroup(5, false);

            // expect
            assertThatThrownBy(() -> orderTable.changeNumberOfGuests(negativeNumberOfGuests))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
