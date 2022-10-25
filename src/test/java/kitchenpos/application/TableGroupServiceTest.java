package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("테이블의 그룹을 해제한다.")
    @Test
    void ungroup() {
        OrderTable table1 = givenTable();
        OrderTable table2 = givenTable();
        TableGroup tableGroup = givenTableGroup(table1, table2);

        tableGroupService.ungroup(tableGroup.getId());

        List<OrderTable> tables = orderTableDao.findAllByIdIn(List.of(table1.getId(), table2.getId()));

        assertThat(tables).extracting("tableGroupId")
                .containsOnlyNulls();
    }

    private TableGroup givenTableGroup(OrderTable... tables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(tables));
        return tableGroupService.create(tableGroup);
    }

    private OrderTable givenTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);
        return orderTableDao.save(orderTable);
    }
}
