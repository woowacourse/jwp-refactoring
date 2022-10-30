package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
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
    public ResponseEntity<MenuResponse> create(@RequestBody MenuRequest menuRequest) {

        final Menu created = menuService.create(menuRequest);
        final MenuResponse menuResponse = MenuResponse.from(created);
        final URI uri = URI.create("/api/menus/" + created.getId());

        return ResponseEntity.created(uri)
                .body(menuResponse);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {

        final List<Menu> menus = menuService.list();

        final List<MenuResponse> menuResponses = menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(menuResponses);
    }
}
