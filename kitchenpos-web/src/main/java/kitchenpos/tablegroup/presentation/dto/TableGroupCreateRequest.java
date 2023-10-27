package kitchenpos.tablegroup.presentation.dto;

import org.springframework.util.CollectionUtils;

import java.util.List;

public class TableGroupCreateRequest {

    private List<Long> tableIds;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<Long> tableIds) {
        validateTableIds(tableIds);
        this.tableIds = tableIds;
    }

    private void validateTableIds(List<Long> tableIds) {
        if (CollectionUtils.isEmpty(tableIds) || tableIds.size() < 2) {
            throw new IllegalArgumentException("테이블 그룹은 2개 이상의 테이블로 구성되어야 합니다.");
        }
    }

    public List<Long> getTableIds() {
        return tableIds;
    }
}
