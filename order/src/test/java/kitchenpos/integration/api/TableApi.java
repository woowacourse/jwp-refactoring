package kitchenpos.integration.api;

import java.util.List;
import kitchenpos.table.application.response.OrderTableResponse;
import kitchenpos.table.ui.request.OrderTableChangeEmptyRequest;
import kitchenpos.table.ui.request.OrderTableChangeGuestsRequest;
import kitchenpos.table.ui.request.OrderTableCreateRequest;
import kitchenpos.testtool.MockMvcResponse;
import kitchenpos.testtool.MockMvcUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TableApi {

    private static final String BASE_URL = "/api/tables";

    @Autowired
    private MockMvcUtils mockMvcUtils;

    public MockMvcResponse<OrderTableResponse> 테이블_등록(OrderTableCreateRequest orderTable) {
        return mockMvcUtils.request()
            .post(BASE_URL)
            .content(orderTable)
            .asSingleResult(OrderTableResponse.class);
    }

    public MockMvcResponse<OrderTableResponse> 테이블_등록(int numberOfGuests, boolean empty) {
        return 테이블_등록(OrderTableCreateRequest.create(numberOfGuests, empty));
    }

    public MockMvcResponse<List<OrderTableResponse>> 테이블_조회() {
        return mockMvcUtils.request()
            .get(BASE_URL)
            .asMultiResult(OrderTableResponse.class);
    }

    public MockMvcResponse<OrderTableResponse> 테이블_빈자리_수정(Long id, boolean empty) {
        final OrderTableChangeEmptyRequest request = OrderTableChangeEmptyRequest.create(empty);
        return mockMvcUtils.request()
            .put(BASE_URL+"/{orderTableId}/empty", id)
            .content(request)
            .asSingleResult(OrderTableResponse.class);
    }

    public MockMvcResponse<OrderTableResponse> 테이블_손님수_수정(Long id, int numberOfGuests) {
        final OrderTableChangeGuestsRequest request = OrderTableChangeGuestsRequest
            .create(numberOfGuests);
        return mockMvcUtils.request()
            .put(BASE_URL+"/{orderTableId}/number-of-guests", id)
            .content(request)
            .asSingleResult(OrderTableResponse.class);
    }
}
