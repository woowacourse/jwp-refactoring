package kitchenpos.domain.table;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableGroupTest {

    @Test
    @DisplayName("테이블 그룹에 테이블을 추가할 때 추가할 테이블이 없다면 예외가 발생한다.")
    void initOrderTables_zeroTable() {
        // given
        final TableGroup tableGroup = new TableGroup();
        final List<OrderTable> zeroOrderTable = Collections.emptyList();

        // when & then
        assertThatThrownBy(() -> tableGroup.initOrderTables(zeroOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹화하려는 테이블은 2개 이상이어야 합니다.");
    }

    @Test
    @DisplayName("테이블 그룹에 테이블을 추가할 때 추가할 테이블이 1개이면 예외가 발생한다.")
    void initOrderTables_oneTable() {
        // given
        final TableGroup tableGroup = new TableGroup();
        final List<OrderTable> oneOrderTable = List.of(new OrderTable(3, true));

        // when & then
        assertThatThrownBy(() -> tableGroup.initOrderTables(oneOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹화하려는 테이블은 2개 이상이어야 합니다.");
    }
}
