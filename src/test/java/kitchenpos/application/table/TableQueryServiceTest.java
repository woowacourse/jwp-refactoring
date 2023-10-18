package kitchenpos.application.table;

import kitchenpos.application.TableService;
import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class TableQueryServiceTest extends ApplicationTestConfig {

    private TableService tableService;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderRepository, orderTableRepository);
    }

    @DisplayName("[SUCCESS] 전체 테이블 목록을 조회한다.")
    @Test
    void success_findAll() {
        // given
        final OrderTable orderTable = new OrderTable(null, 5, false);
        final OrderTable expected = tableService.create(orderTable);

        // when
        final List<OrderTable> actual = tableService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(1);
            final OrderTable actualOrderTable = actual.get(0);

            softly.assertThat(actualOrderTable.getId()).isEqualTo(expected.getId());
            softly.assertThat(actualOrderTable.getTableGroup()).isEqualTo(expected.getTableGroup());
            softly.assertThat(actualOrderTable.getNumberOfGuests()).isEqualTo(expected.getNumberOfGuests());
            softly.assertThat(actualOrderTable.isEmpty()).isEqualTo(expected.isEmpty());
        });
    }
}
