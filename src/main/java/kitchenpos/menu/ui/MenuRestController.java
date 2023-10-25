package kitchenpos.menu.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.request.MenuCreateRequest;
import kitchenpos.menu.application.response.MenuResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuRestController {

    private final MenuService menuService;

    public MenuRestController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuResponse> create(@RequestBody MenuCreateRequest menuCreateRequest) {
        MenuResponse menuResponse = menuService.create(menuCreateRequest);
        URI uri = URI.create("/api/menus/" + menuResponse.getId());
        return ResponseEntity.created(uri)
                .body(menuResponse);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        return ResponseEntity.ok()
                .body(menuService.list());
    }
}
