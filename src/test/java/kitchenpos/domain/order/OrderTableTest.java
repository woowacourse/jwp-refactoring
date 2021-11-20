package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("빈 테이블 검증 성공")
    @Test
    void validateEmpty() {
        OrderTable orderTable = new OrderTable(null, 0, false);
        assertThatCode(orderTable::validateEmpty)
                .doesNotThrowAnyException();
    }

    @DisplayName("빈 테이블 검증 실패")
    @Test
    void validateChangeEmptyFail() {
        OrderTable orderTable = new OrderTable(null, 0, true);
        assertThatThrownBy(orderTable::validateEmpty)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블 검증 성공")
    @Test
    void validateGroup() {
        OrderTable orderTable = new OrderTable(null, 0, false);
        assertThatCode(orderTable::validateEmpty)
                .doesNotThrowAnyException();
    }

    @DisplayName("빈 테이블 검증 실패")
    @Test
    void validateGroupFail() {
        OrderTable orderTable = new OrderTable(1L, 0, true);
        assertThatThrownBy(orderTable::validateEmpty)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 인원 변경 성공")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = new OrderTable(null, 0, false);
        orderTable.changeNumberOfGuests(10);
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);
    }

    @DisplayName("테이블 인원 변경 실패")
    @Test
    void changeNumberOfGuestsFail() {
        OrderTable orderTable = new OrderTable(1L, 0, true);
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 빈 상태 변경 성공")
    @Test
    void changeEmpty() {
        OrderTable orderTable = new OrderTable(null, 0, false);
        assertThat(orderTable.isEmpty()).isFalse();
        orderTable.changeEmpty(true);
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블 그룹 변경 성공")
    @Test
    void changeTableGroup() {
        OrderTable orderTable = new OrderTable(null, 0, false);
        assertThat(orderTable.getTableGroupId()).isNull();
        orderTable.changeTableGroupId(1L);
        assertThat(orderTable.getTableGroupId()).isNotNull();
    }

    @DisplayName("테이블 그룹 아이디 조회")
    @Test
    void tableGroupId() {
        OrderTable orderTable = new OrderTable(null, 0, false);
        assertThat(orderTable.getTableGroupId()).isNull();
        orderTable.changeTableGroupId(1L);
        assertThat(orderTable.getTableGroupId()).isNotNull();
    }
}
