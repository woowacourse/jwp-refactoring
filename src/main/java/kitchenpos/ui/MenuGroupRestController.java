package kitchenpos.ui;

import kitchenpos.application.MenuGroupService;
import kitchenpos.ui.dto.menugroup.MenuGroupRequest;
import kitchenpos.ui.dto.menugroup.MenuGroupResponse;
import kitchenpos.ui.dto.menugroup.MenuGroupResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupResponse> create(@RequestBody final MenuGroupRequest request) {
        final MenuGroupResponse created = menuGroupService.create(request);
        return ResponseEntity.created(URI.create("/api/menu-groups/" + created.getId()))
                .body(created);
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupResponses> list() {
        return ResponseEntity.ok()
                .body(menuGroupService.list());
    }
}
