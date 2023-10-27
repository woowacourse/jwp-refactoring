package kitchenpos.menu.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.ui.request.MenuCreateRequest;
import kitchenpos.menu.ui.response.MenuResponse;
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
    public ResponseEntity<MenuResponse> create(
            @Valid @RequestBody final MenuCreateRequest request
    ) {
        final var response = menuService.create(request);
        final var uri = URI.create("/api/menus/" + response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        final var response = menuService.list();
        return ResponseEntity.ok(response);
    }
}
