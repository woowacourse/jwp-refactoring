package kitchenpos.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class OrderTableTest {

    @DisplayName("주문 테이블 생성 시, 방문한 손님 수가 0명 미만이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -100})
    void orderTable_FailWithInvalidNumberOfGuests(int invalidNumberOfGuests) {
        // when & then
        assertThatThrownBy(() -> OrderTable.create(invalidNumberOfGuests, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("방문한 손님 수는 0명 이상이어야 합니다.");
    }

    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void orderTable() {
        // then
        assertDoesNotThrow(() -> OrderTable.create(1, true));
    }

    @DisplayName("주문 테이블의 방문한 손님 수 변경 시, 빈 테이블이면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_FailWithInvalidEmptyStatus() {
        // given
        OrderTable orderTable = OrderTable.create(1, true);

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블의 방문한 손님 수는 변경할 수 없습니다.");
    }

    @DisplayName("주문 테이블의 방문한 손님 수 변경 시, 방문한 손님 수가 0명 미만이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -100})
    void changeNumberOfGuests_FailWithInvalidNumberOfGuests(int invalidNumberOfGuests) {
        // given
        OrderTable orderTable = OrderTable.create(1, false);

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(invalidNumberOfGuests))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("방문한 손님 수는 0명 이상이어야 합니다.");
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 변경할 수 있다.")
    @ParameterizedTest
    @ValueSource(ints = {2, 100})
    void changeNumberOfGuests(int numberOfGuests) {
        // given
        OrderTable orderTable = OrderTable.create(1, false);

        // when
        orderTable.changeNumberOfGuests(numberOfGuests);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @DisplayName("주문 테이블을 그룹화할 수 있다.")
    @Test
    void group() {
        // given
        OrderTable orderTable = OrderTable.create(1, true);

        TableGroup tableGroupWithoutGrouping = TableGroup.createWithoutGrouping();

        // when & then
        assertDoesNotThrow(() -> orderTable.group(tableGroupWithoutGrouping));
        assertSoftly(softly -> {
            softly.assertThat(orderTable.isEmpty()).isFalse();
            softly.assertThat(orderTable.getTableGroup()).isEqualTo(tableGroupWithoutGrouping);
        });
    }

    @DisplayName("주문 테이블 그룹화 시, 빈 테이블이 아니면 예외가 발생한다.")
    @Test
    void group_FailWithNotEmptyTableGroup() {
        // given
        OrderTable orderTable1 = OrderTable.create(1, false);
        OrderTable orderTable2 = OrderTable.create(1, false);

        TableGroup tableGroupWithoutGrouping = TableGroup.createWithoutGrouping();

        // when & then
        assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> orderTable1.group(tableGroupWithoutGrouping))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블이 비어있지 않은 경우 그룹화할 수 없습니다.");
            softly.assertThatThrownBy(() -> orderTable2.group(tableGroupWithoutGrouping))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블이 비어있지 않은 경우 그룹화할 수 없습니다.");
        });
    }

    @DisplayName("주문 테이블 그룹화 시, 이미 다른 그룹에 속한 테이블이면 예외가 발생한다.")
    @Test
    void group_FailWithAlreadyGroupedOrderTable() {
        // given
        OrderTable orderTable1 = OrderTable.create(1, true);
        OrderTable orderTable2 = OrderTable.create(1, true);

        TableGroup tableGroupWithGrouping = TableGroup.createWithGrouping(List.of(orderTable1, orderTable2));

        // when
        orderTable1.setTableGroup(tableGroupWithGrouping);
        orderTable2.setTableGroup(tableGroupWithGrouping);

        // then
        assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> orderTable1.group(tableGroupWithGrouping))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 그룹 지정된 테이블은 그룹화할 수 없습니다.");
            softly.assertThatThrownBy(() -> orderTable2.group(tableGroupWithGrouping))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 그룹 지정된 테이블은 그룹화할 수 없습니다.");
        });
    }

    @DisplayName("테이블 그룹에서 주문 테이블을 그룹 해제할 수 있다.")
    @Test
    void ungroup() {
        // given
        OrderTable orderTable = OrderTable.create(1, true);

        TableGroup tableGroupWithoutGrouping = TableGroup.createWithoutGrouping();

        orderTable.group(tableGroupWithoutGrouping);

        // when & then
        assertDoesNotThrow(orderTable::ungroup);
        assertSoftly(softly -> {
            softly.assertThat(orderTable.getTableGroup()).isNull();
            softly.assertThat(orderTable.isEmpty()).isFalse();
        });

    }

    @DisplayName("주문 테이블 그룹 해제 시, 그룹 지정되지 않은 테이블이면 예외가 발생한다.")
    @Test
    void ungroup_FailWithNullTableGroup() {
        // given
        OrderTable orderTable = OrderTable.create(1, true);

        // when & then
        assertThatThrownBy(orderTable::ungroup)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹 지정되지 않은 테이블은 그룹을 해제할 수 없습니다.");
    }
}
