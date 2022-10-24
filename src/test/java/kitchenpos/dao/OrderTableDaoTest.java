package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.RepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class OrderTableDaoTest {

    private TableGroupDao tableGroupDao;
    private OrderTableDao orderTableDao;

    @Autowired
    public OrderTableDaoTest(TableGroupDao tableGroupDao, OrderTableDao orderTableDao) {
        this.tableGroupDao = tableGroupDao;
        this.orderTableDao = orderTableDao;
    }

    private Long tableGroupId;

    @BeforeEach
    void setUp() {
        tableGroupId = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null)).getId();
    }

    @Test
    void save() {
        // given
        OrderTable orderTable = new OrderTable(tableGroupId, 4, false);
        // when
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        // then
        assertThat(savedOrderTable.getId()).isNotNull();
    }

    @Test
    void findById() {
        // given
        OrderTable orderTable = new OrderTable(tableGroupId, 4, false);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        // when
        Optional<OrderTable> foundOrderTable = orderTableDao.findById(savedOrderTable.getId());

        // then
        assertThat(foundOrderTable).isNotNull();
    }

    @Test
    void findAll() {
        // given
        orderTableDao.save(new OrderTable(tableGroupId, 4, false));
        orderTableDao.save(new OrderTable(tableGroupId, 2, false));

        // when
        List<OrderTable> orderTables = orderTableDao.findAll();

        // then
        int defaultSize = 8;
        assertThat(orderTables).hasSize(2 + defaultSize);
    }

    @Test
    void findAllByIdIn() {
        // given
        OrderTable orderTableA = orderTableDao.save(new OrderTable(tableGroupId, 4, false));
        OrderTable orderTableB = orderTableDao.save(new OrderTable(tableGroupId, 2, false));

        // when
        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(List.of(orderTableA.getId(), orderTableB.getId()));

        // then
        assertThat(orderTables).hasSize(2);
    }

    @Test
    void findAllByTableGroupId() {
        // given
        orderTableDao.save(new OrderTable(tableGroupId, 4, false));
        orderTableDao.save(new OrderTable(tableGroupId, 2, false));

        // when
        List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        // then
        assertThat(orderTables).hasSize(2);
    }
}
