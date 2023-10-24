package kitchenpos.menu.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import kitchenpos.common.dto.request.MenuCreationRequest;
import kitchenpos.common.dto.response.MenuResponse;
import kitchenpos.menu.application.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuRestController {

    private final MenuService menuService;

    public MenuRestController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuResponse> create(@Valid @RequestBody MenuCreationRequest request) {
        MenuResponse response = menuService.create(request);
        URI uri = URI.create("/api/menus/" + response.getId());

        return ResponseEntity.created(uri)
                .body(response);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        return ResponseEntity.ok()
                .body(menuService.list());
    }

}
