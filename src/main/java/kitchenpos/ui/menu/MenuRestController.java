package kitchenpos.ui.menu;

import kitchenpos.application.menu.MenuService;
import kitchenpos.application.menu.request.MenuCreateRequest;
import kitchenpos.domain.menu.Menu;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class MenuRestController {
    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<Menu> create(@RequestBody MenuCreateRequest request) {
        final Menu menu = menuService.create(request);
        final URI uri = URI.create("/api/menus/" + menu.getId());
        return ResponseEntity.created(uri).body(menu);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<Menu>> list() {
        return ResponseEntity.ok().body(menuService.list());
    }
}
