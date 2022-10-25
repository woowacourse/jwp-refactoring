package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.MenuService;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.ui.dto.MenuRequest;
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
    public ResponseEntity<MenuResponse> create(@RequestBody final MenuRequest menuRequest) {
        MenuResponse menuResponse = menuService.create(menuRequest.toCommand());
        final URI uri = URI.create("/api/menus/" + menuResponse.id());
        return ResponseEntity.created(uri)
                .body(menuResponse);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<Menu>> list() {
        return ResponseEntity.ok()
                .body(menuService.list());
    }
}
