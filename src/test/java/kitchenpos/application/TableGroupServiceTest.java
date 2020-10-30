package kitchenpos.application;

import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_1;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_2;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_3;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    void create() {
        OrderTable table1 = orderTableDao.save(ORDER_TABLE_FIXTURE_1);
        OrderTable table2 = orderTableDao.save(ORDER_TABLE_FIXTURE_2);
        OrderTable table3 = orderTableDao.save(ORDER_TABLE_FIXTURE_3);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(Arrays.asList(table1, table2, table3));

        TableGroup createdTableGroup = tableGroupService.create(tableGroup);
        table1.setTableGroupId(tableGroup.getId());
        table2.setTableGroupId(tableGroup.getId());
        table3.setTableGroupId(tableGroup.getId());
        orderTableDao.save(table1);
        orderTableDao.save(table2);
        orderTableDao.save(table3);

        assertThat(createdTableGroup.getCreatedDate()).isEqualTo(tableGroup.getCreatedDate());
        assertThat(createdTableGroup.getOrderTables().size()).isEqualTo(tableGroup.getOrderTables().size());
    }

    @Test
    void ungroup() {
    }
}