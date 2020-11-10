package kitchenpos.application;

import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_1;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_2;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_3;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    private TableGroup tableGroup;

    @BeforeEach
    void setupTableGroup() {

    }

    @Test
    void create() {
        OrderTable table1 = orderTableDao.save(ORDER_TABLE_FIXTURE_1);
        OrderTable table2 = orderTableDao.save(ORDER_TABLE_FIXTURE_2);
        OrderTable table3 = orderTableDao.save(ORDER_TABLE_FIXTURE_3);

        tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(Arrays.asList(table1, table2, table3));

        TableGroup createdTableGroup = tableGroupService.create(tableGroup);

        List<Long> orderTablesGroupIds = createdTableGroup.getOrderTables()
            .stream()
            .map(OrderTable::getTableGroupId)
            .collect(Collectors.toList());
        List<Boolean> orderTablesIsEmpty = createdTableGroup.getOrderTables()
            .stream()
            .map(OrderTable::isEmpty)
            .collect(Collectors.toList());
        Long expected = createdTableGroup.getId();

        assertAll(
            () -> assertThat(orderTablesGroupIds).containsExactly(expected, expected, expected),
            () -> assertThat(orderTablesIsEmpty).containsExactly(false, false, false)
        );
    }

    @Test
    void ungroup() {
        OrderTable table1 = orderTableDao.save(ORDER_TABLE_FIXTURE_1);
        OrderTable table2 = orderTableDao.save(ORDER_TABLE_FIXTURE_2);
        OrderTable table3 = orderTableDao.save(ORDER_TABLE_FIXTURE_3);

        tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(Arrays.asList(table1, table2, table3));
        TableGroup createdTableGroup = tableGroupService.create(tableGroup);

        tableGroupService.ungroup(createdTableGroup.getId());

        List<OrderTable> orderTables = orderTableDao
            .findAllByIdIn(Arrays.asList(table1.getId(), table2.getId(), table3.getId()));

        List<Long> orderTablesGroupId = orderTables.stream()
            .map(OrderTable::getTableGroupId)
            .collect(Collectors.toList());

        List<Boolean> orderTablesIsEmpty = orderTables.stream()
            .map(OrderTable::isEmpty)
            .collect(Collectors.toList());

        assertAll(
            () -> assertThat(orderTablesGroupId).containsExactly(null, null, null),
            () -> assertThat(orderTablesIsEmpty).containsExactly(false, false, false)
        );
    }
}