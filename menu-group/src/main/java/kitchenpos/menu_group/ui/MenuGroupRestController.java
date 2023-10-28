package kitchenpos.menu_group.ui;

import kitchenpos.menu_group.application.MenuGroupService;
import kitchenpos.menu_group.domain.MenuGroup;
import kitchenpos.menu_group.dto.request.MenuGroupCreateRequest;
import kitchenpos.menu_group.dto.response.MenuGroupResponse;
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
    public ResponseEntity<MenuGroup> create(@RequestBody MenuGroupCreateRequest request) {
        final Long menuGroupId = menuGroupService.create(request.getName());
        final URI uri = URI.create("/api/menu-groups/" + menuGroupId);
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> list() {
        return ResponseEntity.ok()
                .body(menuGroupService.list());
    }

}
