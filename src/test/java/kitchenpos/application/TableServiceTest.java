package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

class TableServiceTest extends ServiceTest {

    @Autowired
    protected TableService tableService;
    @Autowired
    protected OrderDao orderDao;
    @Autowired
    protected OrderTableDao orderTableDao;
    @Autowired
    protected TableGroupDao tableGroupDao;
    @Autowired
    protected TableGroupService tableGroupService;

    @Test
    @DisplayName("새 주문 테이블을 생성한다")
    void create() {
        // given
        int numberOfGuests = 999;
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);

        // when
        OrderTable createdOrderTable = tableService.create(orderTable);

        // then
        assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @Test
    @DisplayName("주문 테이블 목록을 조회한다")
    void list() {
        // given
        int default_data_size = 8;

        // when
        List<OrderTable> orderTables = tableService.list();
        OrderTable firstTable = orderTables.get(0);

        // then
        assertAll(
            () -> assertThat(orderTables).hasSize(default_data_size),
            () -> assertThat(firstTable.getId()).isEqualTo(1),
            () -> assertThat(firstTable.getNumberOfGuests()).isEqualTo(0),
            () -> assertThat(firstTable.isEmpty()).isTrue()
        );
    }

    @Test
    @DisplayName("테이블 점유 여부를 변경한다")
    void changeIsEmpty() {
        // given
        OrderTable createdOrderTable = createEmptyOrderTable();

        Order order = new Order();
        order.setOrderTableId(createdOrderTable.getId());
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        OrderTable targetOrderTable = new OrderTable();
        targetOrderTable.setEmpty(false);

        // when
        OrderTable changedOrderTable = tableService.changeEmpty(createdOrderTable.getId(), targetOrderTable);

        // then
        assertAll(
            () -> assertThat(changedOrderTable.getId()).isEqualTo(createdOrderTable.getId()),
            () -> assertThat(changedOrderTable.isEmpty()).isEqualTo(targetOrderTable.isEmpty())
        );
    }

    @Test
    @DisplayName("등록되지 않은 테이블의 점유 여부를 변경할 수 없다")
    void changeIsEmptyWithNonRegisteredOrderTable() {
        // given
        Long fakeOrderTableId = 999L;
        OrderTable orderTable = new OrderTable();

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(fakeOrderTableId, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹이 설정된 테이블의 점유 여부를 변경할 수 없다")
    void changeIsEmptyWithoutTableGroup() {
        // given
        OrderTable createdOrderTable = createEmptyOrderTable();
        OrderTable createdOrderTable2 = createEmptyOrderTable();

        // when
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(createdOrderTable, createdOrderTable2));
        tableGroupService.create(tableGroup);

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(createdOrderTable.getId(), createdOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("식사 완료 상태가 아닌 테이블의 점유 여부를 변경할 수 없다")
    void changeIsEmptyBeforeCompleted() {
        // given
        OrderTable createdOrderTable = createEmptyOrderTable();

        Order order = new Order();
        order.setOrderTableId(createdOrderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(createdOrderTable.getId(), createdOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블의 고객 수를 변경한다")
    void changeNumberOfGuests() {
        // given
        int expectedNumberOfGuests = 5;
        OrderTable targetOrderTable = new OrderTable();
        targetOrderTable.setNumberOfGuests(expectedNumberOfGuests);

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        OrderTable createdOrderTable = tableService.create(orderTable);

        // when
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(createdOrderTable.getId(), targetOrderTable);

        // then
        assertAll(
            () -> assertThat(changedOrderTable.getId()).isEqualTo(createdOrderTable.getId()),
            () -> assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(targetOrderTable.getNumberOfGuests())
        );
    }

    @Test
    @DisplayName("고객 수를 음수로 설정할 수 없다")
    void minusNumberOfGuests() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(-1);

        Long fakeOrderTableId = 999L;

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(fakeOrderTableId, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록되지 않은 테이블의 고객 수를 변경할 수 없다")
    void changeNumberOfGuestsWithNonRegisteredOrderTable() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(5);

        Long fakeOrderTableId = 999L;

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(fakeOrderTableId, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("점유되지 않은 테이블의 고객 수를 변경할 수 없다")
    void changeNumberOfGuestsOfEmptyTable() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(5);

        OrderTable createdOrderTable = createEmptyOrderTable();

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(createdOrderTable.getId(), orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable createEmptyOrderTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        return orderTableDao.save(orderTable);
    }
}
