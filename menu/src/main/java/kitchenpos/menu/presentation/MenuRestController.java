package kitchenpos.menu.presentation;

import java.net.URI;
import java.util.List;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.dto.MenuCreateDto;
import kitchenpos.menu.application.dto.MenuDto;
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
    public ResponseEntity<MenuDto> create(@RequestBody final MenuCreateDto request) {
        final MenuDto response = menuService.create(request);

        final URI uri = URI.create("/api/menus/" + response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuDto>> list() {
        final List<MenuDto> response = menuService.list();

        return ResponseEntity.ok().body(response);
    }
}
