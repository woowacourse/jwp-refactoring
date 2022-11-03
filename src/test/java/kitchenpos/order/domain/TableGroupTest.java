package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    @DisplayName("주문 그룹이 비어있으면 예외 발생")
    void whenOrderTableIsEmpty() {
        assertThatThrownBy(() -> TableGroup.of(new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 그룹이 2개 미만이면 예외 발생")
    void whenOrderTableIsUnderTwo() {
        assertThatThrownBy(() -> TableGroup.of(Collections.singletonList(new OrderTable(null, 0, true))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 빈 테이블이 아닐 경우 예외 발생")
    void whenOrderTableIsNotEmptyTable() {
        final OrderTable emptyTable = new OrderTable(null, 0, true);
        final OrderTable orderTable = new OrderTable(null, 0, false);

        assertThatThrownBy(() -> TableGroup.of(List.of(emptyTable, orderTable)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문테이블들의 사이즈와 다를 경우 예외 발생")
    void checkOrderTableSize() {
        final TableGroup tableGroup = TableGroup.of(
                List.of(new OrderTable(null, 0, true), new OrderTable(null, 0, true)));

        assertThatThrownBy(() -> tableGroup.checkOrderTableSize(5))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
