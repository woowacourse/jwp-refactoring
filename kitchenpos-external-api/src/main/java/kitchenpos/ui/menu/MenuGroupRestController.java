package kitchenpos.ui.menu;

import java.net.URI;
import java.util.List;
import kitchenpos.application.menu.MenuGroupService;
import kitchenpos.dto.menu.request.MenuGroupCreateRequest;
import kitchenpos.dto.menu.response.MenuGroupResponse;
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
    public ResponseEntity<MenuGroupResponse> create(
            @RequestBody final MenuGroupCreateRequest menuGroupCreateRequest) {
        MenuGroupResponse created = menuGroupService.create(menuGroupCreateRequest);
        URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> list() {
        return ResponseEntity.ok().body(menuGroupService.list());
    }
}
