package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;

class TableServiceTest extends IntegrationTest {

    @Test
    void 주문_테이블을_저장한다() {
        // given
        OrderTable orderTable = new OrderTable();

        // when
        OrderTable result = tableService.create(orderTable);

        // then
        assertThat(result.getId()).isPositive();
    }
}
