package kitchenpos.ui;

import kitchenpos.menu.service.MenuService;
import kitchenpos.menu.service.MenuDto;
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
    public ResponseEntity<MenuDto> create(@RequestBody final MenuDto menuDto) {
        final MenuDto created = menuService.create(menuDto);
        final URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri)
                             .body(created)
            ;
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuDto>> list() {
        return ResponseEntity.ok()
                             .body(menuService.list())
            ;
    }
}
