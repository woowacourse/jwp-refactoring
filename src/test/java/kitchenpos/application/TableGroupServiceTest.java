package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import kitchenpos.BeanAssembler;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class TableGroupServiceTest {

    private TableGroupService tableGroupService;
    private OrderTableDao orderTableDao;
    private OrderDao orderDao;

    @Autowired
    public TableGroupServiceTest(DataSource dataSource) {
        this.tableGroupService = BeanAssembler.createTableGroupService(dataSource);
        this.orderTableDao = BeanAssembler.createOrderTableDao(dataSource);
        this.orderDao = BeanAssembler.createOrderDao(dataSource);
    }

    @Test
    void create() {
        // given
        TableGroup tableGroup = new TableGroup(
                LocalDateTime.now(),
                List.of(new OrderTable(1L, null, 0, true), new OrderTable(2L, null, 0, true))
        );

        // when
        TableGroup createdTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(createdTableGroup.getId()).isNotNull();
    }

    @Test
    void createWithEmptyOrderTable() {
        // given
        TableGroup tableGroup = new TableGroup(
                LocalDateTime.now(),
                new ArrayList<>()
        );

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createWithOneOrderTable() {
        // given
        TableGroup tableGroup = new TableGroup(
                LocalDateTime.now(),
                List.of(new OrderTable(1L, null, 0, true))
        );

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createWithWrongOrderTable() {
        // given
        long wrongOrderTableId = 999L;
        TableGroup tableGroup = new TableGroup(
                LocalDateTime.now(),
                List.of(new OrderTable(1L, null, 0, true), new OrderTable(wrongOrderTableId, null, 0, true))
        );

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createWithNotEmptyOrderTable() {
        // given
        OrderTable emptyOrderTable = orderTableDao.save(new OrderTable(1L, null, 2, false));
        TableGroup tableGroup = new TableGroup(
                LocalDateTime.now(),
                List.of(emptyOrderTable, new OrderTable(2L, null, 0, true))
        );

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void ungroup() {
        // given
        TableGroup tableGroup = new TableGroup(
                LocalDateTime.now(),
                List.of(new OrderTable(1L, null, 0, true), new OrderTable(2L, null, 0, true))
        );
        TableGroup createdTableGroup = tableGroupService.create(tableGroup);

        // when & then
        assertThatCode(() -> tableGroupService.ungroup(createdTableGroup.getId()))
                .doesNotThrowAnyException();
    }

    @Test
    void ungroupWithCookingStatus() {
        // given
        long orderTableId = 1L;
        Order order = new Order(orderTableId, OrderStatus.COOKING.name(), LocalDateTime.now(), null);
        orderDao.save(order);

        TableGroup tableGroup = new TableGroup(
                LocalDateTime.now(),
                List.of(new OrderTable(orderTableId, null, 0, true), new OrderTable(2L, null, 0, true))
        );
        TableGroup createdTableGroup = tableGroupService.create(tableGroup);

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(createdTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
