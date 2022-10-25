package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class TableGroupRestControllerTest extends ControllerTest {

    @Autowired
    private TableGroupRestController tableGroupController;

    @Test
    void create() {
        OrderTable orderTable1 = createOrderTable(2, true);
        OrderTable orderTable2 = createOrderTable(2, true);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

        ResponseEntity<TableGroup> response = tableGroupController.create(tableGroup);

        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void createInvalidOrderTableSize() {
        OrderTable orderTable1 = createOrderTable(2, true);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(orderTable1));

        assertThatThrownBy(() -> tableGroupController.create(tableGroup))
                .hasMessage("주문 테이블은 2개 이상이어야 합니다.");
    }

    @Test
    void createInvalidOrderTable() {
        OrderTable orderTable1 = createOrderTable(2, false);
        OrderTable orderTable2 = createOrderTable(2, true);

        TableGroup tableGroup = new TableGroup();

        tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

        assertThatThrownBy(() -> tableGroupController.create(tableGroup))
                .hasMessage("빈 주문테이블이어야 합니다.");
    }
}
