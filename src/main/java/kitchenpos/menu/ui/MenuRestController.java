package kitchenpos.menu.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.ui.dto.request.MenuCreateRequest;
import kitchenpos.menu.ui.dto.response.MenuCreateResponse;
import kitchenpos.menu.ui.dto.response.MenuFindAllResponse;
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
    public ResponseEntity<MenuCreateResponse> create(@RequestBody final MenuCreateRequest request) {
        final MenuCreateResponse response = menuService.create(request);
        final URI uri = URI.create("/api/menus/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuFindAllResponse>> list() {
        final List<MenuFindAllResponse> responses = menuService.list();
        return ResponseEntity.ok()
                .body(responses);
    }
}
