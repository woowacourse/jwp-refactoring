package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.ui.dto.request.MenuCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<Menu> create(@RequestBody MenuCreateRequest menuCreateRequest) {
        final Menu created = menuService.create(menuCreateRequest);
        final URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<Menu>> list() {
        return ResponseEntity.ok().body(menuService.list());
    }
}
