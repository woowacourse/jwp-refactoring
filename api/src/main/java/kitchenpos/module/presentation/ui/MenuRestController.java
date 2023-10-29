package kitchenpos.module.presentation.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.module.application.MenuService;
import kitchenpos.module.domain.menu.Menu;
import kitchenpos.module.presentation.dto.request.MenuCreateRequest;
import kitchenpos.module.presentation.dto.response.MenuResponse;
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
    public ResponseEntity<MenuResponse> create(@RequestBody final MenuCreateRequest request) {
        final Menu created = menuService.create(request);
        final URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri)
                .body(MenuResponse.from(created))
                ;
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        final List<Menu> menus = menuService.list();
        return ResponseEntity.ok()
                .body(MenuResponse.of(menus))
                ;
    }
}
