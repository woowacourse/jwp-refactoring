package kitchenpos.menu.presentation;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.dto.request.MenuCreateRequest;
import kitchenpos.menu.domain.dto.response.MenuResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MenuRestController {

    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuResponse> create(@RequestBody final MenuCreateRequest request) {
        final Menu created = menuService.create(request.toEntity());
        final URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri)
                .body(MenuResponse.toDTO(created))
                ;
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        List<MenuResponse> response = menuService.list().stream()
                .map(MenuResponse::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(response)
                ;
    }
}
