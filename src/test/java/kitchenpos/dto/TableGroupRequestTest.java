package kitchenpos.dto;

import kitchenpos.dto.table.request.OrderTableRequest;
import kitchenpos.dto.table.request.TableGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class TableGroupRequestTest {

    @DisplayName("table group 요청 table이 2개 미만일 때")
    @Test
    void create() {
        List<OrderTableRequest> orderTableRequests = Arrays.asList(new OrderTableRequest(1L));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new TableGroupRequest(orderTableRequests))
                .withMessage("table group에 속한 테이블은 2개 이상이여야 합니다.");
    }

    @DisplayName("TableGroupRequest에서 OrderTable id민 가져오기")
    @Test
    void extractTableIds() {
        List<OrderTableRequest> orderTableRequests = Arrays.asList(
                new OrderTableRequest(1L),
                new OrderTableRequest(2L),
                new OrderTableRequest(3L)
        );
        TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableRequests);

        List<Long> actual = tableGroupRequest.extractTableIds();

        assertThat(actual).isEqualTo(Arrays.asList(1L, 2L, 3L));
    }
}