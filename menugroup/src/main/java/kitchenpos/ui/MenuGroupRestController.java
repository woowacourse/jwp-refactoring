package kitchenpos.ui;

import kitchenpos.application.MenuGroupService;
import kitchenpos.dto.MenuGroupCreateRequest;
import kitchenpos.dto.MenuGroupResponse;
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
    public ResponseEntity<MenuGroupResponse> create(@RequestBody final MenuGroupCreateRequest request) {
        final MenuGroupResponse menuGroup = menuGroupService.create(request);
        final URI uri = URI.create("/api/menu-groups/" + menuGroup.getId());

        return ResponseEntity.created(uri).body(menuGroup);
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> list() {
        final List<MenuGroupResponse> menuGroups = menuGroupService.list();
        return ResponseEntity.ok().body(menuGroups);
    }
}
