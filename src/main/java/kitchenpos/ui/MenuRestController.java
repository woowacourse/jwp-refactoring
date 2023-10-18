package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.MenuService;
import kitchenpos.application.dto.menu.MenuCreateRequest;
import kitchenpos.application.dto.menu.MenuCreateResponse;
import kitchenpos.application.dto.menu.MenuResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menus")
public class MenuRestController {

    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<MenuCreateResponse> create(@RequestBody final MenuCreateRequest request) {
        final MenuCreateResponse created = menuService.create(request);
        final URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping
    public ResponseEntity<List<MenuResponse>> list() {
        return ResponseEntity.ok()
                .body(menuService.list());
    }
}
