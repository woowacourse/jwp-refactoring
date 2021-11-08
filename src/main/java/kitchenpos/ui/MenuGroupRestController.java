package kitchenpos.ui;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.MenuGroupRequest;
import kitchenpos.ui.dto.MenuGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupResponse> create(@RequestBody final MenuGroupRequest menuGroupRequest) {
        final MenuGroupResponse createdMenuGroup = menuGroupService.create(menuGroupRequest);
        final URI uri = URI.create("/api/menu-groups/" + createdMenuGroup.getId());

        return ResponseEntity.created(uri)
                .body(createdMenuGroup);
                .body(created);
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> list() {
        List<MenuGroupResponse> menuGroups = menuGroupService.findAll();

        return ResponseEntity.ok()
                .body(menuGroups);
                .body(menuGroupService.list());
    }
}
