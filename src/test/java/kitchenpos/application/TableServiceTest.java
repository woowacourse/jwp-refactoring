package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("주문 테이블과 테이블 그룹의 id를 null로 초기화 한다.")
    @Test
    void createSuccessTest_InitializeOrderTableAndTableGroupId() {
        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setId(99L);
        orderTable.setTableGroupId(99L);

        //when
        assertThat(orderTableDao.findById(99L)).isEmpty();
        assertThat(orderTableDao.findAllByTableGroupId(99L)).isEmpty();

        OrderTable savedOrderTable = tableService.create(orderTable);

        //then
        OrderTable findOrderTable = tableService.list()
                .stream()
                .filter(table -> table.getId().equals(savedOrderTable.getId()))
                .findAny()
                .get();

        assertThat(findOrderTable.getTableGroupId()).isNull();
        assertThat(findOrderTable.getId()).isNotEqualTo(99L);
    }

    @DisplayName("기존에 주문이 없었던 테이블인 경우, 주문 상태를 변경할 수 없다.")
    @Test
    void changeEmptyFailTest_ByOrderTableIsNotExists() {
        //given
        OrderTable orderTable = new OrderTable();

        //when
        assertThat(orderTableDao.findById(99L)).isEmpty();

        //then
        assertThatThrownBy(() -> tableService.changeEmpty(99L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹화 되어있는 테이블이 존재하는 경우, 주문 상태를 변경할 수 없다.")
    @Test
    void changeEmptyFailTest_ByAlreadyGroupedOtherTable() {
        //given
        OrderTable orderTable = new OrderTable();
        OrderTable otherOrderTable = new OrderTable();

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(orderTable, otherOrderTable));
        tableGroup.setCreatedDate(LocalDateTime.now());

        //when
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        orderTable.setTableGroupId(savedTableGroup.getId());
        Long savedOrderTableId = orderTableDao.save(orderTable).getId();

        //then
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTableId, otherOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @ParameterizedTest(name = "완료(COMPLETION)되지 않은 상태(COOKING, MEAL)의 주문이 있는 경우, 변경할 수 없다.")
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeEmptyFailTest_ByOrderStatusIsNotCompletion(String orderStatus) {
        //given
        OrderTable orderTable = new OrderTable();
        Long savedOrderTableId = orderTableDao.save(orderTable).getId();

        Order order = new Order();
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(orderStatus);
        order.setOrderTableId(savedOrderTableId);
        orderDao.save(order);

        //when
        assertThat(order.getOrderStatus()).isNotEqualTo("COMPLETION");

        //then
        OrderTable otherOrderTable = new OrderTable();

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTableId, otherOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 주문 가능 상태(Empty)를 변경할 수 있다.")
    @Test
    void changeEmptySuccessTest() {
        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        Long savedOrderTableId = orderTableDao.save(orderTable).getId();

        Order order = new Order();
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus("COMPLETION");
        order.setOrderTableId(savedOrderTableId);
        orderDao.save(order);

        //when
        OrderTable findOrderTable = tableService.list()
                .stream()
                .filter(table -> table.getId().equals(savedOrderTableId))
                .findAny()
                .get();
        assertThat(findOrderTable.isEmpty()).isTrue();

        OrderTable otherOrderTable = new OrderTable();
        otherOrderTable.setEmpty(false);

        OrderTable changedEmptyOrderTable = tableService.changeEmpty(savedOrderTableId, otherOrderTable);

        //then
        assertThat(changedEmptyOrderTable.isEmpty()).isFalse();
    }

    @ParameterizedTest(name = "방문한 손님 수가 0명 미만이면, 테이블에 방문한 손님 수를 변경할 수 없다.")
    @ValueSource(ints = {-100, -1})
    void changeNumberOfGuestsFailTest_ByNumberOfGuestsIsLessThanZero(int numberOfGuests) {
        //given
        OrderTable orderTable = new OrderTable();
        Long savedOrderTableId = orderTableDao.save(orderTable).getId();

        //when
        OrderTable otherOrderTable = new OrderTable();
        otherOrderTable.setNumberOfGuests(numberOfGuests);

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTableId, otherOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("변경하려고 하는 테이블이 존재하지 않으면, 테이블에 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsFailTest_ByOrderTableIsNotExists() {
        //given
        OrderTable orderTable = new OrderTable();

        //when
        assertThat(orderTableDao.findById(99L)).isEmpty();

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(99L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문할 수 없는 테이블(Empty)이면, 테이블에 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsFailTest_ByOrderTableIsEmpty() {
        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);

        Long savedOrderTableId = orderTableDao.save(orderTable).getId();

        //when
        OrderTable otherOrderTable = new OrderTable();
        otherOrderTable.setNumberOfGuests(99);

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTableId, otherOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "테이블에 방문한 손님 수를 변경할 수 있다.")
    @ValueSource(ints = {1, 1000})
    void changeNumberOfGuestsSuccessTest(int numberOfGuests) {
        //given
        OrderTable orderTable = new OrderTable();

        OrderTable savedOrderTable = tableService.create(orderTable);

        //when
        assertThat(savedOrderTable.getNumberOfGuests()).isZero();

        OrderTable otherOrderTable = new OrderTable();
        otherOrderTable.setNumberOfGuests(numberOfGuests);

        tableService.changeNumberOfGuests(savedOrderTable.getId(), otherOrderTable);

        //then
        OrderTable findOrderTable = tableService.list()
                .stream()
                .filter(table -> table.getId().equals(savedOrderTable.getId()))
                .findAny()
                .get();

        assertThat(findOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

}
