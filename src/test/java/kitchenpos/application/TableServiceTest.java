package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.GuestSizeException;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.ui.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.ui.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    void 주문테이블을_저장한다() {
        OrderTableCreateRequest request = new OrderTableCreateRequest(2, false);
        OrderTable savedOrderTable = tableService.create(request);

        assertThat(orderTableDao.findById(savedOrderTable.getId())).isPresent();
    }

    @Test
    void 주문테이블_목록을_불러온다() {
        OrderTable orderTable = new OrderTable(0, false);

        int beforeSize = tableService.list().size();
        orderTableDao.save(orderTable);

        assertThat(tableService.list().size()).isEqualTo(beforeSize + 1);
    }

    @Test
    void 주문테이블을_비울때_아이디가_잘못되면_예외를_반환한다() {
        OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(true);

        assertThatThrownBy(() -> tableService.changeEmpty(0L, request))
                .isInstanceOf(NotFoundOrderTableException.class);
    }

    @Test
    void 손님_수를_변경한다() {
        OrderTable orderTable = new OrderTable(2, false);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        OrderTableChangeNumberOfGuestsRequest request
                = new OrderTableChangeNumberOfGuestsRequest(10);
        tableService.changeNumberOfGuests(savedOrderTable.getId(), request);

        assertThat(orderTableDao.findById(savedOrderTable.getId()).get().getNumberOfGuests()).isEqualTo(10);
    }

    @Test
    void 손님_수를_변경할때_잘못된_수는_예외를_반환한다() {
        OrderTable orderTable = new OrderTable(2, false);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

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
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        OrderTableChangeNumberOfGuestsRequest request
                = new OrderTableChangeNumberOfGuestsRequest(10);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), request))
                .isInstanceOf(GuestSizeException.class);
    }
}
