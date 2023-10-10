package kitchenpos.ui.v1;

import java.net.URI;
import java.util.List;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/menus")
public class MenuControllerV1 {

    private final MenuService menuService;

    public MenuControllerV1(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<Menu> create(@RequestBody Menu menu) {
        Menu created = menuService.create(menu);
        URI uri = URI.create("/api/v1/menus/" + created.getId());
        return ResponseEntity.created(uri)
            .body(created);
    }

    @GetMapping
    public ResponseEntity<List<Menu>> findAll() {
        return ResponseEntity.ok()
            .body(menuService.list());
    }
}
