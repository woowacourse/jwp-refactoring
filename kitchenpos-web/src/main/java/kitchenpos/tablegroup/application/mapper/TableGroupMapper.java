package kitchenpos.tablegroup.application.mapper;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupResponse;

public class TableGroupMapper {

    private TableGroupMapper() {
    }

    public static TableGroup toTableGroup() {
        return new TableGroup();
    }

    public static TableGroupResponse toTableGroupResponse(final TableGroup tableGroup) {
        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate()
        );
    }
}
