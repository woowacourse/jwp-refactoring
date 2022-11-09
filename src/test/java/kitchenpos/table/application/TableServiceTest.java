package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.ServiceTest;
import kitchenpos.table.application.request.OrderTableRequest;
import kitchenpos.table.application.response.OrderTableResponse;

public class TableServiceTest extends ServiceTest {

    @Test
    @DisplayName("테이블을 등록한다.")
    void create() {
        // given
        OrderTableRequest request = new OrderTableRequest(NO_ID, NO_ID, 1, false);

        // when
        OrderTableResponse savedOrderTable = tableService.create(request);

        // then
        assertThat(savedOrderTable).usingRecursiveComparison()
            .ignoringFields("id", "orders")
            .isEqualTo(request);
    }

    @Test
    @DisplayName("전체 테이블을 조회한다.")
    void list() {
        // given
        OrderTableRequest request = new OrderTableRequest(NO_ID, NO_ID, 1, false);
        OrderTableResponse savedOrderTable = tableService.create(request);

        // when
        List<OrderTableResponse> result = tableService.list();

        // then
        assertOrderTableResponse(result.get(0), savedOrderTable);
    }

    private void assertOrderTableResponse(final OrderTableResponse actual, final OrderTableResponse expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getTableGroupId()).isEqualTo(expected.getTableGroupId());
        assertThat(actual.getNumberOfGuests()).isEqualTo(expected.getNumberOfGuests());
        assertThat(actual.getEmpty()).isEqualTo(expected.getEmpty());
    }
}
