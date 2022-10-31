package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.MenuService;
import kitchenpos.dto.MenuDto;
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
    public ResponseEntity<MenuDto> create(@RequestBody final MenuDto menu) {
        MenuDto created = menuService.create(menu);
        URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping
    public ResponseEntity<List<MenuDto>> list() {
        return ResponseEntity.ok()
                .body(menuService.list())
                ;
    }
}
