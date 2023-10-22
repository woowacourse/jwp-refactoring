package kitchenpos.ui;

import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.ui.dto.MenuCreateRequest;
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
    public ResponseEntity<Void> create(@RequestBody final MenuCreateRequest menuCreateRequest) {
        final Long menuId = menuService.create(
                menuCreateRequest.getName(),
                menuCreateRequest.getPrice(),
                menuCreateRequest.getMenuGroupId(),
                menuCreateRequest.getProductIds(),
                menuCreateRequest.getCounts()
        );
        final URI uri = URI.create("/api/menus/" + menuId);
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<Menu>> list() {
        return ResponseEntity.ok().body(menuService.list());
    }
}
