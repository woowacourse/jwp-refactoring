package kitchenpos.ui.v1;

import java.net.URI;
import java.util.List;
import kitchenpos.application.MenuService;
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/menus")
public class MenuControllerV1 {

    private final MenuService menuService;

    public MenuControllerV1(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<MenuResponse> create(@RequestBody MenuCreateRequest request) {
        var response = menuService.create(request);
        URI uri = URI.create("/api/v1/menus/" + response.getId());
        return ResponseEntity.created(uri)
            .body(response);
    }

    @GetMapping
    public ResponseEntity<List<MenuResponse>> findAll() {
        return ResponseEntity.ok()
            .body(menuService.findAll());
    }
}
