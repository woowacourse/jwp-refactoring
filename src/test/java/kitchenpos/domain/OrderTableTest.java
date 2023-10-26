package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("테이블의 상태가 비어있고 할당된 그룹이 존재하지 않는지 검사한다.")
    @Test
    void checkEmptyAndTableGroups() {
        // given
        final OrderTable orderTable = new OrderTable(null, 3, true);

        // when & then
        assertDoesNotThrow(orderTable::checkEmptyAndTableGroups);
    }

    @DisplayName("테이블의 상태가 비어 있지 않으면 예외 처리한다.")
    @Test
    void checkEmptyAndTableGroups_FailWhenStatusIsFalse() {
        // given
        final OrderTable orderTable = new OrderTable(null, 3, false);

        // when & then
        assertThatThrownBy(orderTable::checkEmptyAndTableGroups)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블의 상태가 비어 있지 않습니다.");
    }

    @DisplayName("테이블에 이미 할당된 그룹이 존재하면 예외 처리한다.")
    @Test
    void checkEmptyAndTableGroups_FailWhenGroupAlreadyExist() {
        // given
        final OrderTable orderTable = new OrderTable(new TableGroup(), 3, true);

        // when & then
        assertThatThrownBy(orderTable::checkEmptyAndTableGroups)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("할당된 그룹이 존재합니다.");
    }

    @DisplayName("테이블의 상태가 비어있지 않은지 검사한다.")
    @Test
    void checkEmptyIsFalse() {
        // given
        final OrderTable orderTable = new OrderTable(null, 3, false);

        // when & then
        assertDoesNotThrow(orderTable::checkEmptyIsFalse);
    }

    @DisplayName("테이블의 상태가 비어 있으면 예외 처리한다.")
    @Test
    void checkEmptyIsFalse_FailWhenEmptyIsTrue() {
        // given
        final OrderTable orderTable = new OrderTable(null, 3, true);

        // when & then
        assertThatThrownBy(orderTable::checkEmptyIsFalse)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블의 상태가 비어 있습니다.");
    }

    @DisplayName("테이블에 그룹을 할당할 수 있다.")
    @Test
    void addGroup() {
        // given
        final OrderTable orderTable = new OrderTable(null, 3, true);
        final TableGroup tableGroup = new TableGroup();

        // when
        orderTable.addGroup(tableGroup);

        // then
        assertThat(orderTable.getTableGroup()).isEqualTo(tableGroup);
    }

    @DisplayName("테이블에 이미 그룹이 할당되어 있다면 예외 처리한다.")
    @Test
    void addGroup_FailWhenGroupAlreadyExist() {
        // given
        final OrderTable orderTable = new OrderTable(new TableGroup(), 3, true);
        final TableGroup tableGroup = new TableGroup();

        // when & then
        assertThatThrownBy(() -> orderTable.addGroup(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("할당된 그룹이 존재합니다.");
    }

    @DisplayName("테이블에 할당된 그룹을 삭제한다.")
    @Test
    void unGroup() {
        // given
        final OrderTable orderTable = new OrderTable(new TableGroup(), 3, true);

        // when

        // then
        assertAll(
                () -> assertThat(Objects.nonNull(orderTable.getTableGroup())).isTrue(),
                orderTable::unGroup,
                () -> assertThat(Objects.isNull(orderTable.getTableGroup())).isTrue());
    }
}
