package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
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
        OrderTable orderTable = createOrderTable(2, false);
        OrderTable savedOrderTable = tableService.create(orderTable);

        assertThat(orderTableDao.findById(savedOrderTable.getId())).isPresent();
    }

    @Test
    void 주문테이블_목록을_불러온다() {
        OrderTable orderTable = new OrderTable();

        int beforeSize = tableService.list().size();
        tableService.create(orderTable);

        assertThat(tableService.list().size()).isEqualTo(beforeSize + 1);
    }

    @Test
    void 주문테이블을_비울때_아이디가_잘못되면_예외를_반환한다() {
        OrderTable orderTable = new OrderTable();

        assertThatThrownBy(() -> tableService.changeEmpty(0L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 손님_수를_변경한다() {
        OrderTable orderTable = createOrderTable(10, false);
        OrderTable savedOrderTable = tableService.create(orderTable);

        tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable);

        assertThat(orderTableDao.findById(savedOrderTable.getId()).get().getNumberOfGuests()).isEqualTo(10);
    }

    @Test
    void 손님_수를_변경할때_잘못된_수는_예외를_반환한다() {
        OrderTable orderTable = createOrderTable(-1, false);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), savedOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 손님_수를_변경할때_orderTableId를_찾을수없으면_예외를_반환한다() {
        OrderTable orderTable = new OrderTable();

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 손님_수를_변경할때_비어있으면_예외를_반환한다() {
        OrderTable orderTable = createOrderTable(1, true);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable createOrderTable(int numberOfGuests, boolean isEmpty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(isEmpty);
        return orderTable;
    }
}
