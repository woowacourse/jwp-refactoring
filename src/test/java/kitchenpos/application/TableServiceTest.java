package kitchenpos.application;

import static kitchenpos.fixture.DomainFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.GuestSizeException;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.exception.OrderTableConvertEmptyStatusException;
import kitchenpos.ui.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.ui.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;
import org.junit.jupiter.api.Test;

class TableServiceTest extends ServiceTest {

    @Test
    void 주문테이블을_저장한다() {
        OrderTableCreateRequest request = new OrderTableCreateRequest(2, false);
        OrderTable savedOrderTable = tableService.create(request);

        assertThat(orderTableRepository.findById(savedOrderTable.getId())).isPresent();
    }

    @Test
    void 주문테이블_목록을_불러온다() {
        OrderTable orderTable = new OrderTable(0, false);

        int beforeSize = tableService.list().size();
        orderTableRepository.save(orderTable);

        assertThat(tableService.list().size()).isEqualTo(beforeSize + 1);
    }

    @Test
    void 주문테이블을_비운다() {
        OrderTable orderTable = new OrderTable(0, false);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(true);

        tableService.changeEmpty(savedOrderTable.getId(), request);

        assertThat(orderTableRepository.findById(savedOrderTable.getId()).get().isEmpty()).isTrue();
    }

    @Test
    void 주문테이블을_비울때_아이디가_잘못되면_예외를_반환한다() {
        OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(true);

        assertThatThrownBy(() -> tableService.changeEmpty(0L, request))
                .isInstanceOf(NotFoundOrderTableException.class);
    }

    @Test
    void 주문테이블을_비울수_없는_상태면_예외를_반환한다() {
        OrderTable savedOrderTable = orderTableRepository.save(createOrderTable());
        orderRepository.save(new Order(savedOrderTable));

        OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(true);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), request))
                .isInstanceOf(OrderTableConvertEmptyStatusException.class);
    }

    @Test
    void 손님_수를_변경한다() {
        OrderTable orderTable = new OrderTable(2, false);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        OrderTableChangeNumberOfGuestsRequest request
                = new OrderTableChangeNumberOfGuestsRequest(10);
        tableService.changeNumberOfGuests(savedOrderTable.getId(), request);

        assertThat(orderTableRepository.findById(savedOrderTable.getId()).get().getNumberOfGuests()).isEqualTo(10);
    }

    @Test
    void 손님_수를_변경할때_잘못된_수는_예외를_반환한다() {
        OrderTable orderTable = new OrderTable(2, false);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        OrderTableChangeNumberOfGuestsRequest request
                = new OrderTableChangeNumberOfGuestsRequest(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), request))
                .isInstanceOf(GuestSizeException.class);
    }

    @Test
    void 손님_수를_변경할때_orderTableId를_찾을수없으면_예외를_반환한다() {
        OrderTableChangeNumberOfGuestsRequest request
                = new OrderTableChangeNumberOfGuestsRequest(10);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, request))
                .isInstanceOf(NotFoundOrderTableException.class);
    }

    @Test
    void 손님_수를_변경할때_비어있으면_예외를_반환한다() {
        OrderTable orderTable = new OrderTable(2, true);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        OrderTableChangeNumberOfGuestsRequest request
                = new OrderTableChangeNumberOfGuestsRequest(10);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), request))
                .isInstanceOf(GuestSizeException.class);
    }
}
