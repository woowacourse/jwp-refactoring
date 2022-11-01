package kitchenpos.application;

import static kitchenpos.fixture.TableFixture.getOrderTableRequest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TableIntegrationTest extends IntegrationTest{

    @Autowired
    private TableService tableService;

    @Test
    void 테이블을_생성한다() {
        final OrderTable orderTable = tableService.create(getOrderTableRequest(1L, 10, false));
        assertThat(orderTable.getId()).isNotNull();
    }

    @Test
    void 테이블목록을_조회한다() {
        final List<OrderTable> orderTables = tableService.list();
        assertThat(orderTables).hasSize(8);
    }
}
