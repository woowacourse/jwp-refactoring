package kitchenpos.ui;

import kitchenpos.application.MenuService;
import kitchenpos.dto.menu.MenuCreateRequest;
import kitchenpos.dto.menu.MenuResponse;
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
    public ResponseEntity<MenuResponse> respondCreatedMenuResponse(@RequestBody final MenuCreateRequest menuCreateRequest) {
        final MenuResponse menuResponse = menuService.createMenu(menuCreateRequest);
        final URI uri = URI.create("/api/menus/" + menuResponse.getId());

        return ResponseEntity.created(uri)
                .body(menuResponse);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> respondAllMenuResponses() {
        return ResponseEntity.ok()
                .body(menuService.listAllMenus());
    }
}
