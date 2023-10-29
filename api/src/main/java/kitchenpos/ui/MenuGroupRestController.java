package kitchenpos.ui;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.dto.MenuGroupCreateRequest;
import kitchenpos.menu.application.dto.MenuGroupResponse;
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
    public ResponseEntity<Void> create(@RequestBody final MenuGroupCreateRequest menuGroupCreateRequest) {
        final Long id = menuGroupService.create(menuGroupCreateRequest);
        return ResponseEntity.created(URI.create("/api/menu-groups/" + id)).build();
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> findAll() {
        final List<MenuGroupResponse> menuGroupResponses = menuGroupService.findAll();
        return ResponseEntity.ok().body(menuGroupResponses);
    }
}
