package kitchenpos.menu.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.response.MenuResponse;
import kitchenpos.menu.ui.request.MenuCreateRequest;
import kitchenpos.menu.ui.request.MenuUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuRestController {
    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuResponse> create(@RequestBody final MenuCreateRequest menuCreateRequest) {
        final MenuResponse created = menuService.create(menuCreateRequest.toEntity());
        final URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        return ResponseEntity.ok()
                .body(menuService.list());
    }

    @PutMapping("/api/menus/{menuId}")
    public ResponseEntity<MenuResponse> updateMenu(@PathVariable Long menuId,
                                                   @RequestBody MenuUpdateRequest menuUpdateRequest) {
        final MenuResponse menuResponse = menuService.updateMenuDetails(menuId, menuUpdateRequest.getName(),
                menuUpdateRequest.getPrice());
        return ResponseEntity.ok(menuResponse);
    }
}
