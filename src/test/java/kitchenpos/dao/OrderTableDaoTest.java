package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableDaoTest extends DaoTest {

    private OrderTableDao orderTableDao;
    private TableGroupDao tableGroupDao;

    @BeforeEach
    void setUp() {
        orderTableDao = new JdbcTemplateOrderTableDao(dataSource);
        tableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
    }

    @Test
    @DisplayName("OrderTable을 저장한다.")
    void save() {
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));
        OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 10, false));

        assertThat(orderTable).isEqualTo(orderTableDao.findById(orderTable.getId()).orElseThrow());
    }

    @Test
    @DisplayName("모든 OrderTable을 조회한다.")
    void findAll() {
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));
        OrderTable orderTable1 = orderTableDao.save(new OrderTable(tableGroup.getId(), 10, false));
        OrderTable orderTable2 = orderTableDao.save(new OrderTable(tableGroup.getId(), 5, false));
        List<OrderTable> orderTables = orderTableDao.findAll();

        assertAll(
                () -> assertThat(orderTables).isNotEmpty(),
                () -> assertThat(orderTables).contains(orderTable1, orderTable2)
        );
    }

    @Test
    @DisplayName("Id에 포함되는 모든 OrderTable을 조회한다.")
    void findAllByIdIn() {
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));
        OrderTable orderTable1 = orderTableDao.save(new OrderTable(tableGroup.getId(), 10, false));
        OrderTable orderTable2 = orderTableDao.save(new OrderTable(tableGroup.getId(), 5, false));
        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(List.of(orderTable1.getId(), orderTable2.getId()));

        assertThat(orderTables).containsExactly(orderTable1, orderTable2);
    }

    @Test
    @DisplayName("TableGroup 내 모든 OrderTable을 조회한다.")
    void findAllByTableGroupId() {
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));
        OrderTable orderTable1 = orderTableDao.save(new OrderTable(tableGroup.getId(), 10, false));
        OrderTable orderTable2 = orderTableDao.save(new OrderTable(tableGroup.getId(), 5, false));
        List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroup.getId());

        assertThat(orderTables).containsExactly(orderTable1, orderTable2);
    }
}
