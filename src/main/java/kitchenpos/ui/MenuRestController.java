package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.MenuService;
import kitchenpos.domain.menu.Menu;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.MenuResponse;
import kitchenpos.ui.dto.MenuUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuRestController {

    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuResponse> create(@RequestBody final MenuRequest request) {
        final Menu created = menuService.create(request);
        final var response = MenuResponse.from(created);

        final URI uri = URI.create("/api/menus/" + created.getId());

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        final var menus = menuService.list();
        final var response = mapToMenuResponses(menus);

        return ResponseEntity.ok().body(response);
    }

    private List<MenuResponse> mapToMenuResponses(final List<Menu> menus) {
        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    @PutMapping("/api/menus/{menuId}")
    public ResponseEntity<MenuResponse> changeMenuInfo(@PathVariable final Long menuId,
                                                       @RequestBody final MenuUpdateRequest request) {
        final var updated = menuService.update(menuId, request);
        final var response = MenuResponse.from(updated);

        return ResponseEntity.ok(response);
    }
}
