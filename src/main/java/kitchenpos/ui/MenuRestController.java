package kitchenpos.ui;

import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.MenuResponse;
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
    public ResponseEntity<MenuResponse> create(@RequestBody final MenuRequest menuRequest) {
        final MenuResponse newMenu = menuService.create(menuRequest);
        final URI uri = URI.create("/api/menus/" + newMenu.getId());
        return ResponseEntity.created(uri)
                .body(newMenu);
                .body(created);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> findAll() {
        return ResponseEntity.ok()
                .body(menuService.findAll());
                .body(menuService.list());
    }
}
