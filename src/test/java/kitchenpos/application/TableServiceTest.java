package kitchenpos.application;

import static kitchenpos.constants.Constants.TEST_ORDER_TABLE_EMPTY_FALSE;
import static kitchenpos.constants.Constants.TEST_ORDER_TABLE_EMPTY_TRUE;
import static kitchenpos.constants.Constants.TEST_ORDER_TABLE_NUMBER_OF_GUESTS;
import static kitchenpos.constants.Constants.TEST_ORDER_TABLE_NUMBER_OF_GUESTS_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.junit.jupiter.params.provider.ValueSource;

class TableServiceTest extends KitchenPosServiceTest {

    @DisplayName("OrderTable 생성 - 성공")
    @Test
    void create_Success() {
        OrderTable orderTable = new OrderTable();

        orderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        orderTable.setEmpty(TEST_ORDER_TABLE_EMPTY_FALSE);

        OrderTable savedOrderTable = tableService.create(orderTable);

        assertThat(savedOrderTable.getId()).isNotNull();
        assertThat(savedOrderTable.getNumberOfGuests())
            .isEqualTo(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        assertThat(savedOrderTable.isEmpty()).isEqualTo(TEST_ORDER_TABLE_EMPTY_FALSE);
        assertThat(savedOrderTable.getTableGroupId()).isNull();
    }

    @DisplayName("OrderTable 생성 - 성공, ID와 TableGroupID가 있는 경우 제거")
    @Test
    void create_ExistsIdAndTableGroupId_RemoveThatAndSuccess() {
        OrderTable orderTable = new OrderTable();

        long removedId = -1L;
        orderTable.setId(removedId);
        orderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        orderTable.setEmpty(TEST_ORDER_TABLE_EMPTY_FALSE);
        orderTable.setTableGroupId(removedId);

        OrderTable savedOrderTable = tableService.create(orderTable);

        assertThat(savedOrderTable.getId()).isNotNull();
        assertThat(savedOrderTable.getId()).isNotEqualTo(removedId);
        assertThat(savedOrderTable.getNumberOfGuests())
            .isEqualTo(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        assertThat(savedOrderTable.isEmpty()).isEqualTo(TEST_ORDER_TABLE_EMPTY_FALSE);
        assertThat(savedOrderTable.getTableGroupId()).isNull();
    }

    @DisplayName("전체 orderTable 조회 - 성공")
    @Test
    void list_Succes() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        orderTable.setEmpty(TEST_ORDER_TABLE_EMPTY_FALSE);
        OrderTable savedOrderTable = tableService.create(orderTable);

        List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).isNotNull();
        assertThat(orderTables).isNotEmpty();

        List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        assertThat(orderTableIds).contains(savedOrderTable.getId());
    }

    @DisplayName("Empty 상태 변경 - 성공")
    @Test
    void changeEmpty_Success() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        orderTable.setEmpty(TEST_ORDER_TABLE_EMPTY_FALSE);
        OrderTable savedOrderTable = tableService.create(orderTable);
        assertThat(savedOrderTable.isEmpty()).isEqualTo(TEST_ORDER_TABLE_EMPTY_FALSE);
        Long savedOrderTableId = savedOrderTable.getId();

        OrderTable orderTableOnlyEmpty = new OrderTable();
        orderTableOnlyEmpty.setEmpty(TEST_ORDER_TABLE_EMPTY_TRUE);
        OrderTable changedOrderTable
            = tableService.changeEmpty(savedOrderTableId, orderTableOnlyEmpty);
        assertThat(changedOrderTable.isEmpty()).isEqualTo(TEST_ORDER_TABLE_EMPTY_TRUE);

        OrderTable otherOrderTableOnlyEmpty = new OrderTable();
        otherOrderTableOnlyEmpty.setEmpty(TEST_ORDER_TABLE_EMPTY_FALSE);
        OrderTable changedOtherOrderTable
            = tableService.changeEmpty(savedOrderTableId, otherOrderTableOnlyEmpty);
        assertThat(changedOtherOrderTable.isEmpty()).isEqualTo(TEST_ORDER_TABLE_EMPTY_FALSE);
    }

    @DisplayName("Empty 상태 변경 - 예외 발생, ID가 존재하지 않는 경우")
    @Test
    void changeEmpty_NotExistsId_ThrownException() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        orderTable.setEmpty(TEST_ORDER_TABLE_EMPTY_FALSE);
        OrderTable savedOrderTable = tableService.create(orderTable);
        assertThat(savedOrderTable.isEmpty()).isEqualTo(TEST_ORDER_TABLE_EMPTY_FALSE);
        Long wrongOrderTableId = savedOrderTable.getId() + 1;

        OrderTable orderTableOnlyEmpty = new OrderTable();
        orderTableOnlyEmpty.setEmpty(TEST_ORDER_TABLE_EMPTY_TRUE);
        assertThatThrownBy(() -> tableService.changeEmpty(wrongOrderTableId, orderTableOnlyEmpty))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Empty 상태 변경 - 예외 발생, ID가 Null인 경우")
    @Test
    void changeEmpty_NullId_ThrownException() {
        Long wrongOrderTableId = null;

        OrderTable orderTableOnlyEmpty = new OrderTable();
        orderTableOnlyEmpty.setEmpty(TEST_ORDER_TABLE_EMPTY_TRUE);
        assertThatThrownBy(() -> tableService.changeEmpty(wrongOrderTableId, orderTableOnlyEmpty))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Empty 상태 변경 - 예외 발생, TableGroup이 세팅된 경우")
    @Test
    void changeEmpty_SetTableGroup_ThrownException() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        orderTable.setEmpty(TEST_ORDER_TABLE_EMPTY_TRUE);
        OrderTable savedOrderTable = tableService.create(orderTable);

        OrderTable otherOrderTable = new OrderTable();
        otherOrderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        otherOrderTable.setEmpty(TEST_ORDER_TABLE_EMPTY_TRUE);
        OrderTable savedOtherOrderTable = tableService.create(otherOrderTable);
        setCreatedTableGroup(Arrays.asList(savedOrderTable, savedOtherOrderTable));

        Long savedOrderTableId = savedOrderTable.getId();

        OrderTable orderTableOnlyEmpty = new OrderTable();
        orderTableOnlyEmpty.setEmpty(TEST_ORDER_TABLE_EMPTY_FALSE);
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTableId, orderTableOnlyEmpty))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Empty 상태 변경 - 성공, Order 상태가 Cooking/Meal이 아닌 경우")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"}, mode = Mode.EXCLUDE)
    void changeEmpty_OrderNotCookingOrMeal_ThrownException(OrderStatus orderStatus) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        orderTable.setEmpty(TEST_ORDER_TABLE_EMPTY_FALSE);
        OrderTable savedOrderTable = tableService.create(orderTable);
        assertThat(savedOrderTable.isEmpty()).isEqualTo(TEST_ORDER_TABLE_EMPTY_FALSE);
        Long savedOrderTableId = savedOrderTable.getId();

        Order order = new Order();
        order.setOrderStatus(orderStatus.name());
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        OrderTable orderTableOnlyEmpty = new OrderTable();
        orderTableOnlyEmpty.setEmpty(TEST_ORDER_TABLE_EMPTY_TRUE);
        OrderTable changedOrderTable
            = tableService.changeEmpty(savedOrderTableId, orderTableOnlyEmpty);
        assertThat(changedOrderTable.isEmpty()).isEqualTo(orderTableOnlyEmpty.isEmpty());
    }

    @DisplayName("Empty 상태 변경 - 예외 발생, Order 상태가 Cooking/Meal인 경우")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void changeEmpty_OrderCookingOrMeal_ThrownException(OrderStatus orderStatus) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        orderTable.setEmpty(TEST_ORDER_TABLE_EMPTY_FALSE);
        OrderTable savedOrderTable = tableService.create(orderTable);
        assertThat(savedOrderTable.isEmpty()).isEqualTo(TEST_ORDER_TABLE_EMPTY_FALSE);
        Long savedOrderTableId = savedOrderTable.getId();

        Order order = new Order();
        order.setOrderStatus(orderStatus.name());
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        OrderTable orderTableOnlyEmpty = new OrderTable();
        orderTableOnlyEmpty.setEmpty(TEST_ORDER_TABLE_EMPTY_TRUE);
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTableId, orderTableOnlyEmpty))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수 변경 - 성공")
    @Test
    void changeNumberOfGuests_Success() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        orderTable.setEmpty(TEST_ORDER_TABLE_EMPTY_FALSE);

        OrderTable savedOrderTable = tableService.create(orderTable);

        OrderTable orderTableOnlyNumberOfGuests = new OrderTable();
        orderTableOnlyNumberOfGuests.setNumberOfGuests(20);
        OrderTable changedOrderTable = tableService
            .changeNumberOfGuests(savedOrderTable.getId(), orderTableOnlyNumberOfGuests);

        assertThat(changedOrderTable.getId()).isEqualTo(savedOrderTable.getId());
        assertThat(changedOrderTable.isEmpty()).isEqualTo(savedOrderTable.isEmpty());
        assertThat(changedOrderTable.getNumberOfGuests())
            .isEqualTo(orderTableOnlyNumberOfGuests.getNumberOfGuests());
        assertThat(changedOrderTable.getTableGroupId())
            .isEqualTo(savedOrderTable.getTableGroupId());
    }

    @DisplayName("손님 수 변경 - 예외 발생, 테이블이 비어있는 경우")
    @Test
    void changeNumberOfGuests_EmptyTable_ThrownException() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS_EMPTY);
        orderTable.setEmpty(TEST_ORDER_TABLE_EMPTY_TRUE);

        OrderTable savedOrderTable = tableService.create(orderTable);

        OrderTable orderTableOnlyNumberOfGuests = new OrderTable();
        orderTableOnlyNumberOfGuests.setNumberOfGuests(20);
        assertThatThrownBy(() -> tableService
            .changeNumberOfGuests(savedOrderTable.getId(), orderTableOnlyNumberOfGuests))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수 변경 - 예외 발생, 손님 수가 적정하지 않을 경우")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void changeNumberOfGuests_WrongNumberOfGuests_ThrownException(int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS_EMPTY);
        orderTable.setEmpty(TEST_ORDER_TABLE_EMPTY_TRUE);

        OrderTable savedOrderTable = tableService.create(orderTable);

        OrderTable orderTableOnlyNumberOfGuests = new OrderTable();
        orderTableOnlyNumberOfGuests.setNumberOfGuests(numberOfGuests);

        assertThatThrownBy(() -> tableService
            .changeNumberOfGuests(savedOrderTable.getId(), orderTableOnlyNumberOfGuests))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
