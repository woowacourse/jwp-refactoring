package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.dto.response.MenuGroupResponse;
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
    public ResponseEntity<MenuGroupResponse> create(@RequestBody final MenuGroupRequest request) {
        final MenuGroup created = menuGroupService.create(request);

        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        final MenuGroupResponse body = MenuGroupResponse.from(created);
        return ResponseEntity.created(uri)
                .body(body)
                ;
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> list() {
        final List<MenuGroup> foundMenuGroups = menuGroupService.list();

        final List<MenuGroupResponse> body = MenuGroupResponse.from(foundMenuGroups);
        return ResponseEntity.ok()
                .body(body)
                ;
    }
}
