package kitchenpos.dto.mapper;

import kitchenpos.domain.TableGroup;
import kitchenpos.dto.response.TableGroupResponse;

public interface TableGroupDtoMapper {

    TableGroupResponse toTableGroupCreateResponse(TableGroup tableGroup);
}
