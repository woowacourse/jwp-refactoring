package kitchenpos.ui.menu;

import kitchenpos.menu.MenuGroupService;
import kitchenpos.menu.request.MenuGroupCreateRequest;
import kitchenpos.menu.MenuGroup;
import kitchenpos.ui.menu.response.MenuGroupResponse;
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
    public ResponseEntity<MenuGroupResponse> create(@RequestBody MenuGroupCreateRequest request) {
        final MenuGroup menuGroup = menuGroupService.create(request);
        final URI uri = URI.create("/api/menu-groups/" + menuGroup.getId());
        return ResponseEntity.created(uri).body(MenuGroupResponse.of(menuGroup));
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroup>> list() {
        return ResponseEntity.ok().body(menuGroupService.list());
    }
}
