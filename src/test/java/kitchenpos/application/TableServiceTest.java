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
import kitchenpos.domain.OrderTable;
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
        OrderTable orderTable = createOrderTable(null, true, null, 1);

        OrderTable actual = tableService.create(orderTable);

        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.isEmpty()).isEqualTo(true)
        );
    }

    @DisplayName("주문테이블 리스트를 조회한다.")
    @Test
    void list() {
        OrderTable table1 = createOrderTable(null, true, null, 1);
        OrderTable savedOrderTable1 = tableService.create(table1);
        OrderTable table2 = createOrderTable(null, true, null, 1);
        OrderTable savedOrderTable2 = tableService.create(table2);

        List<OrderTable> actual = tableService.list();

        assertAll(
            () -> assertThat(actual).hasSize(2),
            () -> assertThat(actual.get(0).getId()).isEqualTo(savedOrderTable1.getId()),
            () -> assertThat(actual.get(1).getId()).isEqualTo(savedOrderTable2.getId())
        );
    }

    @DisplayName("주문테이블이 없는경우에 테이블의 사람유무를 변경했을 때 예외가 발생한다.")
    @Test
    void changeEmptyWhenNoOrderTable() {
        assertThatThrownBy(
            () -> tableService.changeEmpty(1L, new OrderTable())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체가 지정되어 있는 경우 사람유무를 변경했을 때 예외가 발생한다.")
    @Test
    void changeEmptyWhenHasTableGroupId() {
        TableGroup tableGroup = createTableGroup(null, LocalDateTime.now(), Collections.emptyList());
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        OrderTable orderTable = createOrderTable(null, true, savedTableGroup.getId(), 1);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), new OrderTable()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 들어가지 않거나, 계산이 완료되지 않은 경우 사람유무를 변경했을 때 예외가 발생한다.")
    @Test
    void changeEmptyWhenOrderStatusIsNullOrCompletion() {
        OrderTable orderTable = createOrderTable(null, true, null, 1);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        Order order = createOrder(null, LocalDateTime.now(), Collections.emptyList(), OrderStatus.COOKING,
            savedOrderTable.getId());
        orderDao.save(order);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), new OrderTable()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 사람의 유무 여부를 변경한다")
    @Test
    void changeEmpty() {
        OrderTable orderTable = createOrderTable(null, true, null, 1);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        Order order = createOrder(null, LocalDateTime.now(), Collections.emptyList(), OrderStatus.COMPLETION,
            savedOrderTable.getId());
        orderDao.save(order);

        OrderTable actual = tableService.changeEmpty(savedOrderTable.getId(), new OrderTable());

        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(savedOrderTable.getId()),
            () -> assertThat(actual.isEmpty()).isEqualTo(false)
        );
    }

    @DisplayName("변경하려는 손님의 수가 0미만일 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsWhenNumberIsBelowZero() {
        OrderTable orderTable = createOrderTable(null, true, null, -1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수를 변경할 때 주문 테이블이 없는 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsWhenNoOrderTable() {
        OrderTable orderTable = createOrderTable(null, true, null, 3);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수를 변경할 때 주문테이블의 착석한 손님이 없는 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsWhenIsEmpty() {
        OrderTable orderTable = createOrderTable(null, true, null, 1);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블의 방문한 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = createOrderTable(null, false, null, 1);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        OrderTable changedTable = createOrderTable(null, false, null, 4);
        OrderTable actual = tableService.changeNumberOfGuests(savedOrderTable.getId(), changedTable);

        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getNumberOfGuests()).isEqualTo(4)
        );
    }
}
