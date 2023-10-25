package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import kitchenpos.order.domain.exception.TableGroupException.CannotAssignOrderTableException;
import kitchenpos.order.domain.exception.TableGroupException.InsufficientOrderTableSizeException;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    @DisplayName("테이블 그룹을 생성할 수 있다.")
    void init_success() {
        TableGroup tableGroup = TableGroup.from(List.of(new OrderTable(10), new OrderTable(10)));

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(tableGroup.getOrderTables().size()).isEqualTo(2);
            softAssertions.assertThat(tableGroup.getOrderTables().get(0).getTableGroup()).isEqualTo(tableGroup);
            softAssertions.assertThat(tableGroup.getOrderTables().get(0).isEmpty()).isEqualTo(false);
        });
    }

    @Test
    @DisplayName("주문 테이블이 두 개 미만이면 테이블 그룹을 생성할 수 없다.")
    void init_fail1() {
        assertThatThrownBy(() -> TableGroup.from(List.of(new OrderTable(10))))
                .isInstanceOf(InsufficientOrderTableSizeException.class);
    }

    @Test
    @DisplayName("주문 테이블 중 빈 테이블이 아닌 테이블이 있으면 테이블 그룹을 생성할 수 없다.")
    void init_fail2() {
        OrderTable notEmptyOrderTable = new OrderTable(10);
        notEmptyOrderTable.setEmpty(false);

        assertThatThrownBy(() -> TableGroup.from(List.of(notEmptyOrderTable, new OrderTable(10))))
                .isInstanceOf(CannotAssignOrderTableException.class);
    }

    @Test
    @DisplayName("주문 테이블 중 테이블 그룹이 지정된 테이블이 있으면 테이블 그룹을 생성할 수 없다.")
    void init_fail3() {
        OrderTable assignedTableGroupOrderTable = new OrderTable(10);
        assignedTableGroupOrderTable.setTableGroup(TableGroup.from(List.of(new OrderTable(10), new OrderTable(10))));

        assertThatThrownBy(() -> TableGroup.from(List.of(assignedTableGroupOrderTable, new OrderTable(10))))
                .isInstanceOf(CannotAssignOrderTableException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 해제할 수 있다.")
    void ungroup_success() {
        TableGroup tableGroup = TableGroup.from(List.of(new OrderTable(10), new OrderTable(10)));

        tableGroup.ungroup();

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(tableGroup.getOrderTables().get(0).getTableGroup()).isEqualTo(null);
            softAssertions.assertThat(tableGroup.getOrderTables().get(0).isEmpty()).isEqualTo(false);
        });
    }
}
