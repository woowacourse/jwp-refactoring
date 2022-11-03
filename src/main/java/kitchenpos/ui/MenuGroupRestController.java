package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import kitchenpos.dto.response.MenuGroupsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupResponse> create(@RequestBody final MenuGroupRequest menuGroupRequest) {
        final MenuGroup menuGroup = menuGroupService.create(menuGroupRequest);
        final MenuGroupResponse response = MenuGroupResponse.from(menuGroup);
        return ResponseEntity.created(URI.create("/api/menu-groups/" + menuGroup.getId())).body(response);
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupsResponse> list() {
        final List<MenuGroup> menuGroups = menuGroupService.list();
        final MenuGroupsResponse menuGroupsResponse = MenuGroupsResponse.from(menuGroups);
        return ResponseEntity.ok().body(menuGroupsResponse);
    }
}
