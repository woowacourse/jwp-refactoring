package kitchenpos.legacy.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.legacy.application.LegacyMenuGroupService;
import kitchenpos.legacy.domain.MenuGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menu-groups")
public class MenuGroupController {

    private final LegacyMenuGroupService menuGroupService;

    public MenuGroupController(LegacyMenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping
    public ResponseEntity<MenuGroup> create(@RequestBody MenuGroup menuGroup) {
        MenuGroup created = menuGroupService.create(menuGroup);
        URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri)
            .body(created);
    }

    @GetMapping
    public ResponseEntity<List<MenuGroup>> findAll() {
        return ResponseEntity.ok()
            .body(menuGroupService.findAll());
    }
}
