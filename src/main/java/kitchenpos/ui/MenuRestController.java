package kitchenpos.ui;

import kitchenpos.application.MenuService;
import kitchenpos.application.dto.MenuCreationDto;
import kitchenpos.application.dto.MenuDto;
import kitchenpos.domain.Menu;
import kitchenpos.ui.dto.request.MenuCreationRequest;
import kitchenpos.ui.dto.response.MenuResponse;
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

    @Deprecated
    @PostMapping("/api/menus")
    public ResponseEntity<Menu> create(@RequestBody final Menu menu) {
        final Menu created = menuService.create(menu);
        final URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @PostMapping("/api/v2/menus")
    public ResponseEntity<MenuResponse> create(@RequestBody final MenuCreationRequest menuCreationRequest) {
        final MenuDto created = menuService.create(MenuCreationDto.from(menuCreationRequest));
        final MenuResponse menuResponse = MenuResponse.from(created);
        final URI uri = URI.create("/api/v2/menus/" + menuResponse.getId());
        return ResponseEntity.created(uri).body(menuResponse);
    }


    @GetMapping("/api/menus")
    public ResponseEntity<List<Menu>> list() {
        return ResponseEntity.ok()
                .body(menuService.list())
                ;
    }
}
