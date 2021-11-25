package kitchenpos.domain.order;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTableTest {
    @DisplayName("테이블을 empty로 변경한다. - 실패, 이미 변경하려는 상태인 경우")
    @Test
    void changeEmpty() {
        // given
        OrderTable orderTable = new OrderTable(10, false);
        assertThatThrownBy(() -> orderTable.changeEmpty(false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("변경하려는 테이블 비어있음의 유무가 이미 일치합니다.");
    }

    @DisplayName("테이블에 손님을 추가한다. - 실패, 테이블이 비어있음")
    @Test
    void addNumberOfGuests() {
        // given
        OrderTable orderTable = new OrderTable(0, true);
        assertThatThrownBy(() -> orderTable.addNumberOfGuests(100))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비어있는 테이블의 손님 수를 변경할 수 없습니다.");
    }

    @DisplayName("테이블을 그룹화한다. - 실패, 이미 그룹화되어있음.")
    @Test
    void addTableGroup() {
        // given
        OrderTable orderTable = new OrderTable(1L, new TableGroup(1L, LocalDateTime.now()), 0, true);
        assertThatThrownBy(() -> orderTable.addTableGroup(new TableGroup(2L, LocalDateTime.now())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("tableGroup이 이미 등록되어있습니다.");
    }
}
