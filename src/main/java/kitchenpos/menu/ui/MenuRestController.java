package kitchenpos.menu.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.dto.MenuCreationDto;
import kitchenpos.menu.application.dto.MenuDto;
import kitchenpos.menu.ui.dto.request.MenuCreationRequest;
import kitchenpos.menu.ui.dto.response.MenuResponse;
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

    @PostMapping("/api/v2/menus")
    public ResponseEntity<MenuResponse> create(@RequestBody final MenuCreationRequest menuCreationRequest) {
        final MenuDto created = menuService.create(MenuCreationDto.from(menuCreationRequest));
        final MenuResponse menuResponse = MenuResponse.from(created);
        final URI uri = URI.create("/api/v2/menus/" + menuResponse.getId());
        return ResponseEntity.created(uri).body(menuResponse);
    }

    @GetMapping("/api/v2/menus")
    public ResponseEntity<List<MenuDto>> getMenus() {
        return ResponseEntity.ok().body(menuService.getMenus());
    }
}
