package kitchenpos.table.application;

import kitchenpos.table.application.TableService;
import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableResponse;
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
        final OrderTableCreateRequest request = new OrderTableCreateRequest(5, false);
        final OrderTableResponse expected = tableService.create(request);

        // when
        final List<OrderTableResponse> actual = tableService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(1);
            final OrderTableResponse actualOrderTable = actual.get(0);

            softly.assertThat(actualOrderTable.getId()).isEqualTo(expected.getId());
            softly.assertThat(actualOrderTable.getNumberOfGuests()).isEqualTo(expected.getNumberOfGuests());
            softly.assertThat(actualOrderTable.isEmpty()).isEqualTo(expected.isEmpty());
        });
    }
}
