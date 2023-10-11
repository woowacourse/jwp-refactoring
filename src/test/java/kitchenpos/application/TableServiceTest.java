package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.TableFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceIntegrationTest {

    @Autowired
    private TableService tableService;

    @Test
    @DisplayName("table을 생성한다.")
    void create() {
        final OrderTable orderTable = TableFixture.주문_테이블();

        final OrderTable savedOrderTable = tableService.create(orderTable);

        assertThat(savedOrderTable)
            .usingRecursiveComparison()
            .ignoringFields("id", "tableGroupId")
            .isEqualTo(orderTable);
    }

    @Test
    @DisplayName("table 목록을 조회한다.")
    void list() {
        final List<OrderTable> foundedOrderTables = tableService.list();

        final List<OrderTable> expectedOrderTables = TableFixture.전체_주문_테이블();
        assertThat(foundedOrderTables)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "tableGroupId")
            .isEqualTo(expectedOrderTables);
    }
}
