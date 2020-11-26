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
import kitchenpos.domain.Table;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableDaoTest {

    @Autowired
    private TableDao tableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Test
    void save() {
        Table table = ORDER_TABLE_FIXTURE_1;

        Table persistTable = tableDao.save(table);

        assertAll(
            () -> assertThat(persistTable.getId()).isNotNull(),
            () -> assertThat(persistTable.getTableGroupId()).isNull(),
            () -> assertThat(persistTable).isEqualToIgnoringGivenFields(table, "id", "tableGroupId")
        );

    }

    @Test
    void findById() {
        Table persistTable = tableDao.save(ORDER_TABLE_FIXTURE_1);

        Table findTable = tableDao.findById(persistTable.getId()).get();

        assertThat(findTable).isEqualToComparingFieldByField(persistTable);
    }

    @Test
    void findAll() {
        tableDao.save(ORDER_TABLE_FIXTURE_1);
        tableDao.save(ORDER_TABLE_FIXTURE_2);

        List<Table> tables = tableDao.findAll();
        List<Long> tableGroupIds = tables.stream()
            .map(Table::getTableGroupId)
            .collect(Collectors.toList());

        assertThat(tableGroupIds)
            .contains(ORDER_TABLE_FIXTURE_1.getTableGroupId(), ORDER_TABLE_FIXTURE_2.getTableGroupId());
    }

    @Test
    void findAllByIdIn() {
        Table table1 = tableDao.save(ORDER_TABLE_FIXTURE_1);
        Table table2 = tableDao.save(ORDER_TABLE_FIXTURE_2);
        Table table3 = tableDao.save(ORDER_TABLE_FIXTURE_3);

        List<Table> tables = tableDao.findAllByIdIn(Arrays.asList(table1.getId(), table2.getId(), table3.getId()));
        List<Long> tableGroupIds = tables.stream()
            .map(Table::getTableGroupId)
            .collect(Collectors.toList());

        assertThat(tableGroupIds)
            .contains(ORDER_TABLE_FIXTURE_1.getTableGroupId(), ORDER_TABLE_FIXTURE_2.getTableGroupId(),
                ORDER_TABLE_FIXTURE_3.getTableGroupId());
    }

    @Test
    void findAllByTableGroupId() {
        Table tableFixture1 = ORDER_TABLE_FIXTURE_1;
        Table tableFixture4 = ORDER_TABLE_FIXTURE_4;

        tableDao.save(tableFixture1);
        tableDao.save(ORDER_TABLE_FIXTURE_2);
        tableDao.save(ORDER_TABLE_FIXTURE_3);
        tableDao.save(tableFixture4);
        TableGroup persistTableGroup = tableGroupDao.save(TABLE_GROUP_FIXTURE_1);

        tableFixture1.setTableGroupId(persistTableGroup.getId());
        tableFixture4.setTableGroupId(persistTableGroup.getId());
        tableDao.save(tableFixture1);
        tableDao.save(tableFixture4);

        List<Table> tables = tableDao.findAllByTableGroupId(persistTableGroup.getId());

        assertThat(tables).hasSizeGreaterThanOrEqualTo(2);
    }
}
