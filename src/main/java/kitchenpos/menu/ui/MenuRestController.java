package kitchenpos.menu.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.request.CreateMenuRequest;
import kitchenpos.menu.dto.response.MenuResponse;

@RestController
public class MenuRestController {
    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuResponse> create(@RequestBody final CreateMenuRequest request) {
        final MenuResponse created = menuService.create(request);
        final URI uri = URI.create("/api/menus/" + created.getId());

        return ResponseEntity.created(uri)
            .body(created)
            ;
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        final List<MenuResponse> menus = menuService.list();

        return ResponseEntity.ok()
            .body(menus)
            ;
    }
}
