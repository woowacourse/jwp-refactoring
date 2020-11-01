package kitchenpos.application;

import static kitchenpos.helper.EntityCreateHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Table;
import kitchenpos.domain.TableGroup;

@SpringBootTest
@Sql(value = "/truncate.sql")
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @DisplayName("주문테이블 추가한다.")
    @Test
    void create() {
        Table table = createTable(null, true, null, 1);

        Table actual = tableService.create(table);

        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.isEmpty()).isEqualTo(true)
        );
    }

    @DisplayName("주문테이블 리스트를 조회한다.")
    @Test
    void list() {
        Table table1 = createTable(null, true, null, 1);
        Table savedTable1 = tableService.create(table1);
        Table table2 = createTable(null, true, null, 1);
        Table savedTable2 = tableService.create(table2);

        List<Table> actual = tableService.list();

        assertAll(
            () -> assertThat(actual).hasSize(2),
            () -> assertThat(actual.get(0).getId()).isEqualTo(savedTable1.getId()),
            () -> assertThat(actual.get(1).getId()).isEqualTo(savedTable2.getId())
        );
    }

    @DisplayName("주문테이블이 없는경우에 테이블의 사람유무를 변경했을 때 예외가 발생한다.")
    @Test
    void changeEmptyWhenNoOrderTable() {
        Table table = createTable(null, true, 1L, 5);
        assertThatThrownBy(
            () -> tableService.changeEmpty(1L, table)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체가 지정되어 있는 경우 사람유무를 변경했을 때 예외가 발생한다.")
    @Test
    void changeEmptyWhenHasTableGroupId() {
        TableGroup tableGroup = createTableGroup(null, LocalDateTime.now(), Collections.emptyList());
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        Table table = createTable(null, true, savedTableGroup.getId(), 1);
        Table savedTable = orderTableDao.save(table);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), createTable(null, true, null, 5)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 들어가지 않거나, 계산이 완료되지 않은 경우 사람유무를 변경했을 때 예외가 발생한다.")
    @Test
    void changeEmptyWhenOrderStatusIsNullOrCompletion() {
        Table table = createTable(null, true, null, 1);
        Table savedTable = orderTableDao.save(table);

        Order order = createOrder(null, LocalDateTime.now(), Collections.emptyList(), OrderStatus.COOKING,
            savedTable.getId());
        orderDao.save(order);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), createTable(null, true, null, 5)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 사람의 유무 여부를 변경한다")
    @Test
    void changeEmpty() {
        Table table = createTable(null, true, null, 1);
        Table savedTable = orderTableDao.save(table);

        Order order = createOrder(null, LocalDateTime.now(), Collections.emptyList(), OrderStatus.COMPLETION,
            savedTable.getId());
        orderDao.save(order);

        Table actual = tableService.changeEmpty(savedTable.getId(), createTable(null, false, null, 5));

        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(savedTable.getId()),
            () -> assertThat(actual.isEmpty()).isEqualTo(false)
        );
    }

    @DisplayName("변경하려는 손님의 수가 0미만일 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsWhenNumberIsBelowZero() {
        Table table = createTable(null, true, null, -1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, table))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수를 변경할 때 주문 테이블이 없는 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsWhenNoOrderTable() {
        Table table = createTable(null, true, null, 3);
        Table savedTable = orderTableDao.save(table);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), table))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수를 변경할 때 주문테이블의 착석한 손님이 없는 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsWhenIsEmpty() {
        Table table = createTable(null, true, null, 1);
        Table savedTable = orderTableDao.save(table);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), table))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블의 방문한 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        Table table = createTable(null, false, null, 1);
        Table savedTable = orderTableDao.save(table);

        Table changedTable = createTable(null, false, null, 4);
        Table actual = tableService.changeNumberOfGuests(savedTable.getId(), changedTable);

        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getNumberOfGuests()).isEqualTo(4)
        );
    }
}
