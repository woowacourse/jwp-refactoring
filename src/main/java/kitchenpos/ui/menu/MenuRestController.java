package kitchenpos.ui.menu;

import kitchenpos.menu.MenuService;
import kitchenpos.menu.request.MenuCreateRequest;
import kitchenpos.menu.Menu;
import kitchenpos.ui.menu.response.MenuResponse;
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
    public ResponseEntity<MenuResponse> create(@RequestBody MenuCreateRequest request) {
        final Menu menu = menuService.create(request);
        final URI uri = URI.create("/api/menus/" + menu.getId());
        return ResponseEntity.created(uri).body(MenuResponse.of(menu));
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        List<Menu> menus = menuService.list();
        return ResponseEntity.ok().body(MenuResponse.of(menus));
    }
}
