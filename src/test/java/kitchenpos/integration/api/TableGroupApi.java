package kitchenpos.integration.api;

import kitchenpos.table.application.response.TableGroupResponse;
import kitchenpos.integration.utils.MockMvcResponse;
import kitchenpos.integration.utils.MockMvcUtils;
import kitchenpos.table.ui.request.OrderTableRequest;
import kitchenpos.table.ui.request.TableGroupCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TableGroupApi {

    private static final String BASE_URL = "/api/table-groups";

    @Autowired
    private MockMvcUtils mockMvcUtils;

    public MockMvcResponse<TableGroupResponse> 테이블_그룹_등록(TableGroupCreateRequest tableGroup) {
        return mockMvcUtils.request()
            .post(BASE_URL)
            .content(tableGroup)
            .asSingleResult(TableGroupResponse.class);
    }

    public MockMvcResponse<TableGroupResponse> 테이블_그룹_등록(Long... orderTableIds) {
        final TableGroupCreateRequest request = TableGroupCreateRequest
            .create(OrderTableRequest.create(orderTableIds));
        return 테이블_그룹_등록(request);
    }

    public MockMvcResponse<Void> 테이블_그룹_목록_삭제(Long id) {
        return mockMvcUtils.request()
            .delete(BASE_URL+"/{id}", id)
            .asSingleResult(Void.class);
    }
}
