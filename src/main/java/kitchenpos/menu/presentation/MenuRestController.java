package kitchenpos.menu.presentation;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.application.dto.MenuCreateRequest;
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
    public ResponseEntity<Menu> create(@RequestBody @Valid MenuCreateRequest request) {
        Menu savedMenu = menuService.create(request);
        URI uri = URI.create("/api/menus/" + savedMenu.getId());

        return ResponseEntity.created(uri)
                .body(savedMenu);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<Menu>> list() {
        List<Menu> responses = menuService.list();

        return ResponseEntity.ok()
                .body(responses);
    }

}
