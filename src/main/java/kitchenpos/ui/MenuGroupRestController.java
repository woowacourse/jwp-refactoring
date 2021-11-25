package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.dto.request.menu.MenuGroupRequestDto;
import kitchenpos.application.dto.response.menu.MenuGroupResponseDto;
import kitchenpos.ui.dto.request.menu.MenuGroupRequest;
import kitchenpos.ui.dto.response.menu.MenuGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuGroupRestController {

    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupResponse> create(@RequestBody MenuGroupRequest menuGroupRequest) {
        MenuGroupRequestDto menuGroupRequestDto =
            new MenuGroupRequestDto(menuGroupRequest.getName());
        MenuGroupResponseDto menuGroupResponseDto = menuGroupService.create(menuGroupRequestDto);
        MenuGroupResponse menuGroupResponse = convert(menuGroupResponseDto);
        URI uri = URI.create("/api/menu-groups/" + menuGroupResponseDto.getId());
        return ResponseEntity.created(uri)
            .body(menuGroupResponse);
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> list() {
        List<MenuGroupResponse> menuGroupResponses = menuGroupService.list()
            .stream()
            .map(this::convert)
            .collect(Collectors.toList());
        return ResponseEntity.ok()
            .body(menuGroupResponses);
    }

    private MenuGroupResponse convert(MenuGroupResponseDto menuGroupResponseDto) {
        return new MenuGroupResponse(menuGroupResponseDto.getId(), menuGroupResponseDto.getName());
    }
}
