package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTablesTest {

    @DisplayName("주문 테이블이 2개 미만인 주문 테이블 목록을 생성할 수 없다")
    @Test
    void create_sizeUnderTwo() {
        List<OrderTable> tables = List.of(new OrderTable(4, true));

        assertThatThrownBy(() -> new OrderTables(tables)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문할 수 있는 테이블이 하나라도 있으면 그룹핑할 수 없다")
    @Test
    void canGroup_existNonEmptyTable() {
        List<OrderTable> tables = List.of(
                new OrderTable(1L, null, 4, true),
                new OrderTable(2L, null,3, false));
        OrderTables orderTables = new OrderTables(tables);

        boolean result = orderTables.canGroup(List.of(1L, 2L));

        assertThat(result).isFalse();
    }

    @DisplayName("이미 그룹핑이 된 테이블이 하나라도 있으면 그룹핑할 수 없다")
    @Test
    void canGroup_existGroupedTable() {
        List<OrderTable> tables = List.of(
                new OrderTable(1L, 1L, 4, true),
                new OrderTable(2L, null,3, true));
        OrderTables orderTables = new OrderTables(tables);

        boolean result = orderTables.canGroup(List.of(1L, 2L));

        assertThat(result).isFalse();
    }

    @DisplayName("존재하지 않는 테이블이 하나라도 있으면 그룹핑할 수 없다")
    @Test
    void canGroup_notExistTable() {
        List<OrderTable> tables = List.of(
                new OrderTable(1L, null, 4, true),
                new OrderTable(2L, null,3, true));
        OrderTables orderTables = new OrderTables(tables);

        boolean result = orderTables.canGroup(List.of(1L));

        assertThat(result).isFalse();
    }
}
