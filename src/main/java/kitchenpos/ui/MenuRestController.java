package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.MenuService;
import kitchenpos.application.dto.menu.CreateMenuResponse;
import kitchenpos.application.dto.menu.SearchMenuResponse;
import kitchenpos.ui.dto.CreateMenuRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuRestController {
    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<CreateMenuResponse> create(@RequestBody final CreateMenuRequest request) {
        final CreateMenuResponse created = menuService.create(request.toCommand());
        final URI uri = URI.create("/api/menus/" + created.id());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<SearchMenuResponse>> list() {
        return ResponseEntity.ok()
                .body(menuService.list());
    }
}
