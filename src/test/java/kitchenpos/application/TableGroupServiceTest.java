package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.TableDao;
import kitchenpos.domain.Table;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.dto.TableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableDao tableDao;

    private TableGroupCreateRequest tableGroup;

    @Test
    void create() {
        Table table1 = tableDao.save(new Table(4, true));
        Table table2 = tableDao.save(new Table(5, true));
        Table table3 = tableDao.save(new Table(6, true));

        tableGroup = new TableGroupCreateRequest(Arrays.asList(table1.getId(), table2.getId(), table3.getId()));

        TableGroupResponse createdTableGroup = tableGroupService.create(tableGroup);

        List<Long> orderTablesGroupIds = createdTableGroup.getTables()
            .stream()
            .map(TableResponse::getTableGroupId)
            .collect(Collectors.toList());
        List<Boolean> orderTablesIsEmpty = createdTableGroup.getTables()
            .stream()
            .map(TableResponse::isEmpty)
            .collect(Collectors.toList());
        Long expected = createdTableGroup.getId();

        assertAll(
            () -> assertThat(orderTablesGroupIds).containsExactly(expected, expected, expected),
            () -> assertThat(orderTablesIsEmpty).containsExactly(false, false, false)
        );
    }

    @Test
    void ungroup() {
        Table table1 = tableDao.save(new Table(4, true));
        Table table2 = tableDao.save(new Table(5, true));
        Table table3 = tableDao.save(new Table(6, true));

        tableGroup = new TableGroupCreateRequest(Arrays.asList(table1.getId(), table2.getId(), table3.getId()));
        TableGroupResponse createdTableGroup = tableGroupService.create(tableGroup);

        tableGroupService.ungroup(createdTableGroup.getId());

        List<Table> tables = tableDao
            .findAllByIdIn(Arrays.asList(table1.getId(), table2.getId(), table3.getId()));

        List<Long> orderTablesGroupId = tables.stream()
            .map(Table::getTableGroupId)
            .collect(Collectors.toList());

        List<Boolean> orderTablesIsEmpty = tables.stream()
            .map(Table::isEmpty)
            .collect(Collectors.toList());

        assertAll(
            () -> assertThat(orderTablesGroupId).containsExactly(null, null, null),
            () -> assertThat(orderTablesIsEmpty).containsExactly(false, false, false)
        );
    }
}