package kitchenpos.menu.presentation;

import java.net.URI;
import java.util.List;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.presentation.dto.request.CreateMenuGroupRequest;
import kitchenpos.menu.presentation.dto.response.MenuGroupResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/menu-groups")
@RestController
public class MenuGroupController {

    private final MenuGroupService menuGroupService;

    public MenuGroupController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping
    public ResponseEntity<MenuGroupResponse> createMenuGroup(@RequestBody final CreateMenuGroupRequest request) {
        final MenuGroup menuGroup = menuGroupService.create(request);
        final MenuGroupResponse response = MenuGroupResponse.from(menuGroup);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .location(URI.create("/api/menu-groups/" + response.getId()))
                             .body(response);
    }

    @GetMapping
    public ResponseEntity<List<MenuGroupResponse>> findAllMenuGroup() {
        final List<MenuGroup> menuGroups = menuGroupService.list();
        final List<MenuGroupResponse> responses = MenuGroupResponse.convertToList(menuGroups);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(responses);
    }
}
