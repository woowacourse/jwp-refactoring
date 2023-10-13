package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.NoSuchElementException;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceIntegrationTest {

    @Autowired
    private TableService tableService;

    @Test
    void Order_Table_생성() {
        // given
        OrderTable orderTable = OrderTableFixture.테이블_그룹이_없는_주문_테이블_생성(1, false);

        // when
        Long orderTableId = tableService.create(orderTable)
                .getId();

        // then
        OrderTable savedOrderTableId = orderTableDao.findById(orderTableId)
                .orElseThrow(NoSuchElementException::new);
        assertThat(savedOrderTableId).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(orderTable);
    }

}
