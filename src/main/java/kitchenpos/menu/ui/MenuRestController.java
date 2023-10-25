package kitchenpos.menu.ui;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuResponse;
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
    public ResponseEntity<MenuResponse> create(@RequestBody final MenuCreateRequest request) {
        final MenuResponse menu = menuService.create(request);
        final URI uri = URI.create("/api/menus/" + menu.getId());

        return ResponseEntity.created(uri).body(menu);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        final List<MenuResponse> menus = menuService.list();
        return ResponseEntity.ok().body(menus);
    }
}
