package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTablesTest {

    @Test
    @DisplayName("주문 테이블은 2개 미만인 경우 예외가 발생한다.")
    void orderTablesOne() {
        assertThatThrownBy(() -> OrderTables.group(List.of(new OrderTable(2, true))))
                .hasMessage("주문 테이블은 2개 이상이어야 합니다.");

    }

    @Test
    @DisplayName("빈 주문 테이블이 아닐경우 예외가 발생한다.")
    void orderTableIsNotEmpty() {
        assertThatThrownBy(() -> OrderTables.group(List.of(
                new OrderTable(1L, 2L, 2, false),
                new OrderTable(2L, 2L, 2, false))))
                .hasMessage("빈 주문 테이블이어야 합니다.");
    }

    @Test
    @DisplayName("단체지정이 된 경우 예외가 발생한다.")
    void orderTableIsTableGroup() {
        assertThatThrownBy(() -> OrderTables.group(List.of(
                new OrderTable(1L, 2L, 2, false),
                new OrderTable(2L, 2L, 2, false))))
                .hasMessage("빈 주문 테이블이어야 합니다.");
    }
}
