package kitchenpos.menu_group.ui;

import kitchenpos.menu_group.application.MenuGroupService;
import kitchenpos.menu_group.application.dto.request.MenuGroupCreateRequest;
import kitchenpos.menu_group.application.dto.response.MenuGroupQueryResponse;
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
    public ResponseEntity<MenuGroupQueryResponse> create(
            @RequestBody final MenuGroupCreateRequest request) {
        final MenuGroupQueryResponse created = menuGroupService.create(request);
        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupQueryResponse>> list() {
        return ResponseEntity.ok()
                .body(menuGroupService.list());
    }
}
