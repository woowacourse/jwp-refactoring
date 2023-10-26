package kitchenpos.menu.presentation;

import java.net.URI;
import java.util.List;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.presentation.dto.request.CreateMenuRequest;
import kitchenpos.menu.presentation.dto.response.MenuResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/menus")
@RestController
public class MenuController {

    private final MenuService menuService;

    public MenuController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<MenuResponse> createMenu(@RequestBody final CreateMenuRequest request) {
        final Menu menu = menuService.create(request);
        final MenuResponse response = MenuResponse.from(menu);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .location(URI.create("/api/menus/" + menu.getId()))
                             .body(response);
    }

    @GetMapping
    public ResponseEntity<List<MenuResponse>> findAllMenu() {
        final List<Menu> menus = menuService.list();
        final List<MenuResponse> responses = MenuResponse.convertToList(menus);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(responses);
    }
}
