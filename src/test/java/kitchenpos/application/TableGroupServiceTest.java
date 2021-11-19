package kitchenpos.application;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TableGroupServiceTest extends IntegrationTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    void create() {
        // given
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = Arrays.asList(createOrderTable(), createOrderTable());
        tableGroup.setOrderTables(orderTables);

        // when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(savedTableGroup.getId()).isNotNull();
    }

    @Test
    void ungroup() {
        // given
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = Arrays.asList(createEmptyOrderTable(), createEmptyOrderTable());
        tableGroup.setOrderTables(orderTables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // when
        tableGroupService.ungroup(savedTableGroup.getId());

        // then
        assertThat(orderTableDao.findAllByTableGroupId(savedTableGroup.getId()))
                .allMatch(orderTable -> orderTable.getTableGroupId() == null)
                .allMatch(orderTable -> !orderTable.isEmpty());
    }

    private OrderTable createOrderTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);
        return tableService.create(orderTable);
    }

    private OrderTable createEmptyOrderTable() {
        OrderTable orderTable = createOrderTable();
        orderTable.setEmpty(true);
        return tableService.changeEmpty(orderTable.getId(), orderTable);
    }
}