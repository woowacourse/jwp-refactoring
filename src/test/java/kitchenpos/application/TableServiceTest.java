package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("OrderTable을 생성할 수 있다.")
    @Test
    void create() {
        OrderTable orderTable = new OrderTable(null, 3, false);

        tableService.create(orderTable);

        assertThat(tableService.list()).hasSize(9);
    }

    @DisplayName("OrderTable의 empty 상태를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        OrderTable orderTable = tableService.create(new OrderTable(null, 3, false));

        tableService.changeEmpty(orderTable.getId(), new OrderTable(null, 3, true));

        Optional<OrderTable> changedOrderTable = tableService.list()
                .stream()
                .filter(OrderTable::isEmpty)
                .findAny();
        assertThat(changedOrderTable).isNotEmpty();
    }

    @DisplayName("TableGroup에 속해있는 OrderTable의 empty 상태를 변경하려고 하면 예외를 발생시킨다.")
    @Test
    void changeEmpty_Exception_GroupedTable() {
        TableGroup tableGroup = tableGroupDao.save(new TableGroup());
        OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 3, true));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTable(null, 3, true)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Table Group으로 묶인 테이블은 empty를 변경할 수 없습니다.");
    }

    @DisplayName("COOKING, MEAL 상태인 OrderTable의 상태를 변경하려고 하면 예외를 발생시킨다.")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeEmpty_Exception_NotCompleteOrderStatus(String orderStatus) {
        OrderTable orderTable = orderTableDao.save(new OrderTable(null, 3, false));
        orderDao.save(new Order(orderTable.getId(), orderStatus));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTable(null, 3, true)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조리중이거나 식사중인 테이블의 empty를 변경할 수 없습니다.");
    }

    @DisplayName("OrderTable의 numberOfGuests 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(null, 3, false));

        tableService.changeNumberOfGuests(orderTable.getId(), new OrderTable(null, 200, false));

        Optional<OrderTable> changedOrderTable = tableService.list()
                .stream()
                .filter(orderTable1 -> orderTable1.getNumberOfGuests() == 200)
                .findAny();
        assertThat(changedOrderTable).isNotEmpty();
    }

    @DisplayName("0보다 작은 numberOfGuests로 OrderTable을 변경하려고 하면 예외를 발생시킨다.")
    @Test
    void changeNumberOfGuests_Exception_InvalidNumberOfGuests() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(null, 3, false));
        int invalidNumberOfGuests = -1;

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(
                        orderTable.getId(), new OrderTable(null, invalidNumberOfGuests, false)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님의 수는 0보다 작을 수 없습니다.");
    }

    @DisplayName("empty 상태인 OrderTable의 numberOfGuests 수를 변경하려고 하면 예외를 발생시킨다.")
    @Test
    void changeNumberOfGuests_Exception_EmptyOrderTable() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(null, 3, true));

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(
                        orderTable.getId(), new OrderTable(null, 10, true)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비어 있는 테이블의 손님 수를 변경할 수 없습니다.");
    }
}
