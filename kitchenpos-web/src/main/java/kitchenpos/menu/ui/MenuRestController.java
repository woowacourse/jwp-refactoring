package kitchenpos.menu.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/menus")
@RestController
public class MenuRestController {

    private final MenuService menuService;

    public MenuRestController(
            final MenuService menuService
    ) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<MenuResponse> create(
            @RequestBody final MenuCreateRequest request
    ) {
        final MenuResponse menuResponse = menuService.create(request);
        final URI uri = URI.create("/api/menus/" + menuResponse.getId());

        return ResponseEntity.created(uri)
                .body(menuResponse)
                ;
    }

    @GetMapping
    public ResponseEntity<List<MenuResponse>> readAll() {
        final List<MenuResponse> menuResponses = menuService.readAll();

        return ResponseEntity.ok()
                .body(menuResponses)
                ;
    }
}
