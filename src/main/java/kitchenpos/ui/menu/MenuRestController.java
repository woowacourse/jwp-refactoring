package kitchenpos.ui.menu;

import java.net.URI;
import java.util.List;
import kitchenpos.application.menu.MenuService;
import kitchenpos.dto.menu.request.MenuCreateRequest;
import kitchenpos.dto.menu.response.MenuResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menus")
public class MenuRestController {

    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<MenuResponse> create(@RequestBody final MenuCreateRequest request) {
        MenuResponse response = menuService.create(request);
        URI uri = URI.create("/api/menus/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<MenuResponse>> list() {
        List<MenuResponse> responses = menuService.list();
        return ResponseEntity.ok()
                .body(responses);
    }
}
