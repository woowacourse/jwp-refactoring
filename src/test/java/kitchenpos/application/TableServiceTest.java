package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import javax.sql.DataSource;
import kitchenpos.BeanAssembler;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class TableServiceTest {

    private TableService tableService;
    private OrderDao orderDao;
    private OrderTableDao orderTableDao;
    private TableGroupDao tableGroupDao;

    @Autowired
    public TableServiceTest(DataSource dataSource) {
        this.tableService = BeanAssembler.createTableService(dataSource);
        this.orderDao = BeanAssembler.createOrderDao(dataSource);
        this.orderTableDao = BeanAssembler.createOrderTableDao(dataSource);
        this.tableGroupDao = BeanAssembler.createTableGroupDao(dataSource);
    }

    @Test
    void create() {
        // given
        OrderTable orderTable = new OrderTable(null, 3, true);
        // when
        OrderTable createdOrderTable = tableService.create(orderTable);
        // then
        assertThat(createdOrderTable.getId()).isNotNull();
    }

    @Test
    void list() {
        // given & when
        List<OrderTable> orderTables = tableService.list();
        // then
        assertThat(orderTables).hasSize(8);
    }

    @Test
    void changeEmpty() {
        // given
        OrderTable orderTable = tableService.create(new OrderTable(null, 0, true));

        // when
        OrderTable changedOrderTable = tableService.changeEmpty(
                orderTable.getId(),
                new OrderTable(null, 3, false)
        );

        // then
        assertThat(changedOrderTable.isEmpty()).isFalse();
    }

    @Test
    void changeEmptyWithTableGroupId() {
        // given
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 3, false));

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTable(null, 0, true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeEmptyWithCookingStatus() {
        // given
        long orderTableId = 1L;
        Order order = new Order(orderTableId, OrderStatus.COOKING.name(), LocalDateTime.now(), null);
        orderDao.save(order);
        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, new OrderTable(null, 3, false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));

        // when
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(
                orderTable.getId(),
                new OrderTable(null, 4, true)
        );

        // then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(4);
    }
    
    @Test
    void changeNumberOfGuestsWithNegativeNumber() {
        // given
        OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(
                orderTable.getId(),
                new OrderTable(null, -1, true)
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuestsWithEmpty() {
        // given
        OrderTable orderTable = tableService.create(new OrderTable(null, 0, true));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(
                orderTable.getId(),
                new OrderTable(null, 2, false)
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuestsWithInvalidOrderTableId() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(
                999L,
                new OrderTable(null, 2, false)
        )).isInstanceOf(IllegalArgumentException.class);
    }
}
