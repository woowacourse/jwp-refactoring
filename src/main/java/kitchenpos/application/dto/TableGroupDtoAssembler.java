package kitchenpos.application.dto;

import kitchenpos.application.dto.response.TableGroupResponseDto;
import kitchenpos.domain.TableGroup;

public class TableGroupDtoAssembler {

    private TableGroupDtoAssembler() {
    }

    public static TableGroupResponseDto tableGroupResponseDto(TableGroup tableGroup) {
        return new TableGroupResponseDto(tableGroup.getId());
    }
}
