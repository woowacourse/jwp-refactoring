package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.table.OrderTableFindAllResponses;
import kitchenpos.dto.table.OrderTableUpdateEmptyRequest;
import kitchenpos.dto.table.OrderTableUpdateEmptyResponse;
import kitchenpos.dto.table.OrderTableUpdateNumberOfGuestsRequest;
import kitchenpos.dto.table.OrderTableUpdateNumberOfGuestsResponse;
import kitchenpos.dto.tableGroup.OrderTableCreateRequest;
import kitchenpos.dto.tableGroup.OrderTableCreateRequests;
import kitchenpos.dto.tableGroup.OrderTableCreateResponse;
import kitchenpos.dto.tableGroup.TableGroupCreateRequest;

class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupService tableGroupService;

    @Test
    void create() {
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(null, null, 1, false);

        OrderTableCreateResponse actual = tableService.create(orderTableCreateRequest);

        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getEmpty()).isEqualTo(false),
            () -> assertThat(actual.getTableGroupId()).isNull(),
            () -> assertThat(actual.getNumberOfGuests()).isEqualTo(orderTableCreateRequest.getNumberOfGuests())
        );
    }

    @Test
    void list() {
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(null, null, 1, false);

        OrderTableCreateResponse expect = tableService.create(orderTableCreateRequest);

        OrderTableFindAllResponses actual = tableService.findAll();

        assertThat(actual.getOrderTableFindAllResponses()).hasSize(1);
        assertThat(actual.getOrderTableFindAllResponses().get(0)).usingRecursiveComparison()
            .isEqualTo(expect);
    }

    @DisplayName("존재하지 않는 OrderTable의 empty 상태를 수정할 때 IllegalArgumentException이 발생한다.")
    @Test
    void changeEmpty_whenOrderTableIsNotExist_thenThrowIllegalArgumentException() {
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTableUpdateEmptyRequest(true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정된 주문 테이블 수정할 때 IllegalArgumentException이 발생한다.")
    @Test
    void changeEmpty_whenOrderTableIsSetTableGroup_thenThrowIllegalArgumentException() {
        OrderTable orderTable1 = createOrderTable(null, true, null, 0);
        OrderTable orderTable2 = createOrderTable(null, true, null, 0);

        OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
        OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);

        OrderTableCreateRequest orderTableCreateRequest1 = new OrderTableCreateRequest(savedOrderTable1);
        OrderTableCreateRequest orderTableCreateRequest2 = new OrderTableCreateRequest(savedOrderTable2);

        OrderTableCreateRequests orderTableCreateRequests = new OrderTableCreateRequests(new ArrayList<>(
            Arrays.asList(orderTableCreateRequest1, orderTableCreateRequest2)));

        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(null,
            LocalDateTime.of(2020, 10, 28, 17, 1), orderTableCreateRequests);

        tableGroupService.create(tableGroupCreateRequest);

        assertThatThrownBy(
            () -> tableService.changeEmpty(savedOrderTable1.getId(), new OrderTableUpdateEmptyRequest(false)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("empty 상태 변경 성공")
    @Test
    void changeEmpty() {
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(null, null, 1, false);

        OrderTableCreateResponse savedOrderTable = tableService.create(orderTableCreateRequest);
        OrderTableUpdateEmptyRequest orderTableUpdateEmptyRequest = new OrderTableUpdateEmptyRequest(true);
        OrderTable changingOrderTable = createOrderTable(savedOrderTable.getId(), true, null, 1);

        OrderTableUpdateEmptyResponse actual = tableService.changeEmpty(savedOrderTable.getId(),
            orderTableUpdateEmptyRequest);

        assertThat(actual.getEmpty()).isEqualTo(changingOrderTable.isEmpty());
    }

    @DisplayName("손님의 수가 음수일 때 IllegalArgumentException 발생")
    @Test
    void changeNumberOfGuests_whenNumberOfGuestIsMinus_thenThrowIllegalArgumentException() {
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(null, null, 1, false);

        OrderTableCreateResponse savedOrderTable = tableService.create(orderTableCreateRequest);
        OrderTableUpdateNumberOfGuestsRequest orderTableUpdateNumberOfGuestsRequest = new OrderTableUpdateNumberOfGuestsRequest(
            -1);

        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTableUpdateNumberOfGuestsRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 OrderTable의 numberOfGusets를 수정할 때 IllegalArgumentException 발생")
    @Test
    void changeNumberOfGuests_whenOrderTableIsNotExist_thenThrowIllegalArgumentException() {
        OrderTableUpdateNumberOfGuestsRequest orderTableUpdateNumberOfGuestsRequest = new OrderTableUpdateNumberOfGuestsRequest(
            2);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTableUpdateNumberOfGuestsRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있는 OrderTable을 수정할 때 IllegalArgumentException 발생")
    @Test
    void changeNumberOfGuests_whenOrderTableIsEmpty_thenThrowIllegalArgumentException() {
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(null, null, 0, true);

        OrderTableCreateResponse savedOrderTable = tableService.create(orderTableCreateRequest);
        OrderTableUpdateNumberOfGuestsRequest orderTableUpdateNumberOfGuestsRequest = new OrderTableUpdateNumberOfGuestsRequest(
            -1);

        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTableUpdateNumberOfGuestsRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderTable numberOfGuest 상태 변경 성공")
    @Test
    void changeNumberOfGuests() {
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(null, null, 2, false);

        OrderTableCreateResponse savedOrderTable = tableService.create(orderTableCreateRequest);
        OrderTableUpdateNumberOfGuestsRequest orderTableUpdateNumberOfGuestsRequest = new OrderTableUpdateNumberOfGuestsRequest(
            4);

        OrderTableUpdateNumberOfGuestsResponse actual = tableService.changeNumberOfGuests(
            savedOrderTable.getId(), orderTableUpdateNumberOfGuestsRequest);

        assertThat(actual.getNumberOfGuests()).isEqualTo(orderTableUpdateNumberOfGuestsRequest.getNumberOfGuests());
    }
}