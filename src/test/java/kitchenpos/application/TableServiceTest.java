package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.dto.table.OrderTableChangeEmptyRequest;
import kitchenpos.dto.table.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.dto.table.OrderTableRequest;
import kitchenpos.dto.table.OrderTableResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;

@Import(TableService.class)
class TableServiceTest extends ServiceTest {

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableService tableService;

    @DisplayName("create: 주문 테이블 생성")
    @Test
    void create() {
        final OrderTableRequest orderTableRequest = new OrderTableRequest(0, true);
        final OrderTableResponse actual = tableService.create(orderTableRequest);

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getTableGroupId()).isNull(),
                () -> assertThat(actual.getNumberOfGuests()).isZero(),
                () -> assertThat(actual.isEmpty()).isTrue()
        );
    }

    @DisplayName("list: 주문 테이블 전체 조회")
    @Test
    void list() {
        tableService.create(new OrderTableRequest(0, true));
        tableService.create(new OrderTableRequest(0, true));

        final List<OrderTableResponse> actual = tableService.list();

        assertThat(actual).hasSize(2);
    }

    @DisplayName("changeEmpty: Empty 수정")
    @Test
    void changeEmpty() {
        final OrderTableResponse orderTableResponse = tableService.create(new OrderTableRequest(0, true));
        final OrderTableChangeEmptyRequest inputOrderTableRequest = new OrderTableChangeEmptyRequest(false);
        final OrderTableResponse actual = tableService.changeEmpty(orderTableResponse.getId(), inputOrderTableRequest);

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.isEmpty()).isFalse()
        );
    }

    @DisplayName("changeEmpty: 주문 테이블이 없을 때 예외 처리")
    @Test
    void changeEmpty_IfOrderTableDoesNotExist_Exception() {
        final OrderTableChangeEmptyRequest orderTableChangeEmptyRequest = new OrderTableChangeEmptyRequest(false);

        assertThatThrownBy(() -> tableService.changeEmpty(0L, orderTableChangeEmptyRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("changeEmpty: 주문 테이블이 테이블 그룹에 속해있을 때 예외 처리")
    @Test
    void changeEmpty_IfOrderTableHasTableGroup_Exception() {
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(0, true));
        orderTable.updateTableGroup(tableGroup);
        orderTableRepository.save(orderTable);
        final OrderTableChangeEmptyRequest orderTableChangeEmptyRequest = new OrderTableChangeEmptyRequest(false);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTableChangeEmptyRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("changeEmpty: 주문이 완료되지 않았을 때 예외 처리")
    @Test
    void changeEmpty_IfOrderStatusIsNotCompletion_Exception() {
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(0, true));
        orderRepository.save(new Order(orderTable, "MEAL", LocalDateTime.now()));
        final OrderTableChangeEmptyRequest orderTableChangeEmptyRequest = new OrderTableChangeEmptyRequest(false);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTableChangeEmptyRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("changeNumberOfGuests: 방문한 손님 수 변경")
    @Test
    void changeNumberOfGuests() {
        final OrderTableResponse orderTableResponse = tableService.create(new OrderTableRequest(0, false));
        final OrderTableChangeNumberOfGuestsRequest orderTableChangeNumberOfGuestsRequest = new OrderTableChangeNumberOfGuestsRequest(2);

        final OrderTableResponse actual = tableService.changeNumberOfGuests(orderTableResponse.getId(), orderTableChangeNumberOfGuestsRequest);

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(2)
        );

    }

    @DisplayName("changeNumberOfGuests: 주문 테이블이 존재하지 않을 때 예외 처리")
    @Test
    void changeNumberOfGuests_IfOrderTableDoesNotExist_Exception() {
        final OrderTableChangeNumberOfGuestsRequest orderTableChangeNumberOfGuestsRequest = new OrderTableChangeNumberOfGuestsRequest(2);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, orderTableChangeNumberOfGuestsRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("changeNumberOfGuests: 주문 테이블의 empty가 true일 때 예외 처리")
    @Test
    void changeNumberOfGuests_IfOrderTableIsEmpty_Exception() {
        final OrderTableResponse orderTableResponse = tableService.create(new OrderTableRequest(0, true));
        final OrderTableChangeNumberOfGuestsRequest orderTableChangeNumberOfGuestsRequest = new OrderTableChangeNumberOfGuestsRequest(2);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableResponse.getId(), orderTableChangeNumberOfGuestsRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}