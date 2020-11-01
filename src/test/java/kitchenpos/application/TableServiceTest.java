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
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.ui.dto.TableChangeEmptyRequest;
import kitchenpos.ui.dto.TableChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.TableCreateRequest;
import kitchenpos.ui.dto.TableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.dao.InvalidDataAccessApiUsageException;

class TableServiceTest extends KitchenPosServiceTest {

    @DisplayName("OrderTable 생성 - 성공")
    @Test
    void create_Success() {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(
            TEST_ORDER_TABLE_NUMBER_OF_GUESTS, TEST_ORDER_TABLE_EMPTY_FALSE);
        TableResponse savedTable = tableService.create(tableCreateRequest);

        assertThat(savedTable.getId()).isNotNull();
        assertThat(savedTable.getNumberOfGuests())
            .isEqualTo(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        assertThat(savedTable.isEmpty()).isEqualTo(TEST_ORDER_TABLE_EMPTY_FALSE);
        assertThat(savedTable.getTableGroupId()).isNull();
    }

    @DisplayName("전체 orderTable 조회 - 성공")
    @Test
    void list_Success() {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(
            TEST_ORDER_TABLE_NUMBER_OF_GUESTS, TEST_ORDER_TABLE_EMPTY_FALSE);
        TableResponse savedTable = tableService.create(tableCreateRequest);

        List<TableResponse> tables = tableService.list();

        assertThat(tables).isNotNull();
        assertThat(tables).isNotEmpty();
        assertThat(tables).contains(savedTable);
    }

    @DisplayName("Empty 상태 변경 - 성공")
    @Test
    void changeEmpty_Success() {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(
            TEST_ORDER_TABLE_NUMBER_OF_GUESTS, TEST_ORDER_TABLE_EMPTY_FALSE);
        TableResponse savedTable = tableService.create(tableCreateRequest);
        assertThat(savedTable.isEmpty()).isEqualTo(TEST_ORDER_TABLE_EMPTY_FALSE);
        Long savedOrderTableId = savedTable.getId();

        TableChangeEmptyRequest tableChangeEmptyRequest
            = new TableChangeEmptyRequest(TEST_ORDER_TABLE_EMPTY_TRUE);
        TableResponse changedTable
            = tableService.changeEmpty(savedOrderTableId, tableChangeEmptyRequest);
        assertThat(changedTable.isEmpty()).isEqualTo(TEST_ORDER_TABLE_EMPTY_TRUE);

        TableChangeEmptyRequest orderTableChangeEmptyRequest
            = new TableChangeEmptyRequest(TEST_ORDER_TABLE_EMPTY_FALSE);
        TableResponse changedOtherTable = tableService
            .changeEmpty(savedOrderTableId, orderTableChangeEmptyRequest);
        assertThat(changedOtherTable.isEmpty()).isEqualTo(TEST_ORDER_TABLE_EMPTY_FALSE);
    }

    @DisplayName("Empty 상태 변경 - 예외 발생, ID가 존재하지 않는 경우")
    @Test
    void changeEmpty_NotExistsId_ThrownException() {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(
            TEST_ORDER_TABLE_NUMBER_OF_GUESTS, TEST_ORDER_TABLE_EMPTY_FALSE);
        TableResponse savedTable = tableService.create(tableCreateRequest);
        assertThat(savedTable.isEmpty()).isEqualTo(TEST_ORDER_TABLE_EMPTY_FALSE);
        Long wrongOrderTableId = savedTable.getId() + 1;

        TableChangeEmptyRequest tableChangeEmptyRequest
            = new TableChangeEmptyRequest(TEST_ORDER_TABLE_EMPTY_TRUE);
        assertThatThrownBy(
            () -> tableService.changeEmpty(wrongOrderTableId, tableChangeEmptyRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Empty 상태 변경 - 예외 발생, ID가 Null인 경우")
    @Test
    void changeEmpty_NullId_ThrownException() {
        Long wrongOrderTableId = null;

        TableChangeEmptyRequest tableChangeEmptyRequest
            = new TableChangeEmptyRequest(TEST_ORDER_TABLE_EMPTY_TRUE);
        assertThatThrownBy(
            () -> tableService.changeEmpty(wrongOrderTableId, tableChangeEmptyRequest))
            .isInstanceOf(InvalidDataAccessApiUsageException.class);
    }

    @DisplayName("Empty 상태 변경 - 예외 발생, TableGroup이 세팅된 경우")
    @Test
    void changeEmpty_SetTableGroup_ThrownException() {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(
            TEST_ORDER_TABLE_NUMBER_OF_GUESTS, TEST_ORDER_TABLE_EMPTY_TRUE);
        TableResponse savedTable = tableService.create(tableCreateRequest);

        TableCreateRequest orderTableCreateRequest = new TableCreateRequest(
            TEST_ORDER_TABLE_NUMBER_OF_GUESTS, TEST_ORDER_TABLE_EMPTY_TRUE);
        TableResponse savedOtherTable = tableService.create(orderTableCreateRequest);
        setCreatedTableGroup(Arrays.asList(savedTable.getId(), savedOtherTable.getId()));

        Long savedOrderTableId = savedTable.getId();

        TableChangeEmptyRequest tableChangeEmptyRequest
            = new TableChangeEmptyRequest(TEST_ORDER_TABLE_EMPTY_FALSE);
        assertThatThrownBy(
            () -> tableService.changeEmpty(savedOrderTableId, tableChangeEmptyRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Empty 상태 변경 - 성공, Order 상태가 Cooking/Meal이 아닌 경우")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"}, mode = Mode.EXCLUDE)
    void changeEmpty_OrderNotCookingOrMeal_ThrownException(OrderStatus orderStatus) {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(
            TEST_ORDER_TABLE_NUMBER_OF_GUESTS, TEST_ORDER_TABLE_EMPTY_FALSE);
        TableResponse savedTable = tableService.create(tableCreateRequest);
        assertThat(savedTable.isEmpty()).isEqualTo(TEST_ORDER_TABLE_EMPTY_FALSE);
        Long savedOrderTableId = savedTable.getId();

        Order order = new Order();
        order.setOrderStatus(orderStatus.name());
        order.setOrderTableId(savedTable.getId());
        order.setOrderedTime(LocalDateTime.now());
        orderRepository.save(order);

        TableChangeEmptyRequest tableChangeEmptyRequest
            = new TableChangeEmptyRequest(TEST_ORDER_TABLE_EMPTY_TRUE);
        TableResponse changedTable = tableService
            .changeEmpty(savedOrderTableId, tableChangeEmptyRequest);
        assertThat(changedTable.isEmpty()).isEqualTo(tableChangeEmptyRequest.isEmpty());
    }

    @DisplayName("Empty 상태 변경 - 예외 발생, Order 상태가 Cooking/Meal인 경우")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void changeEmpty_OrderCookingOrMeal_ThrownException(OrderStatus orderStatus) {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(
            TEST_ORDER_TABLE_NUMBER_OF_GUESTS, TEST_ORDER_TABLE_EMPTY_FALSE);
        TableResponse savedTable = tableService.create(tableCreateRequest);
        assertThat(savedTable.isEmpty()).isEqualTo(TEST_ORDER_TABLE_EMPTY_FALSE);
        Long savedOrderTableId = savedTable.getId();

        Order order = new Order();
        order.setOrderStatus(orderStatus.name());
        order.setOrderTableId(savedTable.getId());
        order.setOrderedTime(LocalDateTime.now());
        orderRepository.save(order);

        TableChangeEmptyRequest tableChangeEmptyRequest
            = new TableChangeEmptyRequest(TEST_ORDER_TABLE_EMPTY_TRUE);
        assertThatThrownBy(
            () -> tableService.changeEmpty(savedOrderTableId, tableChangeEmptyRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수 변경 - 성공")
    @Test
    void changeNumberOfGuests_Success() {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(
            TEST_ORDER_TABLE_NUMBER_OF_GUESTS, TEST_ORDER_TABLE_EMPTY_FALSE);
        TableResponse savedTable = tableService.create(tableCreateRequest);

        TableChangeNumberOfGuestsRequest tableChangeNumberOfGuestsRequest
            = new TableChangeNumberOfGuestsRequest(20);
        TableResponse changedTable = tableService
            .changeNumberOfGuests(savedTable.getId(), tableChangeNumberOfGuestsRequest);

        assertThat(changedTable.getId()).isEqualTo(savedTable.getId());
        assertThat(changedTable.isEmpty()).isEqualTo(savedTable.isEmpty());
        assertThat(changedTable.getNumberOfGuests())
            .isEqualTo(tableChangeNumberOfGuestsRequest.getNumberOfGuests());
        assertThat(changedTable.getTableGroupId())
            .isEqualTo(savedTable.getTableGroupId());
    }

    @DisplayName("손님 수 변경 - 예외 발생, 테이블이 비어있는 경우")
    @Test
    void changeNumberOfGuests_EmptyTable_ThrownException() {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(
            TEST_ORDER_TABLE_NUMBER_OF_GUESTS_EMPTY, TEST_ORDER_TABLE_EMPTY_TRUE);
        TableResponse savedTable = tableService.create(tableCreateRequest);

        TableChangeNumberOfGuestsRequest tableChangeNumberOfGuestsRequest
            = new TableChangeNumberOfGuestsRequest(20);
        assertThatThrownBy(() -> tableService
            .changeNumberOfGuests(savedTable.getId(), tableChangeNumberOfGuestsRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수 변경 - 예외 발생, 손님 수가 적정하지 않을 경우")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void changeNumberOfGuests_WrongNumberOfGuests_ThrownException(int numberOfGuests) {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(
            TEST_ORDER_TABLE_NUMBER_OF_GUESTS_EMPTY, TEST_ORDER_TABLE_EMPTY_TRUE
        );
        TableResponse savedTable = tableService.create(tableCreateRequest);

        TableChangeNumberOfGuestsRequest tableChangeNumberOfGuestsRequest
            = new TableChangeNumberOfGuestsRequest(numberOfGuests);

        assertThatThrownBy(() -> tableService
            .changeNumberOfGuests(savedTable.getId(), tableChangeNumberOfGuestsRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
