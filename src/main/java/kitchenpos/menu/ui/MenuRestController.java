package kitchenpos.menu.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.request.MenuCreateRequest;
import kitchenpos.menu.response.MenuResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuRestController {

    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuResponse> create(@RequestBody MenuCreateRequest request) {
        Menu created = menuService.create(request);
        URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri).body(MenuResponse.from(created));
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        List<MenuResponse> response = MenuResponse.from(menuService.list());
        return ResponseEntity.ok().body(response);
    }
}
