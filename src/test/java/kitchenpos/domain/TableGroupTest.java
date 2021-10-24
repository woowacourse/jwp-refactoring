package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("TableGroup 엔티티 단위 테스트")
class TableGroupTest {

    @DisplayName("OrderTable 변경시 OrderTable 크기가 2 미만이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void updateOrderTables_UnderSizeTwo_Exception(int size) {
        // given
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            orderTables.add(new OrderTable());
        }

        // when, then
        assertThatCode(() -> tableGroup.updateOrderTables(orderTables))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("TableGroup에 속한 OrderTable은 최소 2개 이상이어야합니다.");
    }

    @DisplayName("OrderTable 변경시 OrderTable 크기가 2 이상이면 예외가 발생하지 않는다.")
    void updateOrderTables_UnderSizeTwo_Exception() {
        // given
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            orderTables.add(new OrderTable());
        }

        // when
        tableGroup.updateOrderTables(orderTables);

        // then
        assertThat(tableGroup.getOrderTables()).hasSize(2);
    }
}
