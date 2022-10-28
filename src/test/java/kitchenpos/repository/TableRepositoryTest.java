package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;

class TableRepositoryTest extends RepositoryTest {

    @Test
    void 주문테이블을_저장한다() {
        OrderTable savedOrderTable = tableRepository.save(new OrderTable(1, false));

        assertThat(orderTableDao.findById(savedOrderTable.getId())).isPresent();
    }

    @Test
    void 주문테이블_목록을_가온다() {
        OrderTable orderTable = new OrderTable(0, false);

        int beforeSize = tableRepository.findAll().size();
        orderTableDao.save(orderTable);

        assertThat(tableRepository.findAll().size()).isEqualTo(beforeSize + 1);
    }

    @Test
    void 손님_수를_변경한다() {
        OrderTable orderTable = new OrderTable(2, false);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        tableRepository.changeNumberOfGuests(savedOrderTable.getId(), 10);

        assertThat(orderTableDao.findById(savedOrderTable.getId()).get().getNumberOfGuests()).isEqualTo(10);
    }

    @Test
    void 주문테이블을_비운다() {
        OrderTable orderTable = new OrderTable(0, false);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        tableRepository.changeEmpty(savedOrderTable.getId(), true);

        assertThat(orderTableDao.findById(savedOrderTable.getId()).get().isEmpty()).isTrue();
    }
}
