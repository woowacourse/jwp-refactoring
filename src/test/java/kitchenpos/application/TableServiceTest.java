package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TableServiceTest extends IntegrationTest {

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable();
        orderTable2 = new OrderTable();
    }

    @Test
    void 주문_테이블을_저장한다() {
        // when
        OrderTable result = tableService.create(orderTable1);

        // then
        assertThat(result.getId()).isPositive();
    }

    @Test
    void 주문_테이블들을_조회한다() {
        // given
        OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        // when
        List<OrderTable> result = tableService.list();

        // then
        Assertions.assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).getId()).isEqualTo(savedOrderTable1.getId()),
                () -> assertThat(result.get(1).getId()).isEqualTo(savedOrderTable2.getId())
        );
    }
}
