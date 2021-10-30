package kitchenpos.integration.api;

import java.util.Arrays;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.integration.utils.MockMvcResponse;
import kitchenpos.integration.utils.MockMvcUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TableGroupApi {

    private static final String BASE_URL = "/api/table-groups";

    @Autowired
    private MockMvcUtils mockMvcUtils;

    public MockMvcResponse<TableGroup> 테이블_그룹_등록(TableGroup tableGroup) {
        return mockMvcUtils.request()
            .post(BASE_URL)
            .content(tableGroup)
            .asSingleResult(TableGroup.class);
    }

    public MockMvcResponse<TableGroup> 테이블_그룹_등록(OrderTable ... orderTables) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(orderTables));
        return 테이블_그룹_등록(tableGroup);
    }

    public MockMvcResponse<Void> 테이블_그룹_목록_삭제(Long id) {
        return mockMvcUtils.request()
            .delete(BASE_URL+"/{id}", id)
            .asSingleResult(Void.class);
    }
}
