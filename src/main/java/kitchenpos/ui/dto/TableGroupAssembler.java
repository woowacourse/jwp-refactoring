package kitchenpos.ui.dto;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.application.dto.request.TableGroupIdRequestDto;
import kitchenpos.application.dto.request.TableGroupRequestDto;
import kitchenpos.application.dto.request.TableIdRequestDto;
import kitchenpos.application.dto.response.TableGroupResponseDto;
import kitchenpos.ui.dto.request.TableGroupRequest;
import kitchenpos.ui.dto.response.TableGroupResponse;

public class TableGroupAssembler {

    private TableGroupAssembler() {
    }

    public static TableGroupRequestDto tableGroupRequestDto(TableGroupRequest request) {
        List<TableIdRequestDto> orderTables = request.getOrderTables().stream()
            .map(source -> new TableIdRequestDto(source.getId()))
            .collect(toList());

        return new TableGroupRequestDto(orderTables);
    }

    public static TableGroupResponse tableGroupResponse(TableGroupResponseDto responseDto) {
        return new TableGroupResponse(responseDto.getId());
    }

    public static TableGroupIdRequestDto tableGroupIdRequestDto(Long id) {
        return new TableGroupIdRequestDto(id);
    }
}
