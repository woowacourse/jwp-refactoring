package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SpringBootTest
class JdbcTemplateOrderTableDaoTest {
    @Autowired
    private JdbcTemplateOrderTableDao orderTableDao;
    @Autowired
    private JdbcTemplateTableGroupDao tableGroupDao;

    @Test
    @DisplayName("생성하는 경우")
    void create() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(1);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        assertThat(savedOrderTable.getId()).isNotNull();
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
        assertThat(savedOrderTable.isEmpty()).isEqualTo(orderTable.isEmpty());
    }

    @Test
    void findById() {
        OrderTable foundOrderTable = orderTableDao.findById(1L).get();

        assertThat(foundOrderTable.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("업데이트하는 경우")
    void update() {
        OrderTable foundOrderTable = orderTableDao.findById(1L).get();

        OrderTable orderTable = new OrderTable();
        orderTable.setId(foundOrderTable.getId());
        orderTable.setEmpty(!foundOrderTable.isEmpty());

        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        assertThat(savedOrderTable.getId()).isEqualTo(foundOrderTable.getId());
        assertThat(savedOrderTable.isEmpty()).isNotEqualTo(foundOrderTable.isEmpty());
    }

    @Test
    void findAll() {
        OrderTable orderTable = new OrderTable();
        List<OrderTable> orderTables = orderTableDao.findAll();
        orderTableDao.save(orderTable);
        List<OrderTable> savedOrderTables = orderTableDao.findAll();

        assertThat(savedOrderTables.size()).isEqualTo(orderTables.size() + 1);
    }

    @Test
    @DisplayName("주어진 id리스트에 orderTable의 아이디가 포함되어 있는 모든 orderTable 반환")
    void findAllByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(ids);

        OrderTable orderTable1 = orderTableDao.findById(1L).get();
        OrderTable orderTable2 = orderTableDao.findById(2L).get();
        OrderTable orderTable3 = orderTableDao.findById(3L).get();

        assertThat(orderTables.get(0)).isEqualTo(orderTable1);
        assertThat(orderTables.get(1)).isEqualTo(orderTable2);
        assertThat(orderTables.get(2)).isEqualTo(orderTable3);
    }

    @Test
    @DisplayName("주어진 table Group의 아이디가 동일한 orderTable 리스트 반환")
    void findAllByTableGroupId() {
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable1 = orderTableDao.findById(1L).get();
        OrderTable orderTable2 = orderTableDao.findById(2L).get();

        orderTable1.setTableGroupId(1L);
        orderTable2.setTableGroupId(1L);
        orderTable1.setEmpty(false);
        orderTable2.setEmpty(false);
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroupDao.save(tableGroup);
        orderTableDao.save(orderTable1);
        orderTableDao.save(orderTable2);

        List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(1L);

        assertThat(orderTables.get(0)).isEqualTo(orderTableDao.findById(1L).get());
        assertThat(orderTables.get(1)).isEqualTo(orderTableDao.findById(2L).get());
    }
}
