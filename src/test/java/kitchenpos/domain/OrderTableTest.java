package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @DisplayName("테이블을 비운다.")
    @Test
    void changeEmpty() {
        // given
        OrderTable orderTable = new OrderTable(5, false);

        // when
        orderTable.changeEmpty(true);

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("단체로 지정된 테이블을 비우면 예외가 발생한다.")
    @Test
    void changeEmpty_TableGroup_ExceptionThrown() {
        // given
        OrderTable orderTable = new OrderTable(null, 1L, 5, false);

        // when, then
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
