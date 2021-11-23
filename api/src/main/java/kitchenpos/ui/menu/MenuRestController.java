package kitchenpos.ui.menu;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import kitchenpos.application.MenuService;
import kitchenpos.dto.request.ChangeNamePriceRequest;
import kitchenpos.dto.request.CreateMenuRequest;
import kitchenpos.dto.response.MenuResponse;

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
