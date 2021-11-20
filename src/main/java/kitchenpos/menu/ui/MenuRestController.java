package kitchenpos.menu.ui;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.ui.request.ChangeNamePriceRequest;
import kitchenpos.menu.ui.request.CreateMenuRequest;
import kitchenpos.menu.ui.response.MenuResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class MenuRestController {
    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuResponse> create(@RequestBody final CreateMenuRequest request) {
        final MenuResponse response = menuService.create(request);
        final URI uri = URI.create("/api/menus/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        return ResponseEntity.ok()
                .body(menuService.list());
    }

    @PutMapping("/api/menus/{menuId}")
    public ResponseEntity<Void> changeNamePrice(
            @PathVariable final Long menuId,
            @RequestBody final ChangeNamePriceRequest request
    ) {
        menuService.changeNamePrice(menuId, request);
        return ResponseEntity.noContent().build();
    }
}
