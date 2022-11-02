package kitchenpos.ui;

import java.util.stream.Collectors;
import kitchenpos.menu.application.MenuService;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.menu.domain.Menu;
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
    public ResponseEntity<MenuResponse> create(@RequestBody final MenuCreateRequest menuCreateRequest) {
        final Menu created = menuService.create(menuCreateRequest);
        final URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri)
                .body(MenuResponse.from(created));
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        final List<MenuResponse> response = menuService.list().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(response);
    }
}
