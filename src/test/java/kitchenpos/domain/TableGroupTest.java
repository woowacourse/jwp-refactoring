package kitchenpos.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableGroupTest {

    @Test
    @DisplayName("생성할 때 주문 테이블이 2개 미만이면 예외가 발생한다")
    void create() {
        assertThatThrownBy(() -> new TableGroup(List.of(new OrderTable(4, true))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹 지정은 주문 테이블이 최소 2개여야 합니다.");
    }
}
