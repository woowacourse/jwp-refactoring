package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @DisplayName("주문 테이블이 존재하지 않으면 예외를 반환한다.")
    @Test
    public void create_exception_emptyOrderTable() {
        assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), List.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 2개 보다 적으면 예외를 반환한다.")
    @Test
    public void create_exception_orderTableLessThanTwo() {
        assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), List.of(new OrderTable(0, true))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 빈 상태가 아니면 예외를 반환한다.")
    @Test
    public void create_exception_orderTableIsNotEmpty() {
        assertThatThrownBy(() -> new TableGroup(
                LocalDateTime.now(),
                List.of(
                        new OrderTable(0, false),
                        new OrderTable(0, true))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블에 groupId가 존재하면 예외를 반환한.")
    @Test
    public void create_exception_orderTableHasGroupId() {
        assertThatThrownBy(() -> new TableGroup(
                LocalDateTime.now(),
                List.of(
                        new OrderTable(0, true),
                        new OrderTable(1L, 1L, 0, true))))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
