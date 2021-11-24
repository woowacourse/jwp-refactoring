package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class OrderTableTest {

    @DisplayName("객체 생성: 객체가 정상적으로 생성된다.")
    @Test
    void create() {
        // given
        final Long id = 1L;
        final int numberOfGuests = 0;
        final boolean empty = true;

        // when
        final OrderTable orderTable = new OrderTable(id, null, numberOfGuests, empty);

        // then
        assertThat(orderTable.getId()).isEqualTo(id);
        assertThat(orderTable.getTableGroupId()).isNull();
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
        assertThat(orderTable.isEmpty()).isEqualTo(empty);
    }

    @DisplayName("테이블 최초 생성시 상태 초기화: 테이블 최초 생성 시 id와 tableGroup 둘 모두 null 이어야한다.")
    @Test
    void initBeforeCreate() {
        // given
        final Long id = 1L;
        final Long tableGroupId = 2L;
        final int numberOfGuests = 0;
        final boolean empty = true;
        final OrderTable orderTable = new OrderTable(id, tableGroupId, numberOfGuests, empty);

        // when
        orderTable.initBeforeCreate();

        // then
        assertThat(orderTable.getId()).isNull();
        assertThat(orderTable.getTableGroupId()).isNull();
    }

    @DisplayName("테이블 활성화 상태 변경: 테이블이 그룹화된 상태가 아니라면 활성화 상태를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        // given
        final Long id = 1L;
        final int numberOfGuests = 0;
        final boolean empty = true;
        final OrderTable orderTable = new OrderTable(id, null, numberOfGuests, empty);
        final TableValidator tableValidator = new TableValidatorTestImpl();

        // when
        orderTable.changeEmpty(tableValidator, false);
        final boolean expectedEmpty = false;

        // then
        assertThat(orderTable.isEmpty()).isEqualTo(expectedEmpty);
    }

    @DisplayName("손님 수 변경: 변경 숫자가 0이상이고 테이블이 활성화 상태면 변경이 가능하다.")
    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTable orderTable = new OrderTable(1L, null, 0, false);

        // when
        orderTable.changeNumberOfGuests(1);
        int expectedNumberOfGuests = 1;

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(expectedNumberOfGuests);
    }

    @DisplayName("손님 수 변경: 변경 숫자가 음수라면 변경이 불가능하다.")
    @Test
    void changeNumberOfGuestsWithNegativeNumber() {
        // given
        final OrderTable orderTable = new OrderTable(1L, null, 0, false);

        // when then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수 변경: 테이블이 비활성화 상태면 변경이 불가능하다")
    @Test
    void changeNumberOfGuestsWithEmptyTable() {
        // given
        final OrderTable orderTable = new OrderTable(1L, null, 0, true);

        // when then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹화 여부를 알 수 있다.")
    @Test
    void isGrouping() {
        // given
        final Long id = 1L;
        final Long tableGroupId = 2L;
        final int numberOfGuests = 0;
        final boolean empty = true;

        final OrderTable orderTable1 = new OrderTable(id, null, numberOfGuests, empty);
        final OrderTable orderTable2 = new OrderTable(id, tableGroupId, numberOfGuests, empty);

        // when
        boolean result1 = orderTable1.isGrouping();
        boolean result2 = orderTable2.isGrouping();

        // then
        assertThat(result1).isFalse();
        assertThat(result2).isTrue();
    }

    @DisplayName("테이블을 그룹에 포함시킨다.")
    @Test
    void grouping() {
        // given
        final OrderTable orderTable = new OrderTable(1L, null, 0, true);

        final Long tableGroupId = 2L;

        // when
        assertThat(orderTable.getTableGroupId()).isNull();
        orderTable.grouping(tableGroupId);

        // then
        assertThat(orderTable.getTableGroupId()).isEqualTo(2L);
    }

    @DisplayName("테이블의 그룹화를 해제한다.")
    @Test
    void ungroup() {
        // given
        final Long tableGroupId = 2L;
        final OrderTable orderTable = new OrderTable(1L, tableGroupId, 0, true);

        // when
        orderTable.ungroup();

        // then
        assertThat(orderTable.getTableGroupId()).isNull();
    }
}