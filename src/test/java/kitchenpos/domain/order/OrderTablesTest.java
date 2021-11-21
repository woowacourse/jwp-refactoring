package kitchenpos.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.TableFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class OrderTablesTest {

    @DisplayName("주문 테이블들 수가 2 미만일 경우 생성할 수 없다.")
    @Test
    void createExceptionIfSizeLessThanTwo() {
        final OrderTable orderTable1 = createOrderTable(true);

        assertThatThrownBy(() -> new OrderTables(Collections.singletonList(orderTable1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블이 아닌 주문 테이블을 포함한 경우 주문 테이블들을 경우 생성할 수 없다.")
    @Test
    void createExceptionIfNotEmpty() {
        final OrderTable orderTable1 = createOrderTable(false);
        final OrderTable orderTable2 = createOrderTable(true);

        assertThatThrownBy(() -> new OrderTables(Arrays.asList(orderTable1, orderTable2)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 아닌 주문 테이블을 포함한 경우 주문 테이블들을 경우 생성할 수 없다.")
    @Test
    void createExceptionIfHasGroup() {
        final TableGroup tableGroup = new TableGroup();
        final OrderTable orderTable1 = createOrderTable(tableGroup, true);
        final OrderTable orderTable2 = createOrderTable(true);

        assertThatThrownBy(() -> new OrderTables(Arrays.asList(orderTable1, orderTable2)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블들을 성공적으로 생성한다.")
    @Test
    void create() {
        final OrderTable orderTable1 = createOrderTable(true);
        final OrderTable orderTable2 = createOrderTable(true);

        assertDoesNotThrow(() -> new OrderTables(Arrays.asList(orderTable1, orderTable2)));
    }

    @DisplayName("주문 테이블들에 단체 지정을 설정한다.")
    @Test
    void setTableGroup() {
        final OrderTable orderTable1 = createOrderTable(true);
        final OrderTable orderTable2 = createOrderTable(true);
        final OrderTables orderTables = new OrderTables(Arrays.asList(orderTable1, orderTable2));
        final TableGroup tableGroup = new TableGroup();

        orderTables.addTableGroup(tableGroup);

        assertAll(
                () -> assertThat(orderTables.getOrderTables().get(0).getTableGroup()).isNotNull(),
                () -> assertThat(orderTables.getOrderTables().get(1).getTableGroup()).isNotNull()
        );
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() {
        final OrderTable orderTable1 = createOrderTable(true);
        final OrderTable orderTable2 = createOrderTable(true);
        final OrderTables orderTables = new OrderTables(Arrays.asList(orderTable1, orderTable2));
        final TableGroup tableGroup = new TableGroup();
        orderTables.addTableGroup(tableGroup);

        orderTables.ungroup();

        assertAll(
                () -> assertThat(orderTables.getOrderTables().get(0).getTableGroup()).isNull(),
                () -> assertThat(orderTables.getOrderTables().get(1).getTableGroup()).isNull()
        );
    }
}