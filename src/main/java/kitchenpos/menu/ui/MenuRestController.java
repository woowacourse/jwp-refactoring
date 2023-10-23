package kitchenpos.menu.ui;

import kitchenpos.menu.appllication.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.request.MenuCreateRequest;
import kitchenpos.menu.dto.response.MenuResponse;
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
    public ResponseEntity<Void> create(@RequestBody final MenuCreateRequest request) {
        final Long id = menuService.create(request);
        final URI uri = URI.create("/api/menus/" + id);
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        return ResponseEntity.ok().body(menuService.list());
    }
}
