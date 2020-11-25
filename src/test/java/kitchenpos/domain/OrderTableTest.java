package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTableTest {
    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void constructor() {
        OrderTable orderTable = new OrderTable(1L, 1L, 0, true);

        assertAll(
            () -> assertThat(orderTable.getId()).isEqualTo(1L),
            () -> assertThat(orderTable.getTableGroupId()).isEqualTo(1L),
            () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(0),
            () -> assertThat(orderTable.isEmpty()).isTrue()
        );
    }

    @DisplayName("주문 테이블은 단체에 속할 수 있다.")
    @Test
    void groupBy() {
        OrderTable orderTable = new OrderTable(1L, null, 0, true);

        orderTable.groupBy(1L);

        assertAll(
            () -> assertThat(orderTable.getTableGroupId()).isEqualTo(1L),
            () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("빈 테이블이 아닌 경우 단체 지정할 수 없다.")
    @Test
    void groupBy_throws_exception() {
        OrderTable orderTable = new OrderTable(1L, null, 0, false);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderTable.groupBy(1L))
            .withMessage("빈 테이블이 아니면 단체 지정할 수 없습니다.");
    }

    @DisplayName("이미 단체에 속한 주문 테이블은 단체 지정할 수 없다.")
    @Test
    void groupBy_throws_exception2() {
        OrderTable orderTable = new OrderTable(1L, 1L, 0, true);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderTable.groupBy(2L))
            .withMessage("단체 지정된 테이블은 단체 지정을 변경할 수 없습니다.");
    }

    @DisplayName("주문 테이블은 단체를 해제할 수 있다.")
    @Test
    void ungroup() {
        OrderTable orderTable = new OrderTable(1L, 1L, 0, false);

        orderTable.ungroup();

        assertAll(
            () -> assertThat(orderTable.getTableGroupId()).isNull(),
            () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("주문 테이블은 단체에 속하지 않으면 빈 테이블 여부를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        OrderTable orderTable = new OrderTable(1L, null, 0, true);

        orderTable.changeEmpty(false);

        assertThat(orderTable.isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블은 단체에 속하면 빈 테이블 여부를 변경할 수 없다.")
    @Test
    void changeEmpty_throws_exception() {
        OrderTable orderTable = new OrderTable(1L, 1L, 0, true);
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderTable.changeEmpty(false))
            .withMessage("단체 지정된 테이블은 빈 테이블 여부를 변경할 수 없습니다.");
    }

    @DisplayName("주문 테이블은 빈 테이블이 아니면 0명 이상 손님 수로 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = new OrderTable(1L, null, 0, false);

        orderTable.changeNumberOfGuests(10);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);
    }

    @DisplayName("주문 테이블은 빈 테이블이면 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_throws_exception() {
        OrderTable orderTable = new OrderTable(1L, null, 0, true);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderTable.changeNumberOfGuests(10))
            .withMessage("빈 테이블이 아닌 경우 손님 수를 변경할 수 없습니다.");
    }

    @DisplayName("주문 테이블은 0명 미만으로 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_throws_exception2() {
        OrderTable orderTable = new OrderTable(1L, null, 0, true);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderTable.changeNumberOfGuests(-1))
            .withMessageContaining("변경할 손님 수는 0명 이상이어야 합니다.");
    }
}
