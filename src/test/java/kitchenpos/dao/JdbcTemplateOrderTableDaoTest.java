package kitchenpos.dao;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcTemplateOrderTableDaoTest extends JdbcTemplateTest {

    private OrderTableDao orderTableDao;
    private TableGroupDao tableGroupDao;
    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        orderTableDao = new JdbcTemplateOrderTableDao(dataSource);
        tableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
        tableGroup = makeTableGroup();
    }

    @Test
    void 주문_테이블을_저장한다() {
        TableGroup savedTG = tableGroupDao.save(tableGroup);

        OrderTable orderTable = makeOrderTable(savedTG);
        OrderTable saved = orderTableDao.save(orderTable);

        assertThat(saved.getTableGroupId()).isEqualTo(orderTable.getTableGroupId());
        assertThat(saved.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
        assertThat(saved.isEmpty()).isFalse();
    }

    @Test
    void 식별자로_주문_테이블을_조회한다() {
        TableGroup savedTG = tableGroupDao.save(tableGroup);

        OrderTable orderTable = makeOrderTable(savedTG);
        OrderTable saved = orderTableDao.save(orderTable);

        OrderTable expected = orderTableDao.findById(saved.getId()).get();
        assertThat(expected.getId()).isEqualTo(saved.getId());
    }

    @Test
    void 모든_주문테이블을_조회한다() {
        List<OrderTable> orderTables = orderTableDao.findAll();
        assertThat(orderTables.size()).isEqualTo(8);
    }

    @Test
    void 여러_식별자에_해당하는_주문테이블들을_조회한다() {
        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(List.of(1L, 2L, 3L));
        assertThat(orderTables.size()).isEqualTo(3);
    }

    @Test
    void 테이블_그룹_아이디에_해당하는_주문_테이블들을_조회한다() {
        TableGroup savedTG = tableGroupDao.save(tableGroup);

        OrderTable orderTable = makeOrderTable(savedTG);
        orderTableDao.save(orderTable);

        List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(savedTG.getId());
        assertThat(orderTables.size()).isEqualTo(1);
    }

    private TableGroup makeTableGroup() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroup;
    }

    private OrderTable makeOrderTable(TableGroup savedTG) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(savedTG.getId());
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(3);
        return orderTable;
    }
}
