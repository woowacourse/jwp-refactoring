package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import kitchenpos.domain.exception.TableGroupException.CannotAssignOrderTableException;
import kitchenpos.domain.exception.TableGroupException.InsufficientOrderTableSizeException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    @DisplayName("테이블 그룹을 생성할 수 있다.")
    void init_success() {
        TableGroup tableGroup = TableGroup.from(List.of(new OrderTable(), new OrderTable()));

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(tableGroup.getOrderTables().size()).isEqualTo(2);
            softAssertions.assertThat(tableGroup.getOrderTables().get(0).getTableGroup()).isEqualTo(tableGroup);
            softAssertions.assertThat(tableGroup.getOrderTables().get(0).isEmpty()).isEqualTo(false);
        });
    }

    @Test
    @DisplayName("주문 테이블이 두 개 미만이면 테이블 그룹을 생성할 수 없다.")
    void init_fail1() {
        assertThatThrownBy(() -> TableGroup.from(List.of(new OrderTable())))
                .isInstanceOf(InsufficientOrderTableSizeException.class);
    }

    @Test
    @DisplayName("주문 테이블 중 빈 테이블이 아닌 테이블이 있으면 테이블 그룹을 생성할 수 없다.")
    void init_fail2() {
        OrderTable notEmptyOrderTable = new OrderTable();
        notEmptyOrderTable.setEmpty(false);

        assertThatThrownBy(() -> TableGroup.from(List.of(notEmptyOrderTable, new OrderTable())))
                .isInstanceOf(CannotAssignOrderTableException.class);
    }

    @Test
    @DisplayName("주문 테이블 중 테이블 그룹이 지정된 테이블이 있으면 테이블 그룹을 생성할 수 없다.")
    void init_fail3() {
        OrderTable AssignedTableGroupOrderTable = new OrderTable();
        AssignedTableGroupOrderTable.setTableGroup(new TableGroup());

        assertThatThrownBy(() -> TableGroup.from(List.of(AssignedTableGroupOrderTable, new OrderTable())))
                .isInstanceOf(CannotAssignOrderTableException.class);
    }
}
