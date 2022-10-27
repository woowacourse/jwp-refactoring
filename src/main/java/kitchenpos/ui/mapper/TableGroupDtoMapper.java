package kitchenpos.ui.mapper;

import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.response.TableGroupCreateResponse;

public interface TableGroupDtoMapper {

    TableGroupCreateResponse toTableGroupCreateResponse(TableGroup tableGroup);
}
