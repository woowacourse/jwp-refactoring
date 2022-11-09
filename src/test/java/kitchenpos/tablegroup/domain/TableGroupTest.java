package kitchenpos.tablegroup.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @DisplayName("테이블 그룹 생성 시 테이블 그룹에 등록된 테이블이 2개 미만이면 예외가 발생한다.")
    @Test
    void constructWithLessThanTwoOrderTable() {
        assertThatThrownBy(
                () -> new TableGroup(1L, LocalDateTime.now(), new OrderTables(Arrays.asList(new OrderTable(4, false)))))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
