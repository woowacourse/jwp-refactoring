package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TableGroupTest {

    @Test
    void invalidTableNumber() {
        OrderTable table = new OrderTable(4);

        assertThatThrownBy(() -> new TableGroup(List.of(table))).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹 지정은 최소 2개 이상의 테이블부터 가능합니다.");
    }

    @Test
    void alreadyGroupedTable() {
        OrderTable table1 = new OrderTable(4).changeGroupTo(1L);
        OrderTable table2 = new OrderTable(4);

        assertThatThrownBy(() -> new TableGroup()List.of(table1, table2)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 그룹이 지정된 테이블이 존재합니다.");
    }
}
