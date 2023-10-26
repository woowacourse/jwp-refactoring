package kitchenpos.legacy.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.legacy.application.LegacyMenuService;
import kitchenpos.legacy.domain.Menu;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final LegacyMenuService menuService;

    public MenuController(LegacyMenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<Menu> create(@RequestBody Menu menu) {
        Menu created = menuService.create(menu);
        URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri)
            .body(created);
    }

    @GetMapping
    public ResponseEntity<List<Menu>> findAll() {
        return ResponseEntity.ok()
            .body(menuService.findAll());
    }
}
