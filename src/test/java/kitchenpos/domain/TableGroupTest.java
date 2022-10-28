package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @DisplayName("새로운 테이블 그룹의 주문 테이블이 비어있거나 그룹화하려는 주문 테이블이 2개 보다 작을 수는 없다.")
    @Test
    void canNotCreateTableGroupLessThenTwoTable() {
        assertThatThrownBy(() -> TableGroup.of(LocalDateTime.now(),
                List.of(OrderTable.of(1, false))));
    }

    @DisplayName("테이블이 비어있고 이미 단체 지정되지 않은 경우에만 새롭게 지정할 수 있다.")
    @Test
    void canNotCreateTableGroupWhenAlreadyGrouping() {
        // given
        final OrderTable orderTable1 = OrderTable.of(1, false);
        final OrderTable orderTable2 = OrderTable.of(0, true);

        // when & then
        assertThatThrownBy(() -> TableGroup.of(LocalDateTime.now(), List.of(orderTable1, orderTable2)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 이미 그룹에 지정되어 그룹 id 를 가지고 있으면 그룹핑 할 수 없다.")
    @Test
    void canNotCreateTableGroupWhenTableInGroup() {
        // given
        final OrderTable orderTable1 = new OrderTable(1L, 0, true);
        final OrderTable orderTable2 = OrderTable.of(0, true);

        // when & then
        assertThatThrownBy(() -> TableGroup.of(LocalDateTime.now(), List.of(orderTable1, orderTable2)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 조리 중이거나 식사중인 테이블이 있으면 해제할 수 없다.")
    @Test
    void canNotUngroupWhenCookOrMeal() {
        // given
        final Order order = new Order(1L, "COOKING", LocalDateTime.now(), List.of());
        final OrderTable orderTable1 = new OrderTable(1L, 1L, 1, false, List.of(order));
        final OrderTable orderTable2 = new OrderTable(2L, 1L, 1, false);
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));

        assertThatThrownBy(() -> tableGroup.ungroup())
                .isInstanceOf(IllegalArgumentException.class);
    }
}
