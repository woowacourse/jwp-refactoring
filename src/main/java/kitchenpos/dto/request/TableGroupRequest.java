package kitchenpos.dto.request;

import java.util.List;
import org.springframework.util.CollectionUtils;

public class TableGroupRequest {

    private List<Long> tableIds;

    protected TableGroupRequest() {
    }

    public TableGroupRequest(final List<Long> tableIds) {
        if (CollectionUtils.isEmpty(tableIds) || tableIds.size() < 2) {
            throw new IllegalArgumentException("테이블의 수가 올바르지 않습니다.");
        }
        this.tableIds = tableIds;
    }

    public List<Long> getTableIds() {
        return tableIds;
    }
}
