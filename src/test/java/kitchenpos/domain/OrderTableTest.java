package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @DisplayName("주문 테이블 생성")
    @Test
    public void createOrderTable() {
        OrderTable orderTable = new OrderTable(null, 4, true);

        assertThat(orderTable).isNotNull();
        assertThat(orderTable.getTableGroupId()).isNull();
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(4);
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블 empty 상태 수정 실패 - tableGroupId가 있을 경우")
    @Test
    public void changeFailTableGroupIdNull() {
        OrderTable orderTable = new OrderTable(3L, 4, true);

        assertThatThrownBy(() -> orderTable.changeEmptyState(false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 empty 상태 수정")
    @Test
    public void changeEmptyState() {
        OrderTable orderTable = new OrderTable(null, 4, false);

        orderTable.changeEmptyState(true);

        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블 NumberOfGuests 수정 실패 - numberOfGuest가 음수일 경우")
    @Test
    public void changeFailNumberMinus() {
        OrderTable orderTable = new OrderTable(3L, 4, false);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 NumberOfGuests 수정 실패 - empty가 true일 경우")
    @Test
    public void changeFailEmptyTrue() {
        OrderTable orderTable = new OrderTable(3L, 4, true);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(3))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 인원 수 수정")
    @Test
    public void changeNumberOfGuests() {
        OrderTable orderTable = new OrderTable(3L, 4, false);

        orderTable.changeNumberOfGuests(3);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(3);
    }
}