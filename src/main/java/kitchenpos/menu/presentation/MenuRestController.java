package kitchenpos.menu.presentation;

import java.net.URI;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.application.dto.MenusResponse;
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
        MenuResponse response = menuService.create(request);
        final URI uri = URI.create("/api/menus/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response)
                ;
    }

    @GetMapping("/api/menus")
    public ResponseEntity<MenusResponse> list() {
        return ResponseEntity.ok()
                .body(menuService.list())
                ;
    }
}
