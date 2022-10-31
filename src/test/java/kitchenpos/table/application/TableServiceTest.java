package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.ServiceTest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.request.OrderTableCreateRequest;
import kitchenpos.table.dto.request.OrderTableEmptyUpdateRequest;
import kitchenpos.table.dto.request.OrderTableNumberOfGuestsUpdateRequest;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;

class TableServiceTest extends ServiceTest {

    @Autowired
    protected TableService tableService;
    @Autowired
    protected OrderRepository orderRepository;
    @Autowired
    protected OrderTableRepository orderTableRepository;
    @Autowired
    protected TableGroupRepository tableGroupRepository;

    @Test
    @DisplayName("새 주문 테이블을 생성한다")
    void create() {
        // given
        int numberOfGuests = 999;
        OrderTableCreateRequest createRequest = new OrderTableCreateRequest(numberOfGuests, true);

        // when
        OrderTable createdOrderTable = tableService.create(createRequest);

        // then
        assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @Test
    @DisplayName("주문 테이블 목록을 조회한다")
    void list() {
        // given

        // when
        List<OrderTable> orderTables = tableService.list();
        OrderTable firstTable = orderTables.get(0);

        // then
        assertAll(
            () -> assertThat(orderTables).hasSameSizeAs(orderTableRepository.findAll()),
            () -> assertThat(firstTable.getId()).isOne(),
            () -> assertThat(firstTable.getNumberOfGuests()).isZero(),
            () -> assertThat(firstTable.isEmpty()).isTrue()
        );
    }

    @Test
    @DisplayName("테이블 점유 여부를 변경한다")
    void changeIsEmpty() {
        // given
        OrderTable createdOrderTable = createEmptyOrderTable();

        // when
        OrderTableEmptyUpdateRequest updateRequest = new OrderTableEmptyUpdateRequest(false);
        OrderTable changedOrderTable = tableService.changeEmpty(createdOrderTable.getId(), updateRequest);

        // then
        assertAll(
            () -> assertThat(changedOrderTable.getId()).isEqualTo(createdOrderTable.getId()),
            () -> assertThat(changedOrderTable.isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("등록되지 않은 테이블의 점유 여부를 변경할 수 없다")
    void changeIsEmptyWithNonRegisteredOrderTable() {
        // given
        Long fakeOrderTableId = 999L;

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(fakeOrderTableId, new OrderTableEmptyUpdateRequest(false)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹이 설정된 테이블의 점유 여부를 변경할 수 없다")
    void changeIsEmptyWithoutTableGroup() {
        // given
        OrderTable createdOrderTable = createEmptyOrderTable();
        createdOrderTable.setEmpty(false);
        OrderTable createdOrderTable2 = createEmptyOrderTable();
        createdOrderTable2.setEmpty(false);

        // when
        TableGroup tableGroup = new TableGroup(List.of(createdOrderTable, createdOrderTable2));
        tableGroupRepository.save(tableGroup);

        // then
        assertThatThrownBy(
            () -> tableService.changeEmpty(createdOrderTable.getId(), new OrderTableEmptyUpdateRequest(false)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("식사 완료 상태가 아닌 테이블의 점유 여부를 변경할 수 없다")
    void changeIsEmptyBeforeCompleted() {
        // given
        OrderTable createdOrderTable = createEmptyOrderTable();
        createdOrderTable.setEmpty(false);

        Order order = new Order(createdOrderTable, List.of());
        order.updateStatus(OrderStatus.COOKING);
        orderRepository.save(order);

        // when, then
        assertThatThrownBy(
            () -> tableService.changeEmpty(createdOrderTable.getId(), new OrderTableEmptyUpdateRequest(true)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블의 고객 수를 변경한다")
    void changeNumberOfGuests() {
        // given
        OrderTable orderTable = createEmptyOrderTable();
        orderTable.setEmpty(false);
        OrderTable createdOrderTable = orderTableRepository.save(orderTable);

        // when
        int expectedNumberOfGuests = 5;
        OrderTableNumberOfGuestsUpdateRequest updateRequest = new OrderTableNumberOfGuestsUpdateRequest(
            expectedNumberOfGuests);
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(createdOrderTable.getId(), updateRequest);

        // then
        assertAll(
            () -> assertThat(changedOrderTable.getId()).isEqualTo(createdOrderTable.getId()),
            () -> assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(expectedNumberOfGuests)
        );
    }

    @Test
    @DisplayName("고객 수를 음수로 설정할 수 없다")
    void minusNumberOfGuests() {
        // given
        OrderTable orderTable = createEmptyOrderTable();
        orderTable.setEmpty(false);
        OrderTable createdOrderTable = orderTableRepository.save(orderTable);

        // when
        int expectedNumberOfGuests = -1;
        OrderTableNumberOfGuestsUpdateRequest updateRequest = new OrderTableNumberOfGuestsUpdateRequest(
            expectedNumberOfGuests);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(createdOrderTable.getId(), updateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록되지 않은 테이블의 고객 수를 변경할 수 없다")
    void changeNumberOfGuestsWithNonRegisteredOrderTable() {
        // given
        Long fakeOrderTableId = 999L;

        // when
        int expectedNumberOfGuests = 5;
        OrderTableNumberOfGuestsUpdateRequest updateRequest = new OrderTableNumberOfGuestsUpdateRequest(
            expectedNumberOfGuests);

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(fakeOrderTableId, updateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("점유되지 않은 테이블의 고객 수를 변경할 수 없다")
    void changeNumberOfGuestsOfEmptyTable() {
        // given
        OrderTable orderTable = createEmptyOrderTable();
        OrderTable createdOrderTable = orderTableRepository.save(orderTable);

        // when
        int expectedNumberOfGuests = 5;
        OrderTableNumberOfGuestsUpdateRequest updateRequest = new OrderTableNumberOfGuestsUpdateRequest(
            expectedNumberOfGuests);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(createdOrderTable.getId(), updateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable createEmptyOrderTable() {

        OrderTable orderTable = new OrderTable(99, true);
        return orderTableRepository.save(orderTable);
    }
}
