package kitchenpos.dao;

import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_1;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_2;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_3;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_4;
import static kitchenpos.fixture.TableGroupFixture.TABLE_GROUP_FIXTURE_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderTableDaoTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Test
    void save() {
        OrderTable orderTable = ORDER_TABLE_FIXTURE_1;

        OrderTable persistOrderTable = orderTableDao.save(orderTable);

        assertAll(
            () -> assertThat(persistOrderTable.getId()).isNotNull(),
            () -> assertThat(persistOrderTable.getTableGroupId()).isNull(),
            () -> assertThat(persistOrderTable).isEqualToIgnoringGivenFields(orderTable, "id", "tableGroupId")
        );

    }

    @Test
    void findById() {
        OrderTable persistOrderTable = orderTableDao.save(ORDER_TABLE_FIXTURE_1);

        OrderTable findOrderTable = orderTableDao.findById(persistOrderTable.getId()).get();

        assertThat(findOrderTable).isEqualToComparingFieldByField(persistOrderTable);
    }

    @Test
    void findAll() {
        orderTableDao.save(ORDER_TABLE_FIXTURE_1);
        orderTableDao.save(ORDER_TABLE_FIXTURE_2);

        List<OrderTable> orderTables = orderTableDao.findAll();
        List<Long> tableGroupIds = orderTables.stream()
            .map(OrderTable::getTableGroupId)
            .collect(Collectors.toList());

        assertThat(tableGroupIds)
            .contains(ORDER_TABLE_FIXTURE_1.getTableGroupId(), ORDER_TABLE_FIXTURE_2.getTableGroupId());
    }

    @Test
    void findAllByIdIn() {
        orderTableDao.save(ORDER_TABLE_FIXTURE_1);
        orderTableDao.save(ORDER_TABLE_FIXTURE_2);
        orderTableDao.save(ORDER_TABLE_FIXTURE_3);

        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L, 3L));
        List<Long> tableGroupIds = orderTables.stream()
            .map(OrderTable::getTableGroupId)
            .collect(Collectors.toList());

        assertThat(tableGroupIds)
            .contains(ORDER_TABLE_FIXTURE_1.getTableGroupId(), ORDER_TABLE_FIXTURE_2.getTableGroupId(),
                ORDER_TABLE_FIXTURE_3.getTableGroupId());
    }

    @Test
    void findAllByTableGroupId() {
        OrderTable orderTableFixture1 = ORDER_TABLE_FIXTURE_1;
        OrderTable orderTableFixture4 = ORDER_TABLE_FIXTURE_4;

        orderTableDao.save(orderTableFixture1);
        orderTableDao.save(ORDER_TABLE_FIXTURE_2);
        orderTableDao.save(ORDER_TABLE_FIXTURE_3);
        orderTableDao.save(orderTableFixture4);
        TableGroup persistTableGroup = tableGroupDao.save(TABLE_GROUP_FIXTURE_1);

        orderTableFixture1.setTableGroupId(persistTableGroup.getId());
        orderTableFixture4.setTableGroupId(persistTableGroup.getId());
        orderTableDao.save(orderTableFixture1);
        orderTableDao.save(orderTableFixture4);

        List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(persistTableGroup.getId());

        assertThat(orderTables).hasSizeGreaterThanOrEqualTo(2);
    }
}
