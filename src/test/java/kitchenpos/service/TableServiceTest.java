package kitchenpos.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.application.request.OrderTableRequest;
import kitchenpos.domain.OrderTable;

public class TableServiceTest extends ServiceTest {

    @Test
    @DisplayName("테이블을 등록한다.")
    void create() {
        // given
        OrderTableRequest request = new OrderTableRequest(NO_ID, NO_ID, 1, false);

        // when
        OrderTable savedOrderTable = tableService.create(request);

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
        OrderTable savedOrderTable = tableService.create(request);

        // when
        List<OrderTable> result = tableService.list();

        // then
        assertThat(result).contains(savedOrderTable);
    }
}
