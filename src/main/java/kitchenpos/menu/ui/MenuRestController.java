package kitchenpos.menu.ui;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.ui.dto.MenuRequest;
import kitchenpos.menu.ui.dto.MenuResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class MenuRestController {
    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuResponse> create(@RequestBody @Valid final MenuRequest menuRequest) {
        final MenuResponse menuResponse = menuService.create(menuRequest);
        final URI uri = URI.create("/api/menus/" + menuResponse.getId());
        return ResponseEntity.created(uri)
                .body(menuResponse)
                ;
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        return ResponseEntity.ok()
                .body(menuService.list())
                ;
    }
}
