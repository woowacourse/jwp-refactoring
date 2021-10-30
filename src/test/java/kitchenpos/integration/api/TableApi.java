package kitchenpos.integration.api;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.integration.utils.MockMvcResponse;
import kitchenpos.integration.utils.MockMvcUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TableApi {

    private static final String BASE_URL = "/api/tables";

    @Autowired
    private MockMvcUtils mockMvcUtils;

    public MockMvcResponse<OrderTable> 테이블_등록(OrderTable orderTable) {
        return mockMvcUtils.request()
            .post(BASE_URL)
            .content(orderTable)
            .asSingleResult(OrderTable.class);
    }

    public MockMvcResponse<OrderTable> 테이블_등록(int numberOfGuests, boolean empty) {
        return 테이블_등록(new OrderTable(numberOfGuests, empty));
    }

    public MockMvcResponse<List<OrderTable>> 테이블_조회() {
        return mockMvcUtils.request()
            .get(BASE_URL)
            .asMultiResult(OrderTable.class);
    }

    public MockMvcResponse<OrderTable> 테이블_빈자리_수정(Long id, boolean empty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        return mockMvcUtils.request()
            .put(BASE_URL+"/{orderTableId}/empty", id)
            .content(orderTable)
            .asSingleResult(OrderTable.class);
    }

    public MockMvcResponse<OrderTable> 테이블_손님수_수정(Long id, int numberOfGuests) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        return mockMvcUtils.request()
            .put(BASE_URL+"/{orderTableId}/number-of-guests", id)
            .content(orderTable)
            .asSingleResult(OrderTable.class);
    }
}
