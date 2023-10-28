package kitchenpos.menu.presentation;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuResponse;
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
    public ResponseEntity<Void> create(@RequestBody final MenuCreateRequest menuCreateRequest) {
        final Long id = menuService.create(menuCreateRequest);
        return ResponseEntity.created(URI.create("/api/menus/" + id)).build();
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> findAll() {
        final List<MenuResponse> menuResponses = menuService.findAll();
        return ResponseEntity.ok().body(menuResponses);
    }
}
