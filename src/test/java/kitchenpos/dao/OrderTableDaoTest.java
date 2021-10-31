package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import kitchenpos.ordertable.domain.OrderTableDao;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderTableDaoTest extends DaoTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Test
    void save() throws Exception {
        OrderTable savedOrderTable = orderTableDao.save(new OrderTable(10, true));
        OrderTable foundOrderTable = orderTableDao.findById(savedOrderTable.getId())
            .orElseThrow(() -> new Exception());
        assertThat(savedOrderTable.getId()).isEqualTo(foundOrderTable.getId());
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(foundOrderTable.getNumberOfGuests());
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(foundOrderTable.getNumberOfGuests());
    }

    @Test
    void findById() throws Exception {
        OrderTable savedOrderTable = orderTableDao.save(new OrderTable(10, true));
        OrderTable foundOrderTable = orderTableDao.findById(savedOrderTable.getId())
            .orElseThrow(() -> new Exception());
        assertThat(savedOrderTable.getId()).isEqualTo(foundOrderTable.getId());
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(foundOrderTable.getNumberOfGuests());
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(foundOrderTable.getNumberOfGuests());
    }

    @Test
    void findAll() {
        orderTableDao.save(new OrderTable(10, true));
        orderTableDao.save(new OrderTable(10, true));
        assertThat(orderTableDao.findAll()).hasSize(2);
    }

    @Test
    void findAllByOrderId() {
        OrderTable orderTable1 = orderTableDao.save(new OrderTable(10, true));
        OrderTable orderTable2 = orderTableDao.save(new OrderTable(10, true));
        assertThat(orderTableDao.findAllByIdIn(Arrays.asList(orderTable1.getId(), orderTable2.getId()))).hasSize(2);
    }

    @Test
    void findAllByTableGroupId() {
        TableGroup tableGroup = tableGroupDao.save(new TableGroup());
        orderTableDao.save(new OrderTable(tableGroup, 10, true));
        orderTableDao.save(new OrderTable(tableGroup, 10, true));
        assertThat(orderTableDao.findAllByTableGroupId(tableGroup.getId())).hasSize(2);
    }
}
