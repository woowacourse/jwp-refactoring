package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.MenusResponse;
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
        final Menu menu = menuService.create(menuRequest);
        final MenuResponse menuResponse = MenuResponse.from(menu);
        return ResponseEntity.created(URI.create("/api/menus/" + menu.getId())).body(menuResponse);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<MenusResponse> list() {
        final List<Menu> menus = menuService.list();
        final MenusResponse menusResponse = MenusResponse.from(menus);
        return ResponseEntity.ok().body(menusResponse);
    }
}
