package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.common.annotation.SpringTestWithData;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("OrderTableDao 는 ")
@SpringTestWithData
class OrderTableDaoTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("주문 테이블을 저장한다.")
    @Test
    void save() {
        final OrderTable orderTable = new OrderTable(0, true);

        final OrderTable actual = orderTableDao.save(orderTable);

        assertAll(
                () -> assertThat(actual.getId()).isGreaterThanOrEqualTo(0),
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(0)
        );
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void findAll() {
        final OrderTable orderTable = new OrderTable(0, true);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final List<OrderTable> actual = orderTableDao.findAll();

        assertAll(
                () -> assertThat(actual.size()).isEqualTo(1),
                () -> assertThat(actual.get(0).getId()).isEqualTo(savedOrderTable.getId())
        );
    }
}
