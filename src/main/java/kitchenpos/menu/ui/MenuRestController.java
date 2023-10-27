package kitchenpos.menu.ui;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.ui.dto.MenuResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MenuRestController {

    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuResponse> create(@RequestBody final MenuCreateRequest menuCreateRequest) {
        final Menu menu = menuService.create(menuCreateRequest);
        final URI uri = URI.create("/api/menus/" + menu.getId());

        return ResponseEntity.created(uri)
                .body(MenuResponse.from(menu));
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        List<MenuResponse> response = menuService.list()
                .stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(response);
    }
}
