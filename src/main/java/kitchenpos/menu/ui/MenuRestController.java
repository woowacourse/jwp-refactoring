package kitchenpos.menu.ui;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.ui.dto.request.MenuChangeRequest;
import kitchenpos.menu.ui.dto.request.MenuRequest;
import kitchenpos.menu.ui.dto.response.MenuResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuRestController {
    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<MenuResponse> create(@RequestBody final MenuRequest request) {
        final MenuResponse response = menuService.create(request);
        final URI uri = URI.create("/api/menus/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<MenuResponse>> list() {
        return ResponseEntity.ok()
                .body(menuService.list());
    }

    @PutMapping("/{menuId}")
    public ResponseEntity<Void> changeNamePrice(
            @PathVariable final Long menuId,
            @RequestBody final MenuChangeRequest request
    ) {
        menuService.changeNamePrice(menuId, request);
        return ResponseEntity.noContent().build();
    }
}
