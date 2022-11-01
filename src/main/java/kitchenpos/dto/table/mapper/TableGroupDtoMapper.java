package kitchenpos.dto.table.mapper;

import kitchenpos.domain.table.TableGroup;
import kitchenpos.dto.table.response.TableGroupResponse;

public interface TableGroupDtoMapper {

    TableGroupResponse toTableGroupResponse(TableGroup tableGroup);
}
