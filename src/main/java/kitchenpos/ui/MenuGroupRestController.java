package kitchenpos.ui;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static kitchenpos.ui.MenuGroupRestController.MENU_GROUP_API;

@RequestMapping(MENU_GROUP_API)
@RestController
public class MenuGroupRestController {
    public static final String MENU_GROUP_API = "/api/menu-groups";

    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping
    public ResponseEntity<MenuGroup> create(@RequestBody final MenuGroup menuGroup) {
        final MenuGroup created = menuGroupService.create(menuGroup);
        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping
    public ResponseEntity<List<MenuGroup>> list() {
        return ResponseEntity.ok()
                .body(menuGroupService.list())
                ;
    }
}
