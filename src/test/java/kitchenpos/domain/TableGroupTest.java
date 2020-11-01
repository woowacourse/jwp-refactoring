package kitchenpos.domain;

import static kitchenpos.TestObjectFactory.createOrderTable;
import static kitchenpos.TestObjectFactory.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @DisplayName("[예외] 테이블을 포함하지 않은 테이블 그룹 생성")
    @Test
    void create_Fail_With_NoTable() {
        assertThatThrownBy(
            () -> TableGroup.builder().build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 2개 미만의 테이블을 포함한 테이블 그룹 생성")
    @Test
    void create_Fail_With_LessTable() {
        OrderTable orderTable = createOrderTable(true);
        List<OrderTable> orderTables = Arrays.asList(orderTable);

        assertThatThrownBy(
            () ->TableGroup.builder()
                .orderTables(orderTables)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹 생성 시 주문테이블 그룹화 및 사용 가능 여부 변경 확인")
    @Test
    void setOrderTables() {
        OrderTable orderTable1 = createOrderTable(true);
        OrderTable orderTable2 = createOrderTable(true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        TableGroup tableGroup = createTableGroup(orderTables);

        assertAll(
            () -> assertThat(orderTable1.getTableGroup()).isEqualTo(tableGroup),
            () -> assertThat(orderTable1.isEmpty()).isFalse(),
            () -> assertThat(orderTable2.getTableGroup()).isEqualTo(tableGroup),
            () -> assertThat(orderTable2.isEmpty()).isFalse()
        );
    }

    @DisplayName("그룹 해제 시 모든 테이블의 그룹 등록 해제 여부 확인")
    @Test
    void ungroup() {
        OrderTable orderTable1 = createOrderTable(true);
        OrderTable orderTable2 = createOrderTable(true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        TableGroup tableGroup = createTableGroup(orderTables);

        tableGroup.ungroup();

        assertAll(
            () -> assertThat(tableGroup.getOrderTables()).isEmpty(),
            () -> assertThat(orderTable1.getTableGroup()).isNull(),
            () -> assertThat(orderTable2.getTableGroup()).isNull()
        );
    }
}