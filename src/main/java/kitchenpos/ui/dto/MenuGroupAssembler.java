package kitchenpos.ui.dto;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.application.dto.request.MenuGroupRequestDto;
import kitchenpos.application.dto.response.MenuGroupResponseDto;
import kitchenpos.ui.dto.request.MenuGroupRequest;
import kitchenpos.ui.dto.response.MenuGroupResponse;

public class MenuGroupAssembler {

    private MenuGroupAssembler() {
    }

    public static MenuGroupRequestDto menuGroupRequestDto(MenuGroupRequest request) {
        return new MenuGroupRequestDto(request.getName());
    }

    public static MenuGroupResponse menuGroupResponse(MenuGroupResponseDto responseDto) {
        return new MenuGroupResponse(responseDto.getId(), responseDto.getName());
    }

    public static List<MenuGroupResponse> menuGroupsResponse(
        List<MenuGroupResponseDto> responsesDto
    ) {
        return responsesDto.stream()
            .map(MenuGroupAssembler::menuGroupResponse)
            .collect(toList());
    }
}
