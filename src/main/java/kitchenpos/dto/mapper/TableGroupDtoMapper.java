package kitchenpos.dto.mapper;

import kitchenpos.domain.TableGroup;
import kitchenpos.dto.response.TableGroupCreateResponse;

public interface TableGroupDtoMapper {

    TableGroupCreateResponse toTableGroupCreateResponse(TableGroup tableGroup);
}
