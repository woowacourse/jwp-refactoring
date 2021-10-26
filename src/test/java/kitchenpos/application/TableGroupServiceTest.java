package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("단체 지정 서비스 테스트")
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;


    @DisplayName("단체 지정을 생성한다.")
    @Test
    void create() {
        OrderTable orderTable1 = OrderTable.EMPTY_TABLE;
        OrderTable orderTable2 = OrderTable.EMPTY_TABLE;
        tableService.create(orderTable1);
        tableService.create(orderTable2);

        TableGroup tableGroup = new TableGroup(tableService.list());
        TableGroup created = tableGroupService.create(tableGroup);

        assertAll(
            () -> assertThat(created.getId()).isNotNull(),
            () -> assertThat(created.getOrderTables()).isNotEmpty()
        );
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() {
        OrderTable orderTable1 = OrderTable.EMPTY_TABLE;
        OrderTable orderTable2 = OrderTable.EMPTY_TABLE;
        tableService.create(orderTable1);
        tableService.create(orderTable2);
        TableGroup tableGroup = new TableGroup(tableService.list());
        TableGroup created = tableGroupService.create(tableGroup);

        tableGroupService.ungroup(created.getId());
        boolean actual = tableService.list().stream()
            .noneMatch(table -> created.getId().equals(table.getTableGroup().getId()));
        assertThat(actual).isTrue();
    }
}